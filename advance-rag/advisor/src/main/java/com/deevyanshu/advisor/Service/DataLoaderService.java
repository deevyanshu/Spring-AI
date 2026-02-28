package com.deevyanshu.advisor.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderService {

    @Value("classpath:sample_data.json")
    private Resource resource;

    @Value("classpath:DeevyanshuGarg_Resume.pdf")
    private Resource pdfResource;
    public List<Document> loadDocumentFromJson()
    {
        var jsonReader=new JsonReader(resource);
        var listDocuments=jsonReader.read();

        return listDocuments;
    }

    public List<Document> loadDataFromPdf()
    {
        var pdfReader=new PagePdfDocumentReader(pdfResource, PdfDocumentReaderConfig.builder().
                withPageTopMargin(0).withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0).build()).withPagesPerDocument(1).build());

        return pdfReader.read();
    }
}
