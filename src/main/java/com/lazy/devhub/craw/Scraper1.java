package com.lazy.devhub.craw;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper1 {

    private static final String LINK_CRAWL = "https://www.vib.com.vn/vn/the-tin-dung";

    public static void main(String[] args) {
        List<Map<String, Object>> cardData = new ArrayList<>();

        try {
            // Connect to the webpage and fetch HTML
            Document doc = Jsoup.connect(LINK_CRAWL).get();

            Elements cardElements = doc.select("div.vib-v2-box-card-content");

            for (Element card : cardElements) {
                Map<String, Object> cardInfo = new HashMap<>();

                // Extract credit card name
                Element cardNameElement = card.select("h3[data-attr='header'] a").first();
                String cardName = cardNameElement != null ? cardNameElement.text() : "Không tìm thấy";
                cardInfo.put("Tên thẻ", cardName);

                // Extract featured attributes
                String features = extractText(card, "div.box-card-row2:contains(TÍNH NĂNG NỔI BẬT) p");
                cardInfo.put("Tính năng nổi bật", features);

                // Extract limits and fees
                String limitFees = extractText(card, "div.box-card-row2:contains(HẠN MỨC VÀ PHÍ) p");
                cardInfo.put("Hạn mức và phí", limitFees);

                // Extract detail link
                String detailLink = cardNameElement != null ? cardNameElement.attr("href") : "Không tìm thấy";

// Kiểm tra nếu detailLink không phải là "Không tìm thấy"
                if (!detailLink.equals("Không tìm thấy")) {
                    // Thay thế các chuỗi không cần thiết trong liên kết
                    detailLink = detailLink.replace("/product-landing/the-ngan-hang/the-tin-dung", "/the-tin-dung");
                    //.replace("/the-ngan-hang/the-tin-dung", "")
                    // .replace("/the-ngan-hang", "");
                }

// Lưu liên kết chi tiết vào cardInfo
                cardInfo.put("Link chi tiết", detailLink);

                // Fetch benefits details
                List<Map<String, String>> benefits = getDetail(detailLink);
                cardInfo.put("Benefits", benefits);

                // Add card info to list
                cardData.add(cardInfo);
            }

            // Convert the list to a JSONArray and write it to a JSON file
            JSONArray jsonArray = new JSONArray(cardData);
            try (FileWriter file = new FileWriter("card_data.json")) {
                file.write(jsonArray.toString(4));  // Indented with 4 spaces for readability
                System.out.println("Data has been written to card_data.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to extract text based on a selector
    private static String extractText(Element card, String selector) {
        Element element = card.select(selector).first();
        return element != null ? element.text() : "Không tìm thấy";
    }

    // Method to get detailed benefits
    public static List<Map<String, String>> getDetail(String detailLink) {
        List<Map<String, String>> benefitsList = new ArrayList<>();
        try {
            // Fetch the detail page
            Document doc = Jsoup.connect("https://www.vib.com.vn" + detailLink).get();
            System.out.println("Link: " + "https://www.vib.com.vn" + detailLink);

            // Select the benefits section
            Elements benefitElements = doc.select("div.vib-right-content-deatil");

            // Loop through each benefit card
            for (Element benefitElement : benefitElements.select(".vib-v2-box-benefit-card")) {
                Map<String, String> benefit = new HashMap<>();

                // Extract the title of the benefit
                String title = benefitElement.select("h4").first().text();
                benefit.put("Title", title);

                // Extract the description of the benefit
                String description = benefitElement.select("p").first().text();
                benefit.put("Description", description);

                // Extract the image URL if available
                String imageUrl = benefitElement.select(".img_benefit_card img").attr("src");
                benefit.put("Image URL", imageUrl);

                // Add benefit to the list
                benefitsList.add(benefit);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy thông tin chi tiết: " + e.getMessage());
        }
        return benefitsList;
    }
}
