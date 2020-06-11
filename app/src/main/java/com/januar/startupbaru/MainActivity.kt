package com.januar.startupbaru

import android.app.DownloadManager
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.inflate
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Add dummy data
        //listNotes.add(Note(1, "We should be fun", "Life is to be enjoyed, not just endured. Live and work but do not forget to play, to have fun in life and really enjoy it. When fun gets deep enough, it can heal the world. There is no pleasure in having nothing to do; the fun is having lots to do and not doing it."))
        //listNotes.add(Note(2, "It's just same like before", "Life is to be enjoyed, not just endured. Live and work but do not forget to play, to have fun in life and really enjoy it. When fun gets deep enough, it can heal the world. There is no pleasure in having nothing to do; the fun is having lots to do and not doing it."))
        //listNotes.add(Note(3, "Nah, nothing change, Mate.", "Life is to be enjoyed, not just endured. Live and work but do not forget to play, to have fun in life and really enjoy it. When fun gets deep enough, it can heal the world. There is no pleasure in having nothing to do; the fun is having lots to do and not doing it."))

        Toast.makeText(this,"OnCreate",Toast.LENGTH_LONG).show()
        LoadQuery("%")
    }

    override fun onResume( ) {
        super.onResume()
        Toast.makeText(this,"OnResume",Toast.LENGTH_LONG).show()
        LoadQuery("%")
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"OnStart",Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"OnPause",Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this,"OnStop",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"OnDestroy",Toast.LENGTH_LONG).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this,"OnCreate",Toast.LENGTH_LONG).show()
    }
    fun LoadQuery(title:String){
        //Load from DB
        var dbManager = DbManager(this)
        val projections= arrayOf("ID", "Title", "Description")
        val selectionArgs= arrayOf(title)
        val cursor= dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do {
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            }while (cursor.moveToNext())
        }
        var myNotesAdapter = NotesAdapter(this, listNotes)
        lVNotes.adapter=myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {

        if (item != null) {
        when(item.itemId){
            R.id.addNote -> { //Go to Add Note Page
                var intent = Intent (this, AddNotes::class.java)
                startActivity(intent)
            }
        }
    }
        return super.onOptionsItemSelected(item)
    }


    inner class NotesAdapter:BaseAdapter{
        var listNotesAdapter=ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNotesAdapter:ArrayList<Note>):super(){
            this.listNotesAdapter=listNotesAdapter
            this.context=context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView=layoutInflater.inflate(R.layout.ticket,null)
            var myNote=listNotesAdapter[p0]
            myView.tVTitle.text=myNote.noteName
            myView.tVDes.text=myNote.noteDes
            myView.ivDelete.setOnClickListener(View.OnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs= arrayOf(myNote.noteID.toString())
                dbManager.Delete("ID=?", selectionArgs)
                LoadQuery("%")
            })
            myView.ivEdit.setOnClickListener(View.OnClickListener{
                GoToUpdate(myNote)
            })
            return myView
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    fun GoToUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("Name",note.noteName)
        intent.putExtra("Des",note.noteDes)
        startActivity(intent)
    }
}
