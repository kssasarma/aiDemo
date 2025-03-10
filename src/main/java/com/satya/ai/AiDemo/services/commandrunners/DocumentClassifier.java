package com.satya.ai.AiDemo.services.commandrunners;

import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

//@Component
public class DocumentClassifier implements CommandLineRunner {

	private final ChatClient chatClient;

	public DocumentClassifier(ChatClient.Builder builder) {
		this.chatClient = builder.defaultAdvisors().build();
	}

	@Value("classpath:docs/Satya Resume - Software Engineer.pdf")
	private Resource awsPdf;

	@Override
	public void run(String... args) throws Exception {
		Tika tika = new Tika();
		String documentContent = tika.parseToString(awsPdf.getInputStream());
		String systemPrompt = "You are an AI assistant specialized in document classification. Your task is to classify documents into predefined categories such as: Resumes, Invoices, Contracts, Reports, or Others. If the document does not fit any category, classify it as \"Others\". Keep responses concise and relevant.";
		String template = "Analyze the following document and classify it into one of these categories: Resumes, Invoices, Contracts, Reports, or Others. If applicable, extract key details such as sender, recipient, date, and subject. Document: {documentContent}";
		PromptTemplate promptTemplate = new PromptTemplate(template);
		Prompt prompt = promptTemplate.create(Map.of("documentContent", documentContent));
		System.out.println(chatClient.prompt(prompt).system(systemPrompt).user(template).call().content());
	}
}
