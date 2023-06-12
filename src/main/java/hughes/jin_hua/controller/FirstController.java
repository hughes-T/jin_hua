package hughes.jin_hua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/first")
@Controller
public class FirstController {


    @GetMapping("/demo")
    public String demo(){
        return "first";
    }



}
