package ru.chuikov.controller

import org.apache.tika.Tika
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import ru.chuikov.utils.Watermark

@RestController
@RequestMapping("/lunar-watermark")
class WatermarkController(
    val watermarkService: Watermark,
) {
    data class WaterRequest(
        val message: String,
        val fileimage: MultipartFile
    )

    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun makeWatermark(@ModelAttribute request: WaterRequest): ResponseEntity<Resource> {
        var file = watermarkService.addWatermark(request.fileimage.bytes, request.message)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .contentLength(file.size.toLong())
            .body(
                ByteArrayResource(file)

            )
    }
}

