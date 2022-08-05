package com.example.bookhotel.web;
import com.example.bookhotel.service.OperatorService;
import com.example.bookhotel.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final OperatorService operatorService;

    public AdminController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("/operators")
    public String getAllOperators(Model model) {
        model.addAttribute("operatorsList", operatorService.getAllOperators());

        return "view-operators";
    }

}
