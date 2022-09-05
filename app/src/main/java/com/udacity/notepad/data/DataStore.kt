package com.udacity.notepad.data

import android.content.Context
import com.udacity.notepad.data.NoteDatabase
import com.udacity.notepad.data.DataStore
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object DataStore {
    val EXEC: Executor = Executors.newSingleThreadExecutor()
    @JvmStatic
    lateinit var notes: NoteDatabase
        private set

    @JvmStatic
    fun init(context: Context?) {
        notes = NoteDatabase(context)
    }

    @JvmStatic
    fun execute(runnable: Runnable?) {
        EXEC.execute(runnable)
    }
}