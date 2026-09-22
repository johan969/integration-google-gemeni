package se.iths.johan.integrationgooglegemeni.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.iths.johan.integrationgooglegemeni.model.Entry;
import se.iths.johan.integrationgooglegemeni.repository.EntryRepository;
import se.iths.johan.integrationgooglegemeni.service.GeminiService;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final EntryRepository entryRepository;

    private final GeminiService geminiService;

    public HomeController(EntryRepository entryRepository, GeminiService geminiService) {
        this.entryRepository = entryRepository;
        this.geminiService = geminiService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question, Model model) {
        String answer = geminiService.getGeminiResponse(question);
        boolean error = answer.contains("high demand") || answer.contains("something went wrong")|| answer.contains("Too Many Requests");

        if(!error && answer.length()<=1000) {
            Entry entry = new Entry();
            entry.setAnswer(answer);
            entry.setQuestion(question);
            entryRepository.save(entry);
        }else if (answer.length()>1000){
            model.addAttribute("warning","Answer is too large to store in database");
        }


        model.addAttribute("question", question);
        model.addAttribute("answer", answer);

        return "home";
    }

    @GetMapping
    public String home() {
        return "home";
    }



    @GetMapping("/history")
    public String history(Model model) {
        List<Entry> entries = entryRepository.findAll();
        model.addAttribute("chatHistory", entries);
        return "history";
    }

}
