package com.codewithkael.simplertmpproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.nodemedia.NodePlayer
import cn.nodemedia.NodePublisher
import com.codewithkael.simplertmpproject.Utils.STREAM_URL
import com.codewithkael.simplertmpproject.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {

    private lateinit var views:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)
        views.startBtn.setOnClickListener {
            PermissionX.init(this)
                .permissions(android.Manifest.permission.CAMERA)
                .request{allGranted,_,_ ->
                    if (allGranted){
                        //start the process
                        startPublishing()
                        startPlaying()

                    }else{
                        Toast.makeText(this, "permissions are required", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startPublishing(){
        val nodePublisher = NodePublisher(this,"")
        nodePublisher.apply {
            attachView(views.publishingLayout)
            setVideoOrientation(NodePublisher.VIDEO_ORIENTATION_PORTRAIT)
            setVideoCodecParam(
                NodePublisher.NMC_CODEC_ID_H264,
                NodePublisher.NMC_PROFILE_AUTO,
                480,   // Width (pixels)
                760,   // Height (pixels)
                30,     // Frame rate (fps)
                2_500_000 // Bit rate (bps)
            )
            openCamera(false)
            start(STREAM_URL)
        }

    }

    private fun startPlaying(){
        val nodePlayer = NodePlayer(this,"")
        nodePlayer.attachView(views.receivingLayout)
        nodePlayer.start(STREAM_URL)
    }
}