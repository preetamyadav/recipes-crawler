package com.crawler.crawler;

import com.crawler.crawler.model.Recipe;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@Component
public class ContentExtractor {
    private String websiteUrl;

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void extractMainLinks(){
        System.out.println("Main Links\n");
        Map<String,Object> links = new HashMap<>();
        try {
            Document document = Jsoup.connect(this.websiteUrl).get();
            Element element = document.getElementById("recipeListing");
            Elements elements =  element.select("a[href]");
            for (Element element1 : elements){
                links.put(element1.getElementsByAttribute("href").text(),element1.attr("href"));
            }
            System.out.println("Main Links Stop \n");
         this.extractLinks(links);
        }catch (IOException exception){
            exception.getStackTrace();
        }
    }
    private void extractLinks(Map<String,Object> mainLinks){
        System.out.println("Links  \n");

        List<Map<String,Object>> allLinks = new ArrayList<>();
        for (Map.Entry<String,Object> singleLinks:mainLinks.entrySet()) {
            Map<String,Object> links = new HashMap<>();
            try {
                Document document = Jsoup.connect(singleLinks.getValue().toString()).get();
                Element element = document.getElementById("recipeListing");
                Elements elements =  element.select("a[href]");
                for (Element element1 : elements){
                    links.put(element1.getElementsByAttribute("href").text(),element1.attr("href"));
                }
                allLinks.add(links);
            }catch (IOException exception){
                exception.getStackTrace();
            }
        }
        this.extractContent(allLinks);
    }
    public void extractContent( List<Map<String,Object>> allLinks){
        System.out.println("Extract Content \n"+allLinks.size());
        List<Recipe> allRecipe = new ArrayList<>();
        for (Map<String,Object> links :allLinks){
            System.out.println("Extract Content Size\n"+links.size());
            for (Map.Entry<String,Object> singleLinks:links.entrySet()) {

                try {
                    Document document = Jsoup.connect(singleLinks.getValue().toString()).get();
                    Recipe recipe = new Recipe();
                    recipe.setName(singleLinks.getKey());
                    recipe.setAbout(document.getElementsByClass("recipe_description").text());
                    Elements elements2 = document.getElementsByClass("recipe_peocess");
                    for (Element element : elements2) {
                        recipe.setSteps(element.getElementsByClass("stylNew").text());
                        recipe.setIngredients(element.getElementsByClass("ingredients").text());
                    }

                    Elements elements = document.getElementsByClass("recipe-details");
                    for (Element element : elements) {
                        Elements elements1 = element.getElementsByClass("recipe_half");
                        for (Element element1 : elements1){
                            String text =  element1.text();
                            int index = text.indexOf(":");
                            String data = text.substring(index+1,text.length()).trim();
                            if(text.contains("Recipe Servings:")){
                                recipe.setServing(data);
                            }else if(text.contains("Prep Time:")){
                                recipe.setPrepTime(data);
                            }else if (text.contains("Total Cook Time")){
                                recipe.setCookingTime(data);
                            }else {
                                recipe.setDifficultLevel(data);
                            }
                        }
                    }
                    System.out.println(new Gson().toJson(recipe));
                    allRecipe.add(recipe);
                }catch (IOException exception){
                    exception.printStackTrace();
                }
            }

        }
      this.allData(allRecipe);
        System.out.println("Extract Content stop \n");

    }

    private void allData(List<Recipe> allRecipe){
        System.out.println("allData  stop \n");

        try {
            FileWriter file = new FileWriter("./output.json");
            file.write(new Gson().toJson(allRecipe));
            file.close();
            System.out.println("Hahaha");
        }catch (IOException exception){
            exception.getStackTrace();
        }


        System.out.println(new Gson().toJson(allRecipe));
    }
}
