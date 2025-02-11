package ru.chuikov.controller

import org.apache.tika.Tika
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import ru.chuikov.controller.util.LOGIN_FAILED
import ru.chuikov.controller.util.Validator
import ru.chuikov.utils.Watermark

@RestController
@RequestMapping("/lunar-watermark")
class WatermarkController(
    val watermarkService: Watermark,
    val validator: Validator
) {
    data class WaterRequest(
        val message: String,
        val fileimage: MultipartFile
    )

    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun makeWatermark(
        @ModelAttribute request: WaterRequest,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var file = watermarkService.addWatermark(request.fileimage.bytes, request.message)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .contentLength(file.size.toLong())
            .body(
                ByteArrayResource(file)

            )
    }
}

