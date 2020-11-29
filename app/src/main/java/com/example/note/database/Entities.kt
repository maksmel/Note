package com.example.note.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    val title: String,
    val body: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)