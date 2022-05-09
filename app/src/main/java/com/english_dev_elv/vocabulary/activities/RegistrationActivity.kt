package com.english_dev_elv.vocabulary.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.english_dev_elv.vocabulary.MainActivity
import com.english_dev_elv.vocabulary.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

private const val TAG = "RegistrationActivity"

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButtonRegister.setOnClickListener {
            performRegister()
        }

        binding.selectPhotoButtonRegister.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.alreadyHaveAccountRegister.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            binding.selectPhotoImageviewRegister.setImageBitmap(bitmap)
            binding.selectPhotoButtonRegister.alpha = 0f

            Log.d(TAG, "onActivityResult: ${selectedPhotoUri.toString()}")

            //  val selectedPhoto = Glide.with(this).load(selectedPhotoUri).into(binding.selectPhotoButtonRegister)
        }
    }

    private fun performRegister() {

        val password = binding.passwordEdittextRegistration.text.toString()
        val email = binding.emailEdittextRegistration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "Successfully created user with uid: ${it.result.user?.uid} ")

                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Ошибка при создании пользователя: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        Log.d(TAG, "smth")
        if (selectedPhotoUri == null) {
            Log.d(TAG, "$selectedPhotoUri")
        } else {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {

                    Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "File location: $it ")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "uploadImageToFirebaseStorage: ${it.message}")

                }
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.usernameEdittextRegistration.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

class User(val uid: String, val username: String, val profileImageName: String) {
    constructor(): this("","", "")
}