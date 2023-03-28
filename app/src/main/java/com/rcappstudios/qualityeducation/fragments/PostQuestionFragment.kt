package com.rcappstudios.qualityeducation.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentPostQuestionBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.rcappstudios.qualityeducation.utils.LoadingDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.security.auth.Subject

class PostQuestionFragment : Fragment() {

    private lateinit var binding: FragmentPostQuestionBinding
    private var dataCode: Int = 0
    private  var  imageUri : Uri?= null
    private lateinit var subjectName: String
    private lateinit var loadingDialog: LoadingDialog

    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
            if (dataCode == 1) {
                val thumbnail: Bitmap = it.data!!.extras!!.get("data") as Bitmap
                binding.ivQuestion.visibility = View.VISIBLE
                binding.ivQuestion.setImageBitmap(thumbnail)
                imageUri = getImageUri(thumbnail)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity(), "Loading Please wait....")
        clickListener()
    }

    private fun clickListener(){
        binding.submit.setOnClickListener {
            extractDetails()
        }

        binding.addImage.setOnClickListener{
            checkForPermission(1)
        }
    }

    private fun checkForPermission(code: Int) {
        Dexter.withContext(requireActivity())
            .withPermissions(listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        if (code == 1) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            dataCode = 1
                            getImage.launch(intent)
                            Toast.makeText(requireContext(),
                                "All permission checked",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        showRationalDialogForPermissions()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()
    }

    private fun extractDetails(){
        if(binding.etQuestion.text.toString().isNotEmpty() && imageUri != null){
            loadingDialog.startLoading()
            storeImage()
        }else {
            loadingDialog.startLoading()
            storeToDatabase("")
        }
    }

    private fun storeImage(){
        FirebaseStorage.getInstance()
            .getReference("File/${Calendar.getInstance().timeInMillis}")
            .putFile(imageUri!!).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {url->
                    storeToDatabase(url.toString())
                }
            }
    }

    private fun storeToDatabase(imageUrl: String){
        val questionID = FirebaseDatabase.getInstance().reference.push().key
        if(imageUrl == ""){
            FirebaseDatabase.getInstance().getReference("Questions/${questionID}")
                .setValue(
                    QuestionModel(
                        question = binding.etQuestion.text.toString(),
                        questionID = questionID.toString(),
                        timeStamp = Calendar.getInstance().timeInMillis,
                        userImageUrl = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),
                        userName = FirebaseAuth.getInstance().currentUser!!.displayName
//                        subjectName = subjectName
                        )).addOnSuccessListener {
                            requireActivity().onBackPressed()
                        }.addOnSuccessListener {
                            loadingDialog.dismiss()
                }
        } else {
            FirebaseDatabase.getInstance().getReference("Questions/${questionID}")
                .setValue(
                    QuestionModel(
                        question = binding.etQuestion.text.toString(),
                        questionID = questionID.toString(),
                        timeStamp = Calendar.getInstance().timeInMillis,
                        userImageUrl = "https://picsum.photos/200/300",
                        userName = FirebaseAuth.getInstance().currentUser!!.displayName,
//                        subjectName = subjectName,
                        imageUrl = imageUrl
                    )).addOnSuccessListener {
                        requireActivity().onBackPressed()
                    }.addOnSuccessListener { loadingDialog.dismiss() }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireContext()).setMessage("Please enable the required permissions")
            .setPositiveButton("GO TO SETTINGS")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


}