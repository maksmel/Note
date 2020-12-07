package com.example.note.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.note.App
import com.example.note.R
import com.example.note.database.AppDatabase
import com.example.note.database.Category
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
        launch {
            val categories = withContext(Dispatchers.IO) {App.db.categoryDao().getAll()}

            inputCategoryNote.setAdapter(
                    ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            categories.map { it.name }
                    )
            )
            
            inputCategoryNote.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) inputCategoryNote.showDropDown()
            }

        }

        btnAddNote.setOnClickListener {
            val title = inputTitleNote.text.toString()
            val body = inputTextNote.text.toString()
            val category = inputCategoryNote.text.toString()

            if (title.isNotEmpty() && body.isNotEmpty() && category.isNotEmpty())

                launch {
                    withContext(Dispatchers.IO) { App.db.categoryDao().insert(Category(category)) }
                    withContext(Dispatchers.IO) { App.db.noteDao().insert(Note(title, body, category)) }
                    findNavController().popBackStack()
                }
            else
            (AnimatorInflater.loadAnimator(requireContext(), R.animator.error_animation) as AnimatorSet).apply {
                setTarget(inputTitleNote)
                start()
            }
            (AnimatorInflater.loadAnimator(requireContext(), R.animator.error_animation) as AnimatorSet).apply {
                setTarget(inputTextNote)
                start()
            }
            (AnimatorInflater.loadAnimator(requireContext(), R.animator.error_animation) as AnimatorSet).apply {
                setTarget(inputCategoryNote)
                start()
            }
        }
    }
}