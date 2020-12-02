package com.example.note.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.note.App
import com.example.note.R
import com.example.note.database.AppDatabase
import com.example.note.database.Note
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.item_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddNoteFragment : Fragment(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAddNote.setOnClickListener {
            val title = inputTitleNote.text.toString()
            val body = inputTextNote.text.toString()

            if (title.isNotEmpty() && body.isNotEmpty())

                launch {
                    withContext(Dispatchers.IO) { App.db.noteDao().insert(Note(title, body)) }
                    findNavController().popBackStack()
                }
        }
    }
}