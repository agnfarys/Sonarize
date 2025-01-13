package com.sonarize.sonarize_backend.service;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.sonarize.sonarize_backend.model.Survey;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ChatGPTService {

    private final String apiKey = System.getenv("OPENAI_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    public ChatGPTService() {
        if (apiKey == null) {
            throw new IllegalStateException("Missing API key: please set the OPENAI_API_KEY environment variable.");
        }
    }

    public List<String> generateRecommendations(Survey survey) {
        String prompt = buildPrompt(survey);
        Map<String, Object> requestBody = createRequestBody(prompt, survey.getPlaylistLength());


        String rawResponse = sendChatGPTRequest(requestBody);

        // Parse the response to get only song titles
        return parseRecommendations(rawResponse);
    }

    private List<String> parseRecommendations(String response) {
        // Parse the JSON response to extract the message content
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        String content = choices.get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();

        // Split the response content into lines and remove numbering
        return Arrays.stream(content.split("\n"))
                .map(line -> line.replaceAll("^\\d+\\. ", "").trim()) // Remove numbering like "1. ", "2. ", etc.
                .collect(Collectors.toList());
    }

    public List<String> generateDefaultRecommendations() {
        return List.of(
                "Imagine - John Lennon",
                "Bohemian Rhapsody - Queen",
                "Hotel California - Eagles",
                "Shape of You - Ed Sheeran",
                "Billie Jean - Michael Jackson"
        );
    }


    private String buildPrompt(Survey survey) {
        return String.format(
                "Create a playlist with %s unique songs for a user with the following preferences:\n" +
                        "- Mood: %s\n" +
                        "- Genres: %s\n" +
                        "- Energy Level: %s (on a scale of 1-100)\n" +
                        "- Occasion: %s\n" +
                        "- Favorite Artists: %s\n" +
                        "- Discovery Preference: %s (e.g., new releases, top hits)\n" +
                        "- Language Preference: %s\n\n" +
                        "Instructions:\n" +
                        "1. Your response must ONLY contain the list of songs in the exact format provided below.\n" +
                        "2. Do not include any explanations, introductions, or additional text.\n" +
                        "3. Each song must be unique and strictly follow the specified genres. If the genre strongly differs, ignore favorite artists.\n" +
                        "4. After generating the list, ensure that each song in the playlist is unique. Remove any duplicates.\n" +
                        "5. If removing duplicates results in fewer songs than required, add additional unique songs to meet the required playlist length.\n\n" +
                        "Format:\n" +
                        "1. Song Title\n2. Song Title\n3. Song Title\n...\n\n" +
                        "Example:\n" +
                        "1. Song Title 1\n2. Song Title 2\n3. Song Title 3\n...",
                survey.getPlaylistLength(),
                survey.getMood(),
                String.join(", ", survey.getGenres()),
                survey.getEnergyLevel(),
                survey.getOccasion(),
                String.join(", ", survey.getFavoriteArtists()),
                survey.getDiscoveryPreference(),
                survey.getLanguagePreference()
        );
    }


    private Map<String, Object> createRequestBody(String prompt, String number_of_music) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-2024-11-20");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 50 * Integer.parseInt(number_of_music)); // adjust tokens based on list length
        requestBody.put("temperature", 0.5); // lower temperature for more focused responses
        return requestBody;
    }


    private String sendChatGPTRequest(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
