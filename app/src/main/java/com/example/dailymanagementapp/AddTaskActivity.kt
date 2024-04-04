package com.example.dailymanagementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dailymanagementapp.databinding.ActivityAddTaskBinding
import com.example.dailymanagementapp.models.NotesData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase



class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.saveButton.setOnClickListener {
            val content = binding.taskData.text.toString().trim()
            if(content.isEmpty()){
                Utils.showToast(this , "Please write something!")
            }
            else{
                val user = auth.currentUser
                if(user != null){
                    val userId = user.uid
                    val key = database.getReference("Notes").child(userId).push().key
                    val noteData = NotesData(content , key)

                    if (key != null) {
                        database.getReference("Notes").child(userId).child(key).setValue(noteData)
                            .addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    Utils.showToast(this , "note save successfully!")
                                    binding.taskData.text.clear()
                                }
                            }
                    }
                }
            }
        }
    }
}