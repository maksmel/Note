package com.example.note.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.note.App
import com.example.note.R
import com.example.note.database.Category
import com.example.note.database.Note
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.*


class AddNoteFragment : Fragment(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            val categories = withContext(Dispatchers.IO) { App.db.categoryDao().getAll() }

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

            when {
                title.isEmpty() -> {
                    AnimatorSet().apply {
                        playTogether(
                                ObjectAnimator.ofFloat(inputTitleNote, "translationX", 1f, 10f, 1f),
                                ObjectAnimator.ofArgb(
                                            inputTitleNote,
                                            "hintColor",
                                            resources.getColor(R.color.black),
                                            resources.getColor(R.color.red),
                                            resources.getColor(R.color.black)
                                    ).apply {
                                        addUpdateListener {
                                            val animatedValue = it.animatedValue as Int
                                            inputTitleNote.backgroundTintList = ColorStateList.valueOf(animatedValue)
                                        }
                                    }
                        )
                        start()
                    }
                }

                body.isEmpty() -> {
                    AnimatorSet().apply {
                        playTogether(
                                ObjectAnimator.ofFloat(inputTextNote, "translationX", 1f, 10f, 1f),
                                ObjectAnimator.ofArgb(
                                        inputTextNote,
                                        "hintColor",
                                        resources.getColor(R.color.black),
                                        resources.getColor(R.color.red),
                                        resources.getColor(R.color.black)
                                ).apply {
                                    addUpdateListener {
                                        val animatedValue = it.animatedValue as Int
                                        inputTextNote.backgroundTintList = ColorStateList.valueOf(animatedValue)
                                    }
                                }
                        )
                        start()
                    }
                }

                category.isEmpty() -> {
                    AnimatorSet().apply {
                        playTogether(
                                ObjectAnimator.ofFloat(inputCategoryNote, "translationX", 1f, 10f, 1f),
                                ObjectAnimator.ofArgb(
                                        inputCategoryNote,
                                        "hintColor",
                                        resources.getColor(R.color.black),
                                        resources.getColor(R.color.red),
                                        resources.getColor(R.color.black)
                                ).apply {
                                    addUpdateListener {
                                        val animatedValue = it.animatedValue as Int
                                        inputCategoryNote.backgroundTintList = ColorStateList.valueOf(animatedValue)
                                    }
                                }
                        )
                        start()
                    }
                }

                else -> launch {
                    withContext(Dispatchers.IO) { App.db.categoryDao().insert(Category(category)) }
                    withContext(Dispatchers.IO) { App.db.noteDao().insert(Note(title, body, category)) }
                    findNavController().popBackStack()
                }
            }
        }
    }
}