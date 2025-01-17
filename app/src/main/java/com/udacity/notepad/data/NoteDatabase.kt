package com.udacity.notepad.data

import com.udacity.notepad.data.NotesContract.NoteTable

import android.provider.BaseColumns
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.lang.StringBuilder
import java.util.*

class NoteDatabase(context: Context?) {
    private val helper: NotesOpenHelper

    fun getAll(): List<Note> {
        val cursor = helper.readableDatabase.query(
            NoteTable._TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            NoteTable.CREATED_AT
        )
        val retval = allFromCursor(cursor)
        cursor.close()
        return retval
    }

    fun loadAllByIds(vararg ids: Int): List<Note> {
        val questionMarks = StringBuilder()
        var i = 0
        while (i++ < ids.size) {
            questionMarks.append("?")
            if (i <= ids.size - 1) {
                questionMarks.append(", ")
            }
        }
        val args = arrayOfNulls<String>(ids.size)
        i = 0
        while (i < ids.size) {
            args[i] = Integer.toString(ids[i])
            ++i
        }
        val selection = BaseColumns._ID + " IN (" + questionMarks.toString() + ")"
        val cursor = helper.readableDatabase.query(
            NoteTable._TABLE_NAME,
            null,
            selection,
            args,
            null,
            null,
            NoteTable.CREATED_AT
        )
        val retval = allFromCursor(cursor)
        cursor.close()
        return retval
    }

    fun insert(vararg notes: Note) {
        val values = fromNotes(notes)
        val db = helper.writableDatabase
        db.beginTransaction()
        try {
            for (value in values) {
                db.insert(NoteTable._TABLE_NAME, null, value)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun update(note: Note) {
        val values = fromNote(note)
        helper.writableDatabase.update(
            NoteTable._TABLE_NAME,
            values,
            BaseColumns._ID + " = ?", arrayOf(Integer.toString(note.id))
        )
    }

    fun delete(note: Note) {
        helper.writableDatabase.delete(
            NoteTable._TABLE_NAME,
            BaseColumns._ID + " = ?", arrayOf(Integer.toString(note.id))
        )
    }

    companion object {
        private fun fromCursor(cursor: Cursor): Note {
            var col = 0
            val note = Note()
            note.id = cursor.getInt(col++)
            note.text = cursor.getString(col++)
            note.isPinned = cursor.getInt(col++) != 0
            note.createdAt = Date(cursor.getLong(col++))
            note.updatedAt = Date(cursor.getLong(col))
            return note
        }

        private fun allFromCursor(cursor: Cursor): List<Note> {
            val retval: MutableList<Note> = ArrayList()
            while (cursor.moveToNext()) {
                retval.add(fromCursor(cursor))
            }
            return retval
        }

        private fun fromNote(note: Note): ContentValues {
            val values = ContentValues()
            val id = note.id
            if (id != -1) {
                values.put(BaseColumns._ID, id)
            }
            values.put(NoteTable.TEXT, note.text)
            values.put(NoteTable.IS_PINNED, note.isPinned)
            values.put(NoteTable.CREATED_AT, note.createdAt.time)
            values.put(NoteTable.UPDATED_AT, note.updatedAt!!.time)
            return values
        }

        private fun fromNotes(notes: Array<out Note>): List<ContentValues> {
            val values: MutableList<ContentValues> = ArrayList()
            for (note in notes) {
                values.add(fromNote(note))
            }
            return values
        }
    }

    init {
        helper = NotesOpenHelper(context)
    }
}