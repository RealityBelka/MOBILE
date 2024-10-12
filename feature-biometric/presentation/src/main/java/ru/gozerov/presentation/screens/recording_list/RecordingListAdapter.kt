package ru.gozerov.presentation.screens.recording_list

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.ItemRecordingBinding
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.math.absoluteValue

class RecordingListAdapter(
    private val onTryClick: (step: Int, fail: String?) -> Unit
) : RecyclerView.Adapter<RecordingListAdapter.ViewHolder>() {

    var data: List<VoiceRecording> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemRecordingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val backAmplitudes = mutableListOf<Float>()
        private val frontAmplitudes = mutableListOf<Float>()
        private var isPlaying: Boolean = false
        private var mediaPlayer: MediaPlayer? = null
        private var playingJob: Job? = null
        private var duration = 0

        private val coroutineScope = CoroutineScope(Dispatchers.IO)


        fun bind(data: VoiceRecording, onTryClick: (step: Int, fail: String?) -> Unit) {
            with(binding) {
                val context = root.context

                imgHint.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (data.isSuccess) R.drawable.ic_check else R.drawable.ic_alert
                    )
                )

                txtTryOneMoreTime.setOnClickListener { onTryClick(data.step, data.fail) }

                txtCurrentRecording.text = context.getString(R.string.recording_is, data.step)

                txtHint.text =
                    if (data.isSuccess) root.context.getString(R.string.done)
                    else data.fail ?: context.getString(R.string.default_voice_hint)

                imgPlay.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                )
                binding.backRecording.visibility = View.INVISIBLE

                val audioFilePath =
                    "${context.externalCacheDir?.absolutePath}/record_${data.step}.aac"

                binding.imgPlay.setOnClickListener {
                    if (!isPlaying)
                        playAudio(audioFilePath, binding)
                    else
                        stopAudio(binding)
                }

                coroutineScope.launch(Dispatchers.IO) {
                    processAudioFile(audioFilePath, binding)
                }
            }
        }

        private fun processAudioFile(filePath: String, binding: ItemRecordingBinding) {
            try {
                val extractor = MediaExtractor()
                extractor.setDataSource(filePath)

                val format: MediaFormat = extractor.getTrackFormat(0)
                val mime: String = format.getString(MediaFormat.KEY_MIME) ?: return

                if (mime.startsWith("audio/")) {
                    extractor.selectTrack(0)

                    val codec = MediaCodec.createDecoderByType(mime)
                    codec.configure(format, null, null, 0)
                    codec.start()

                    val outputBufferInfo = MediaCodec.BufferInfo()
                    var isEOS = false

                    while (!isEOS) {
                        val inputIndex = codec.dequeueInputBuffer(10000)
                        if (inputIndex >= 0) {
                            val inputBuffer = codec.getInputBuffer(inputIndex)
                            val sampleSize = extractor.readSampleData(inputBuffer!!, 0)

                            if (sampleSize > 0) {
                                codec.queueInputBuffer(
                                    inputIndex,
                                    0,
                                    sampleSize,
                                    extractor.sampleTime,
                                    0
                                )
                                extractor.advance()
                            } else {
                                codec.queueInputBuffer(
                                    inputIndex,
                                    0,
                                    0,
                                    0,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                )
                                isEOS = true
                                binding.root.post {
                                    binding.backRecording.visibility = View.VISIBLE
                                }
                            }
                        }

                        val outputIndex = codec.dequeueOutputBuffer(outputBufferInfo, 10000)
                        if (outputIndex >= 0) {
                            val outputBuffer = codec.getOutputBuffer(outputIndex)

                            if (outputBuffer != null) {
                                // Process the buffer with compaction
                                compactAndProcessBuffer(outputBuffer, binding)
                            }

                            codec.releaseOutputBuffer(outputIndex, false)
                        }
                    }

                    codec.stop()
                    codec.release()
                    extractor.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun compactAndProcessBuffer(buffer: ByteBuffer, binding: ItemRecordingBinding) {
            val amplitudes = mutableListOf<Float>()

            val amplitude = buffer.short.toFloat().absoluteValue
            amplitudes.add(amplitude)
            backAmplitudes.add(amplitude)

            binding.root.post {
                binding.backRecording.addAmplitude(amplitude)
            }

        }

        private fun playAudio(filePath: String, binding: ItemRecordingBinding) {
            if (frontAmplitudes.size == backAmplitudes.size) {
                frontAmplitudes.clear()
                binding.frontRecording.clear()
            }
            binding.imgPlay.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_pause
                )
            )
            isPlaying = true
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(filePath)
                        prepare()
                        start()
                    }

                    this@ViewHolder.duration = mediaPlayer?.duration ?: 10

                    playingJob = coroutineScope.launch {
                        backAmplitudes.forEach { amplitude ->
                            binding?.let {
                                binding.root.post {
                                    binding?.let {
                                        frontAmplitudes.add(amplitude)
                                        binding.frontRecording.addAmplitude(amplitude)
                                    }
                                }
                                delay(duration / backAmplitudes.size.toLong())
                            }
                        }

                        stopAudio(binding)
                        releaseMediaPlayer()
                    }


                } else {
                    mediaPlayer?.start()
                    playingJob = coroutineScope.launch {
                        for (i in frontAmplitudes.size..<backAmplitudes.size) {
                            val amplitude = backAmplitudes[i]
                            binding?.let {
                                binding.root.post {
                                    binding?.let {
                                        frontAmplitudes.add(amplitude)
                                        binding.frontRecording.addAmplitude(amplitude)
                                    }
                                }
                                delay(duration / backAmplitudes.size.toLong())
                            }
                        }

                        stopAudio(binding)
                        releaseMediaPlayer()
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun stopAudio(binding: ItemRecordingBinding) {
            binding.imgPlay.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_play
                )
            )
            playingJob?.cancel()
            mediaPlayer?.let {
                try {
                    if (it.isPlaying) {
                        it.stop()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            }
            isPlaying = false
        }

        private fun releaseMediaPlayer() {
            mediaPlayer?.release()
            mediaPlayer = null
            isPlaying = false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRecordingBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onTryClick)
    }

}