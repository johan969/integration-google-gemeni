package se.iths.johan.integrationgooglegemeni.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final String geminiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    @Value ("${GOOGLE_API_KEY}")
    private String apiKey;


    private final RestClient restClient;


    //behövde ändra konstruktorn till detta istället för den förgjorda, denna skapar Restklient själv istället för att injiceras
    public GeminiService() {
        this.restClient = RestClient.create();
    }


    public String getGeminiResponse(String question) {

        String finalUrl = geminiURL + "?key=" + apiKey;

        var body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", question)))));



        try {
            //detta hämtar hella JSON objected med all data
            String rawJson = restClient.post()
                    .uri(finalUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()

                    .body(String.class);


            //nu ska vi få fram bara texten/svaret i json objectet, vi behöver då navigera till där texten är.
            //candidates -> content -> parts -> text

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(rawJson);

            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        }catch (HttpClientErrorException.TooManyRequests e){
            return "Too Many Requests, You exceeded your current quota";

        }catch (HttpServerErrorException.ServiceUnavailable e){
            return "This model is currently experiencing high demand. Spikes in demand are usually temporary. Please try again later.";
        }catch(Exception e){
            e.printStackTrace();
            return "something went wrong";
        }


    }

}
