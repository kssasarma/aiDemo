package com.satya.ai.AiDemo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

	private final ChatClient chatClient;

	public ChatController(ChatClient.Builder builder, PgVectorStore vectorStore) {
		this.chatClient = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore)).build();
	}

	@GetMapping("/")
	public String chat() {
		return chatClient.prompt().user("Explain about AWS Core Service Categories").call().content();
	}
}
