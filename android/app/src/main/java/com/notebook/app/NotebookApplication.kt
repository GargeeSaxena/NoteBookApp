package com.notebook.app

import android.app.Application

class NotebookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NotebookApplication
            private set
    }
}


