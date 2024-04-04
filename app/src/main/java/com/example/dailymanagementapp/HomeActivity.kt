package com.example.dailymanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailymanagementapp.adapters.TaskAdapter
import com.example.dailymanagementapp.databinding.ActivityHomeBinding
import com.example.dailymanagementapp.databinding.UpdateItemDialogBinding
import com.example.dailymanagementapp.models.NotesData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val noteItems = mutableListOf<NotesData>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
        binding.menuButton.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        recyclerView = binding.taskRv
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Fetch data in RecyclerView from Firebase Database
        val user = auth.currentUser
        val userId = user?.uid
        userId?.let { databaseReference.child("Notes").child(it) }
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noteItems.clear()
                    for (snap in snapshot.children) {
                        val noteItem = snap.getValue(NotesData::class.java)
                        if (noteItem != null) {
                            noteItems.add(noteItem)
                        }
                    }
                    //noteItems.reverse()
                    val adapter = TaskAdapter(noteItems, this@HomeActivity)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onUpdateClicked(noteId: String, description: String) {

        val dialogBinding = UpdateItemDialogBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Task")
            .setPositiveButton("update") { dialog, _ ->
                val newDescription = dialogBinding.updatedDesc.text.toString()
                updateTaskDatabase(noteId, newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updatedDesc.setText(description)
        dialog.show()
    }

    private fun updateTaskDatabase(noteId: String, newDescription: String) {
        val currUser = auth.currentUser
        currUser?.let { user ->
            val noteReference = databaseReference.child("Notes").child(user.uid).child(noteId)
            val updateNote = NotesData(newDescription, noteId)
            noteReference.setValue(updateNote).addOnCompleteListener {task->
                if(task.isSuccessful){
                    Utils.showToast(this , "Task updated successfully!")
                }
                else Utils.showToast(this , "Task updated failed!")
            }
        }
    }

    override fun onDeleteClicked(noteId: String) {
        val currUser = auth.currentUser
        currUser?.let { user ->
            val noteRef = databaseReference.child("Notes").child(user.uid).child(noteId)
            noteRef.removeValue()
        }
    }
}