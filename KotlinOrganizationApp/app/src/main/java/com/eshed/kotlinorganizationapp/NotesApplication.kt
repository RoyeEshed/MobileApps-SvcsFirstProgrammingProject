package com.eshed.kotlinorganizationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.row_main.*

class NotesApplication: AppCompatActivity() {
    var tasks = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        val list = findViewById<ListView>(R.id.listViewItem)

        val ref = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        fun currentUserReference(): DatabaseReference =
                database.child("users").child(ref.currentUser!!.uid)

        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Notes", "This is what I'm looking for" + snapshot.toString())
                 tasks = snapshot.child("tasks").getValue()!! as ArrayList<String>
                Log.d("Notes", "This is what I'm looking for" + tasks.toString())
                Log.d("Notes", "This is what I'm looking for" + tasks[0].toString())
                handler(tasks, list)
            }
        }
        currentUserReference().addValueEventListener(getdata)
        currentUserReference().addListenerForSingleValueEvent(getdata)


        Log.d("Notes", "This is what I'm looking for" + tasks.toString())

        var button = findViewById<Button>(R.id.buttonCreate)

        button.setOnClickListener {
            var getdata =  object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    handler(tasks, list)
                }
            }
            currentUserReference().addValueEventListener(getdata)
            currentUserReference().addListenerForSingleValueEvent(getdata)
        }

        var button2 = findViewById<Button>(R.id.deleteButton)

        button2.setOnClickListener {
            var textDelete = findViewById<EditText>(R.id.editTextNumber).text.toString()
            var num = textDelete.toInt()
            if (num < tasks.size) {
               tasks.removeAt(num)
            }
        }

        //currentUserData
        Log.d("NotesApplication", "This is what I'm looking for right now")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.new_task_create -> {
                val newTask = findViewById<EditText>(R.id.taskName).text.toString()
                tasks.add(newTask)
            }
            R.id.menu_sign_out -> {
                val ref = FirebaseAuth.getInstance()
                val database = FirebaseDatabase.getInstance().reference

                fun currentUserReference(): DatabaseReference = database.child("users").child(ref.currentUser!!.uid)

                currentUserReference().child("tasks").setValue(tasks)

                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun handler(tasks: ArrayList<String>, list: ListView) {
        list.adapter = MyCustomAdapter(this, tasks)
    }

    private class MyCustomAdapter(context: Context, tasks: ArrayList<String>): BaseAdapter() {

        private val mContext: Context
        private val tasklist: ArrayList<String>

        init {
            mContext = context
            tasklist = tasks
            Log.d("Notes", "Moment 37" + tasklist.toString())
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val view = layoutInflater.inflate(R.layout.row_main, p2, false)

            val text = view.findViewById<TextView>(R.id.textEditView)
            val task = tasklist[p0]
            val text2 = view.findViewById<TextView>(R.id.textEditView2)
            text2.text = "" + p0
            text.text = task

            return view
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return tasklist.size
        }

    }
}
