package com.deevyanshu.pdf_rag.Controller;

import com.deevyanshu.pdf_rag.Service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class PdfController {

    private PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> response(@RequestParam(value = "q",required = true) String q)
    {
        return ResponseEntity.ok(pdfService.response(q));
    }
}
