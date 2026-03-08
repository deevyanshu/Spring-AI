package com.deevyanshu.pdf_rag.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;

@Service
public class IngestionService {

    //ETL pipeline
    @Value("classpath:DeevyanshuGarg_Resume.pdf")
    private Resource resource;

    @Autowired
    private PineconeVectorStore vectorStore;

    //Extracting from file
    public List<Document> loadpdf()
    {
        var pdfReader=new PagePdfDocumentReader(resource, PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0).withPageExtractedTextFormatter(ExtractedTextFormatter
                        .builder().withNumberOfTopTextLinesToDelete(0).build()).withPagesPerDocument(1).build());
        return pdfReader.read();
    }

    //Transforming
    public List<Document> transform(List<Document> documents)
    {
        TextSplitter splitter=new TokenTextSplitter();
        return splitter.transform(documents).stream().map(doc->{
            Map<String,Object> metadata=new HashMap<>();
            metadata.put("resume","deevyanshu");
            return new Document(doc.getText(),metadata);
        }).collect(Collectors.toList());
    }

    //loading data into vectordb
    public void loadData(List<Document> documents)
    {
        this.vectorStore.add(documents,"user1");
    }
}
