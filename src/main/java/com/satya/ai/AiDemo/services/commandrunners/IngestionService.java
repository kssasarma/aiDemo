package com.satya.ai.AiDemo.services.commandrunners;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

//@Component
public class IngestionService implements CommandLineRunner {

	private final VectorStore vectorStore;

//	@Value("classpath:docs/080-AWS-Cloud-Practitioner.pdf")
	@Value("classpath:docs/Satya Resume - Software Engineer.pdf")
	private Resource awsPdf;

	public IngestionService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@Override
	public void run(String... args) throws Exception {

		var pdfReader = new PagePdfDocumentReader(awsPdf);

		TextSplitter textSplitter = new TokenTextSplitter();

		//vectorStore.accept(textSplitter.apply(pdfReader.get()));

		System.out.println("Vector store completed");

	}
}
