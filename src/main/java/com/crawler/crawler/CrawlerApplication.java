package com.crawler.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);
		ContentExtractor contentExtractor = context.getBean(ContentExtractor.class);
		contentExtractor.setWebsiteUrl("https://food.ndtv.com/recipes");
		contentExtractor.extractMainLinks();
		System.out.println("Hello");
	}

}
