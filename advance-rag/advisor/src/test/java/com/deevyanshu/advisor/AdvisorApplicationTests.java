package com.deevyanshu.advisor;

import com.deevyanshu.advisor.Service.ChatService;
import com.deevyanshu.advisor.Service.DataLoaderService;
import com.deevyanshu.advisor.Service.DataTransformerService;
import com.deevyanshu.advisor.helper.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AdvisorApplicationTests {

//	@Autowired
//	private ChatService chatService;
//
//	@Test
//	void savedata(){
//		this.chatService.saveData(Helper.getData());
//		System.out.println("saved successfully");
//	}

	@Autowired
	private DataLoaderService dataLoaderService;

	@Autowired
	private DataTransformerService dataTransformerService;

	@Autowired
	private VectorStore vectorStore;

	@Test
	void testLoader()
	{
		var documents=dataLoaderService.loadDocumentFromJson();
		System.out.println(documents.size());
	}

	@Test
	void testPdfLoader(){
		List<Document> documents=dataLoaderService.loadDataFromPdf();
		System.out.println(documents.size());

		documents.forEach(item -> {
			System.out.println(item);
			System.out.println("__________________-");
		});

		var transformedDocument=dataTransformerService.transform(documents);
		System.out.println(transformedDocument.size());

//        going to save the data into database

		this.vectorStore.add(transformedDocument);
		System.out.println("Done");
	}

}
