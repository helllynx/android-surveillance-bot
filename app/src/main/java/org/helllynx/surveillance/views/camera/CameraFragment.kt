package org.helllynx.surveillance.views.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.*
import android.view.TextureView.SurfaceTextureListener
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.helllynx.surveillance.R
import org.helllynx.surveillance.databinding.FragmentCameraBinding


@AndroidEntryPoint
class CameraFragment : Fragment() {
    private val viewModel: CameraViewModel by viewModels()
//    private val args: UserDetailsFragmentArgs by navArgs()

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_camera, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.refreshUserDetails(args.user)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getUserDetails(args.user).observe(viewLifecycleOwner, {
//            viewModel.userDetails.set(it)
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true
        } else {
            // no camera on this device
            return false
        }
    }

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    private var mTextureView: TextureView? = null
    private var mCameraDevice: CameraDevice? = null

    private val mSurfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surfaceTexture: SurfaceTexture,
            width: Int, height: Int
        ) {
            openCamera( requireContext(), width, height)
        }

        override fun onSurfaceTextureSizeChanged(
            surfaceTexture: SurfaceTexture,
            width: Int, height: Int
        ) {
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
            // Detect motion here
        }
    }

    private fun openCamera(context: Context, width: Int, height: Int) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(cameraDevice: CameraDevice) {
                    mCameraDevice = cameraDevice
                    val surfaceTexture = mTextureView!!.surfaceTexture
                    surfaceTexture!!.setDefaultBufferSize(width, height)
                    val surface = Surface(surfaceTexture)
                    try {
                        val builder =
                            mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        builder.addTarget(surface)
                        mCameraDevice!!.createCaptureSession(
                            listOf(surface),
                            object : CameraCaptureSession.StateCallback() {
                                override fun onConfigured(
                                    cameraCaptureSession: CameraCaptureSession
                                ) {
                                    try {
                                        cameraCaptureSession.setRepeatingRequest(
                                            builder.build(), null, null
                                        )
                                    } catch (e: CameraAccessException) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onConfigureFailed(
                                    cameraCaptureSession: CameraCaptureSession
                                ) {
                                }
                            }, null
                        )
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onDisconnected(cameraDevice: CameraDevice) {}
                override fun onError(cameraDevice: CameraDevice, i: Int) {}
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


}