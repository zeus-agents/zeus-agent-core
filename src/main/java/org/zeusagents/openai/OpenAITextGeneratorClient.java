package org.zeusagents.openai;

public class OpenAITextGeneratorClient implements OpenAIClient{

    @Override
    public String execute() {
        return generateText();
    }

    public String generateText(){
        return "text generated";
    }

}
