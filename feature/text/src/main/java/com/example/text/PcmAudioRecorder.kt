package com.example.text


import android.annotation.SuppressLint
import android.media.AudioFormat.CHANNEL_IN_STEREO
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream

class PcmAudioRecorder : AudioRecorder {

    private val sampleRate = 44100
    private val channelConfig = CHANNEL_IN_STEREO
    private val audioFormat = ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private var audioRecord: AudioRecord? = null

    private var isRecording = false
    private lateinit var recordingThread: Thread

    @SuppressLint("MissingPermission")
    override fun start(outputFile: File) {
        isRecording = true
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )
        audioRecord!!.startRecording()
        recordingThread = Thread { writeAudioDataToFile(outputFile) }
        recordingThread.start()
    }

    override fun stop() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun writeAudioDataToFile(file: File) {
        val outputStream = FileOutputStream(file)
        val data = ByteArray(bufferSize)

        while (isRecording) {
            val read = audioRecord!!.read(data, 0, data.size)
            if (read > 0) {
                outputStream.write(data, 0, read)
            }
        }

        outputStream.close()
    }
}