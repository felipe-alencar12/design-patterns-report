package com.report.report_api_pattern.controller;

import com.report.report_api_pattern.dto.ResumeRequestDTO;
import com.report.report_api_pattern.service.impl.PdfServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final PdfServiceImpl pdfService;

    public ResumeController(PdfServiceImpl pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> getResumePdf(@RequestBody ResumeRequestDTO dto) {
        byte[] pdfBytes = pdfService.generateResumePdf(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=curriculo.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}

