package com.example.note.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.database.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter(
        private val deleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteHolder>() {

    private val notes = mutableListOf<Note>()

    fun updateNotes(newNotes: List<Note>) {
        val startIndex = notes.size
        val notesToInsert = newNotes.filter { it !in notes }
        notes.addAll(notesToInsert)
        notifyItemRangeInserted(startIndex, notesToInsert.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(notes[position]) {
            deleteNote(it)
            notes.removeAt(position)
            notifyItemRemoved(position)

        }
    }

    override fun getItemCount() = notes.size

    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(note: Note, deleteNote: (Note) -> Unit) = itemView.apply {
            titleNote.text = note.title
            bodyNote.text = note.body
            categoryNote.text = note.categoryName
            btnDelete.setOnClickListener { deleteNote(note) }
        }
    }
}
