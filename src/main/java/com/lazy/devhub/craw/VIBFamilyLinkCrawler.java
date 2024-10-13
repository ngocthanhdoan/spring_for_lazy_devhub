package com.lazy.devhub.craw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VIBFamilyLinkCrawler {
    public static void main(String[] args) {
        try { 
            // URL of the VIB Family Link page
            String url = "https://www.vib.com.vn/wps/wcm/connect/vib20-personal-vn/credit-cards/detail/vib-rewards-unlimited/vib-rewards-detail/benefits?source=library&srv=cmpnt&cmpntid=c0d94b76-e22e-4057-adbb-be77d8919fb9";
            // Connect to the URL and parse the document
            Document doc = Jsoup.connect(url).get();
            
            
            Elements features = doc.select(".vib-v2-box-txt-card-detail h4:contains(TÍNH NĂNG NỔI BẬT) + p");
            System.out.println("Tính năng nổi bật:");
            for (Element feature : features) {
                System.out.println("- " + feature.text());
            }

            // Extract limits and fees
            Elements limitsAndFees = doc.select(".vib-v2-box-txt-card-detail h4:contains(HẠN MỨC VÀ PHÍ) + p");
            System.out.println("Hạn mức và phí:");
            for (Element limitOrFee : limitsAndFees) {
                System.out.println("- " + limitOrFee.text());
            }

            // Extract card benefits
            Elements benefitElements = doc.select(".vib-v2-box-benefit-card");
            if (benefitElements.isEmpty()) {
                System.out.println("Không tìm thấy thông tin lợi ích thẻ.");
            } else {
                System.out.println("Lợi ích thẻ:");
                for (Element benefitElement : benefitElements) {
                    // Extract benefit title
                    String benefitTitle = benefitElement.select("h4").text();
                    // Extract benefit description
                    String benefitDescription = benefitElement.select("p").text();

                    System.out.println("- " + benefitTitle + ": " + benefitDescription);

                    // Extract links if available
                    Elements links = benefitElement.select("a[href]");
                    for (Element link : links) {
                        String linkText = link.text();
                        String linkHref = link.attr("href");
                        System.out.println("  * " + linkText + ": " + linkHref);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

