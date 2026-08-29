package com.taskmanagement.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final OpenAIClient client;


    public AiService() {

        this.client = OpenAIOkHttpClient.fromEnv();
    }


    public String generateTaskDescription(
            String title,
            String additionalDetails) {

        String prompt = """
            Generate a clear and professional task description
            for a software development task.

            Task Title:
            %s

            Additional Details:
            %s

            Return only the task description.
            Keep it concise and useful.
            """
                .formatted(
                        title,
                        additionalDetails
                );


        ResponseCreateParams params =
                ResponseCreateParams.builder()
                        .model("gpt-4o")
                        .input(prompt)
                        .build();


        Response response =
                client.responses()
                        .create(params);


        return response
                .output()
                .stream()
                .flatMap(item ->
                        item.message().stream()
                )
                .flatMap(message ->
                        message.content().stream()
                )
                .flatMap(content ->
                        content.outputText().stream()
                )
                .map(outputText ->
                        outputText.text()
                )
                .findFirst()
                .orElse(
                        "Unable to generate task description."
                );
    }

}
