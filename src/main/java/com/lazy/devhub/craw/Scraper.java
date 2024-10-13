package com.lazy.devhub.craw;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static final String LINK_CRAWL = "https://www.vib.com.vn/vn/the-tin-dung";

    public static void main(String[] args) {
        try {
            // Connect to the webpage and fetch HTML
            Document doc = Jsoup.connect(LINK_CRAWL).get();

            Elements cardElements = doc.select("div.vib-v2-box-card-content");

            for (Element card : cardElements) {
                // Extract credit card name
                Element cardNameElement = card.select("h3[data-attr='header'] a").first();
                String cardName = cardNameElement != null ? cardNameElement.text() : "Không tìm thấy";

                // Extract featured attributes
                String features = extractText(card, "div.box-card-row2:contains(TÍNH NĂNG NỔI BẬT) p");

                // Extract limits and fees
                String limitFees = extractText(card, "div.box-card-row2:contains(HẠN MỨC VÀ PHÍ) p");

                // Extract detail link
                String detailLink = cardNameElement != null ? cardNameElement.attr("href") : "Không tìm thấy";

                // Print results for each card
                System.out.println("Tên thẻ: " + cardName);
                System.out.println("Tính năng nổi bật: " + features);
                System.out.println("Hạn mức và phí: " + limitFees);
                System.out.println("Link chi tiết: " + detailLink);
                getDetail(detailLink);
                System.out.println("----------------------------------------");
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

    public static void getDetail(String detailLink) {
        try {
            // Fetch the detail page
            Document doc = Jsoup.connect("https://www.vib.com.vn" + detailLink).get();
            System.out.println("Link: " + "https://www.vib.com.vn" + detailLink);

          // Select the benefits section
          Elements benefitElements = doc.select("div.vib-right-content-deatil");

          // Loop through each benefit card
          for (Element benefitElement : benefitElements.select(".vib-v2-box-benefit-card")) {
              // Extract the title of the benefit
              String title = benefitElement.select("h4").first().text();
              
              // Extract the description of the benefit
              String description = benefitElement.select("p").first().text();
              
              // Extract the image URL if available
              String imageUrl = benefitElement.select(".img_benefit_card img").attr("src");

              // Print or save the benefit information
              System.out.println("Title: " + title);
              System.out.println("Description: " + description);
              System.out.println("Image URL: " + imageUrl);
              System.out.println("----------------------------------------");
          }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy thông tin chi tiết: " + e.getMessage());
        }
    }
    
}
