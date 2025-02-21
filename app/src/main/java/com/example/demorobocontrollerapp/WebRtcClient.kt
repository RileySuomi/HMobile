package com.example.demorobocontrollerapp

import android.content.Context
import org.json.JSONObject
import org.webrtc.*
import okhttp3.*

// TODO: USB CAMERA SETUP
/**
 * WebRTCClient handles:
 * - Creating a PeerConnection.
 * - Exchanging SDP offers/answers and ICE candidates via WebSocket signaling.
 * - Rendering the remote video stream.
 *
 * @param context The Android context.
 * @param signalingUrl The URL for the WebSocket signaling server.
 */
class WebRTCClient(private val context: Context, private val signalingUrl: String) {

    lateinit var peerConnectionFactory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null
    private var webSocket: WebSocket? = null

    /**
     * Initializes the WebRTC client:
     * - Sets up the PeerConnectionFactory.
     * - Creates a PeerConnection with ICE servers.
     * - Connects to the signaling server via WebSocket.
     * - Prepares to render the remote video stream on the provided SurfaceViewRenderer.
     *
     * @param surfaceViewRenderer The view where remote video will be displayed.
     */
    fun init(surfaceViewRenderer: SurfaceViewRenderer) {
        // Initialize WebRTC
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )
        peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        // Configure ICE servers (Google's public STUN server)
//        val iceServers = listOf(
//            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
//        )
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("turn:turnserver.example.com:3478")
                .setUsername("user")
                .setPassword("pass")
                .createIceServer()
        )


        // Create PeerConnection with ICE configuration
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let {
                    val json = JSONObject().apply {
                        put("candidate", it.sdp)
                        put("sdpMid", it.sdpMid)
                        put("sdpMLineIndex", it.sdpMLineIndex)
                    }
                    webSocket?.send(json.toString())
                }
            }

            override fun onAddStream(mediaStream: MediaStream?) {
                mediaStream?.videoTracks?.get(0)?.addSink(surfaceViewRenderer)
            }

            //  Required new WebRTC API method
            override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
                mediaStreams?.forEach { stream ->
                    stream.videoTracks.firstOrNull()?.addSink(surfaceViewRenderer)  //  Display the USB Camera stream
                }
            }

            // Required WebRTC Observer Methods
            override fun onSignalingChange(newState: PeerConnection.SignalingState?) {}

            override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {}

            override fun onIceConnectionReceivingChange(receiving: Boolean) {}

            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}

            override fun onRemoveStream(mediaStream: MediaStream?) {}

            override fun onDataChannel(dc: DataChannel?) {}

            override fun onRenegotiationNeeded() {}

            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState) {} // Fixed missing method
        })

        //  Set up WebSocket connection to WebRTC signaling server
        val client = OkHttpClient()
        val request = Request.Builder().url(signalingUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                //  When WebSocket opens, send SDP Offer
                peerConnection?.createOffer(object : SdpObserver {
                    override fun onCreateSuccess(desc: SessionDescription?) {
                        desc?.let {
                            peerConnection?.setLocalDescription(object : SdpObserver {
                                override fun onSetSuccess() {
                                    val json = JSONObject().apply {
                                        put("sdp", it.description)
                                        put("type", it.type.canonicalForm())
                                    }
                                    webSocket.send(json.toString()) //  Send SDP offer
                                }
                                override fun onSetFailure(error: String?) {}
                                override fun onCreateSuccess(desc: SessionDescription?) {}
                                override fun onCreateFailure(error: String?) {}
                            }, it)
                        }
                    }
                    override fun onCreateFailure(error: String?) {}
                    override fun onSetSuccess() {}
                    override fun onSetFailure(error: String?) {}
                }, MediaConstraints())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                //  When a signaling message is received
                val json = JSONObject(text)
                if (json.has("sdp")) {
                    val remoteSdp = SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(json.getString("type")),
                        json.getString("sdp")
                    )
                    peerConnection?.setRemoteDescription(object : SdpObserver {
                        override fun onSetSuccess() {}
                        override fun onSetFailure(error: String?) {}
                        override fun onCreateSuccess(desc: SessionDescription?) {}
                        override fun onCreateFailure(error: String?) {}
                    }, remoteSdp)
                } else if (json.has("candidate")) {
                    val candidate = IceCandidate(
                        json.getString("sdpMid"),
                        json.getInt("sdpMLineIndex"),
                        json.getString("candidate")
                    )
                    peerConnection?.addIceCandidate(candidate)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {}
        })
    }
}
