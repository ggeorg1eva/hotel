package com.example.bookhotel.web;

import com.example.bookhotel.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public String getStats(Model model){
        model.addAttribute("stats", statsService.getRequestPathsAsStrings());

        return "stats";
    }
}
