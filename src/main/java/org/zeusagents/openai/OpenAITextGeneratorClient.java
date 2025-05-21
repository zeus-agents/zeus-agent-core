package org.zeusagents.openai;

public class OpenAITextGeneratorClient implements AIClient {

    @Override
    public String execute(String prompt) {
        return generateText(prompt);
    }

    public String generateText(String prompt){
        return "text generated for: " + prompt;
    }

}
