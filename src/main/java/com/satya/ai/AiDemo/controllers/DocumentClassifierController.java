package com.satya.ai.AiDemo.controllers;

import java.io.IOException;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/documents")
public class DocumentClassifierController {

	private final ChatClient chatClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public DocumentClassifierController(ChatClient.Builder builder) {
		this.chatClient = builder.defaultAdvisors().build();
	}

	@PostMapping("/")
	public JsonNode chat(@RequestParam("file") MultipartFile file) throws IOException, TikaException {

		Tika tika = new Tika();
		String documentContent = tika.parseToString(file.getInputStream());

		String systemPrompt = "You are an AI assistant specialized in document classification. Your task is to classify documents into predefined categories such as: Resumes, Invoices, Contracts, Reports, or Others. If the document does not fit any category, classify it as \"Others\". Always return the response in a structured JSON format. Keep responses concise and relevant.";
		String template = "Analyze the following document and classify it into one of these categories: Resumes, Invoices, Contracts, Reports, or Others. If applicable, extract key details such as sender, recipient, date, and subject. Return a structured JSON response. Document: {documentContent}";
		PromptTemplate promptTemplate = new PromptTemplate(template);
		Prompt prompt = promptTemplate.create(Map.of("documentContent", documentContent));
		return objectMapper.readTree(extractJson(chatClient.prompt(prompt).system(systemPrompt).call().content()));
	}

	private String extractJson(String response) {
		// Remove unwanted ```json markers if present
		return response.replaceAll("```json|```", "").trim();
	}

}
