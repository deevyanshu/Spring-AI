package com.deevyanshu.pdf_rag;

import com.deevyanshu.pdf_rag.Service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PdfRagApplicationTests {

	@Autowired
	private IngestionService service;

	@Test
	public void loadData()
	{
		List<Document> documents=service.loadpdf();
		System.out.println(documents);
		var transformedDocuments=service.transform(documents);
		System.out.println("size is: "+transformedDocuments.size());
		service.loadData(transformedDocuments);
	}

}
