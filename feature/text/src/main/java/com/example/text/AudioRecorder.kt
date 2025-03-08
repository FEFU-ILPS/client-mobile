package com.example.text

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}