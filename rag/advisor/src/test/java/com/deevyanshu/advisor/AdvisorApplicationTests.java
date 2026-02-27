package com.deevyanshu.advisor;

import com.deevyanshu.advisor.Service.ChatService;
import com.deevyanshu.advisor.helper.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdvisorApplicationTests {

	@Autowired
	private ChatService chatService;

	@Test
	void savedata(){
		this.chatService.saveData(Helper.getData());
		System.out.println("saved successfully");
	}



}
