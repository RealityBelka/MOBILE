package ru.gozerov.presentation.screens.recording_list

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.ItemRecordingBinding
import java.nio.ByteBuffer
import kotlin.math.absoluteValue

class RecordingListAdapter : RecyclerView.Adapter<RecordingListAdapter.ViewHolder>() {

    var data: List<Recording> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemRecordingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recording) {
            with(binding) {
                val context = root.context

                imgHint.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (data.isSuccess) R.drawable.ic_check else R.drawable.ic_alert
                    )
                )

                txtCurrentRecording.text = context.getString(R.string.recording_is, data.step)

                txtHint.text =
                    if (data.isSuccess) root.context.getString(R.string.done)
                    else data.fail ?: context.getString(R.string.default_voice_hint)

                imgPlay.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (data.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                )
                binding.backRecording.visibility = View.INVISIBLE

                val audioFilePath = "${context.externalCacheDir?.absolutePath}/record_3.aac"

                // Process audio file asynchronously to avoid blocking UI
                GlobalScope.launch(Dispatchers.IO) {
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
                                codec.queueInputBuffer(inputIndex, 0, sampleSize, extractor.sampleTime, 0)
                                extractor.advance()
                            } else {
                                codec.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
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

            binding.root.post {
                binding.backRecording.addAmplitude(amplitude)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRecordingBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
