package com.alden.realdimedb

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alden.realdimedb.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val database = FirebaseDatabase.getInstance()
    private val TAG = "MainActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnChangeData.setOnClickListener {
            val myRef = database.getReference("lampu 1").child("suhu").child("kandang2")
            myRef.setValue(binding.etTeks.text.toString())
        }
        // Read from the database
        val myRef = database.getReference("lampu 1").child("suhu").child("kandang2")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
                binding.tvLoadText.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        initDate()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDate(){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MMMM-DD")
        val current : LocalDateTime = LocalDateTime.now()
        val theDay = LocalDateTime.parse("2020-12-22T16:33:55.707")
        val formatted = current.format(formatter)
        lateinit var mightyDate : LocalDateTime

        val dateRevEvo = database.getReference("kandang1").child("mightyDate")
        dateRevEvo.get().addOnSuccessListener { doc ->
            mightyDate = LocalDateTime.parse(doc.value.toString())
            binding.tvDay.text = Duration.between(mightyDate, current).toDays().toString()
        }

//        val dateRev = database.getReference("lampu 1").child("nowday")
//        dateRev.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })




    }
}