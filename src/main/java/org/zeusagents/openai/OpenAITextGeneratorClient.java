package org.zeusagents.openai;

public class OpenAITextGeneratorClient implements AIClient {

    @Override
    public String execute() {
        return generateText();
    }

    public String generateText(){
        return "text generated";
    }

}
