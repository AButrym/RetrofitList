package softserve.academy.myapplication.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId

private val inputFormatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
private val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun formatDate(iso: String): String = try {
    val odt = OffsetDateTime.parse(iso, inputFormatter)
    odt.atZoneSameInstant(ZoneId.systemDefault()).format(outputFormatter)
} catch (e: Exception) {
    iso
}
