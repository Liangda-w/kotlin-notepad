package com.udacity.notepad.data

import android.provider.BaseColumns

object NotesContract {
    const val SQL_CREATE_ENTRIES =
        """CREATE TABLE ${NoteTable._TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            ${NoteTable.TEXT} TEXT, 
            ${NoteTable.IS_PINNED} INTEGER, 
            ${NoteTable.CREATED_AT} INTEGER, 
            ${NoteTable.UPDATED_AT} INTEGER)"""

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NoteTable._TABLE_NAME
    const val SQL_QUERY_ALL = "SELECT * FROM ${NoteTable._TABLE_NAME} ORDER BY " + NoteTable.CREATED_AT + " DESC"

    interface NoteTable : BaseColumns {
        companion object {
            const val _TABLE_NAME = "notes"
            const val TEXT = "text"
            const val IS_PINNED = "is_pinned"
            const val CREATED_AT = "created_at"
            const val UPDATED_AT = "updated_at"
        }
    }
}