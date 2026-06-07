package com.example.doanlaptrinhjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private final String model = "gemini-3-flash-preview";

    public String generateReply(String userMessage, String shopName, String menuData) {

        // ===== SYSTEM PROMPT CHẶT CHẼ =====
        String systemInstruction =
                "Bạn là chatbot tư vấn bán hàng của cửa hàng \"" + shopName + "\".\n\n" +

                        "MENU THỰC TẾ (chỉ dùng thông tin này, không tự bịa):\n" +
                        menuData + "\n\n" +

                        "QUY TẮC BẮT BUỘC:\n" +
                        "1. Chỉ trả lời về: menu, giá, sản phẩm, gợi ý đặt hàng, khuyến mãi của shop.\n" +
                        "2. Nếu câu hỏi KHÔNG liên quan đến shop (ví dụ: thời sự, lập trình, toán học, v.v.) → trả lời: \"Mình chỉ hỗ trợ tư vấn về sản phẩm và dịch vụ của " + shopName + " thôi bạn nhé!\"\n" +
                        "3. Nếu không có thông tin trong menu → nói thật, không bịa.\n" +
                        "4. Trả lời NGẮN GỌN, tối đa 3 câu. Không dài dòng.\n" +
                        "5. Giọng thân thiện, tự nhiên, dùng \"mình/bạn\".\n" +
                        "6. Không dùng markdown (**, *, #). Viết văn thuần.\n";

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Cấu trúc request với systemInstruction riêng biệt
        Map<String, Object> body = new HashMap<>();

        // System instruction (Gemini hỗ trợ field này từ v1beta)
        body.put("system_instruction", Map.of(
                "parts", List.of(Map.of("text", systemInstruction))
        ));

        // Tin nhắn user
        body.put("contents", List.of(
                Map.of("role", "user", "parts", List.of(Map.of("text", userMessage)))
        ));

        // Cấu hình generation: nhiệt độ thấp = bám sát sự thật hơn
        body.put("generationConfig", Map.of(
                "temperature", 0.3,
                "maxOutputTokens", 1000,
                "topP", 0.8
        ));

        // Safety settings: tắt bộ lọc không cần thiết cho chatbot bán hàng
        body.put("safetySettings", List.of(
                Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_ONLY_HIGH"),
                Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_ONLY_HIGH")
        ));

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<?> candidates = (List<?>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<?, ?> first = (Map<?, ?>) candidates.get(0);
                    Map<?, ?> content = (Map<?, ?>) first.get("content");
                    List<?> parts = (List<?>) content.get("parts");
                    Map<?, ?> part = (Map<?, ?>) parts.get(0);
                    return ((String) part.get("text")).trim();
                }
            }
            return "Xin lỗi, mình chưa tìm được câu trả lời phù hợp. Bạn thử hỏi lại nhé!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Shop đang bận xử lý, bạn vui lòng thử lại sau ít phút nhé!";
        }
    }
}