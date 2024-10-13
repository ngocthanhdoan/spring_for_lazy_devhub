package com.lazy.devhub.craw;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupSelectorTool {
    public static void main(String[] args) {
        String url = "https://www.vib.com.vn/vn/the-tin-dung/vib-premier-boundless"; // Thay thế bằng link cần crawl
        String targetClass = "vib-v2-box-benefit-card"; // Class cần tìm

        try {
            // Crawl HTML từ URL
            Document doc = Jsoup.connect(url).get();

            // Tìm kiếm tất cả các element có class cụ thể
            Elements elements = doc.getElementsByClass(targetClass);

            for (Element element : elements) {
                // Tạo selector cho phần tử
                String selector = buildSelector(element);
                System.out.println("Found element with selector: " + selector);
                System.out.println("Element HTML: " + element.outerHtml());
            }

        } catch (IOException e) {
            System.out.println("Lỗi khi lấy nội dung HTML: " + e.getMessage());
        }
    }

    private static String buildSelector(Element element) {
        StringBuilder selector = new StringBuilder();

        while (element != null) {
            String tag = element.tagName();
            String id = element.id();
            String classNames = element.className();

            // Xây dựng selector với ID nếu có
            if (!id.isEmpty()) {
                selector.insert(0, "#" + id);
                break; // Nếu đã có ID, không cần tìm các lớp
            }

            // Xây dựng selector với class nếu có
            if (!classNames.isEmpty()) {
                selector.insert(0, "." + classNames.replace(" ", "."));
            }

            // Thêm tag name
            selector.insert(0, tag);

            // Tiến lên cây DOM
            element = element.parent();
            if (element != null) {
                selector.insert(0, " > "); // Thêm dấu phân cách giữa các phần tử
            }
        }

        return selector.toString();
    }
}
