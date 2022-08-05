package com.example.bookhotel.web;

import com.example.bookhotel.service.OperatorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operators")
public class OperatorController {
    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }


    @PatchMapping("/remove-role/{id}")
    public String removeOperatorRole(@PathVariable Long id){
        operatorService.removeOperatorRole(id);

        return "redirect:/admin/operators";
    }

}
