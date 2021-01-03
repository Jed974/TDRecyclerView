package com.example.tdrecyclerview.tasklist

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    @RequiresApi(Build.VERSION_CODES.N)
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun serialize(encoder: Encoder, value: Date) =
            encoder.encodeString(formatter.format(value))

    @RequiresApi(Build.VERSION_CODES.N)
    override fun deserialize(decoder: Decoder): Date =
            formatter.parse(decoder.decodeString())
}