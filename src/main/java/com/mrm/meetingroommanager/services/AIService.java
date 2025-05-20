package com.mrm.meetingroommanager.services;

import com.mrm.meetingroommanager.dto.ChatDTO;
import com.mrm.meetingroommanager.dto.groq.ChatRequest;
import com.mrm.meetingroommanager.dto.groq.ChatResponse;
import com.mrm.meetingroommanager.dto.groq.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIService {

    private final WebClient webClient;
    private final String model;

    public AIService(
            @Value("${ai.groq.api.url}") String apiUrl,
            @Value("${ai.groq.api.key}") String apiKey,
            @Value("${ai.groq.model}") String model) {

        // Debug info
        System.out.println("API URL: " + apiUrl);
        System.out.println("API Key: " + (apiKey != null ? apiKey.substring(0, 5) + "..." : "null"));
        System.out.println("Model: " + model);

        // Format API key for Groq API - removing any potential whitespace
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.out.println("WARNING: API key is null or empty!");
            apiKey = "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"; // Fallback to hardcoded key
        } else {
            apiKey = apiKey.trim();
        }

        final String finalApiKey = apiKey; // For use in lambda
        System.out.println("Using API key starting with: " + apiKey.substring(0, Math.min(5, apiKey.length())) + "...");

        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeaders(headers -> {
                    headers.add("Authorization", "Bearer " + finalApiKey);
                    headers.add("Content-Type", "application/json");
                })
                .build();
        this.model = model;
    }

    public ChatDTO processUserMessage(String userMessage) {
        try {
            // Create system message with instructions
            Message systemMessage = new Message("system",
                    "You are a helpful meeting room booking assistant. " +
                            "Extract the relevant information from user messages to help book meeting rooms. " +
                            "When a meeting room is requested, recommend a suitable room based on capacity, " +
                            "analyze the time request, and format your response in a specific way. " +
                            "Always respond in this JSON structure within your message: " +
                            "{\"room_id\": number, \"start_time\": \"YYYY-MM-DDTHH:MM\", \"end_time\": \"YYYY-MM-DDTHH:MM\", " +
                            "\"title\": \"Meeting title\", \"response\": \"Your friendly response\"}. " +
                            "If you cannot extract all required information, ask for clarification.");

            // Create user message
            Message userMsg = new Message("user", userMessage);

            // Create the chat request
            ChatRequest request = new ChatRequest();
            request.setModel(model);
            request.setMessages(List.of(systemMessage, userMsg));
            request.setTemperature(0.7);
            request.setMax_tokens(1000);

            // Log the request for debugging
            System.out.println("Sending request to Groq API with model: " + model);
            System.out.println("Request messages: " + request.getMessages().size() + " messages");

            // Make the API call with simpler approach
            String endpoint = "/chat/completions";
            System.out.println("Sending request to Groq API at: " + webClient.toString() + endpoint);

            ChatResponse response = webClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();

            if (response != null && !response.getChoices().isEmpty()) {
                String responseContent = response.getChoices().get(0).getMessage().getContent();
                return parseAIResponse(responseContent);
            }

            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setResponse("Sorry, I couldn't process your request at this time. Please try again later.");
            chatDTO.setMessage(userMessage);
            chatDTO.setShowBookButton(false);
            return chatDTO;

        } catch (Exception e) {
            // Handle any exceptions with more detailed error message
            e.printStackTrace(); // Print full stack trace for debugging

            String errorMessage = "Sorry, an error occurred: " + e.getMessage();

            // Add more specific error messages for common issues
            if (e.getMessage() != null && e.getMessage().contains("401 Unauthorized")) {
                errorMessage = "Authentication error with the Groq API. Please check your API key.";
            } else if (e.getMessage() != null && e.getMessage().contains("Connection refused")) {
                errorMessage = "Could not connect to the Groq API. Please check your internet connection.";
            }

            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setResponse(errorMessage);
            chatDTO.setMessage(userMessage);
            chatDTO.setShowBookButton(false);
            return chatDTO;
        }
    }

    private ChatDTO parseAIResponse(String aiResponse) {
        try {
            // Extract JSON from the response using regex
            Pattern pattern = Pattern.compile("\\{.*?\"response\".*?\\}");
            Matcher matcher = pattern.matcher(aiResponse);

            if (matcher.find()) {
                String jsonStr = matcher.group(0);

                // Extract individual fields with regex
                Long roomId = extractLongValue(jsonStr, "room_id");
                String startTimeStr = extractStringValue(jsonStr, "start_time");
                String endTimeStr = extractStringValue(jsonStr, "end_time");
                String title = extractStringValue(jsonStr, "title");
                String response = extractStringValue(jsonStr, "response");

                LocalDateTime startTime = null;
                LocalDateTime endTime = null;

                try {
                    // Parse date-time strings if they exist
                    if (startTimeStr != null) {
                        startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                    if (endTimeStr != null) {
                        endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                } catch (DateTimeParseException e) {
                    // Handle date parsing errors
                    ChatDTO chatDTO = new ChatDTO();
                    chatDTO.setResponse("I encountered an issue with date format. " + (response != null ? response : aiResponse));
                    chatDTO.setShowBookButton(false);
                    return chatDTO;
                }

                // Check if we have all needed booking information
                boolean showBookButton = roomId != null && startTime != null && endTime != null && title != null;

                ChatDTO chatDTO = new ChatDTO();
                chatDTO.setSuggestedRoomId(roomId);
                chatDTO.setSuggestedStartTime(startTime);
                chatDTO.setSuggestedEndTime(endTime);
                chatDTO.setSuggestedTitle(title);
                chatDTO.setResponse(response != null ? response : aiResponse);
                chatDTO.setShowBookButton(showBookButton);
                return chatDTO;
            }
        } catch (Exception e) {
            // Fallback in case of parsing errors
        }

        // Return the original response if parsing failed
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setResponse(aiResponse);
        chatDTO.setShowBookButton(false);
        return chatDTO;
    }

    private Long extractLongValue(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String extractStringValue(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
