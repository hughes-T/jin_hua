package hughes.jin_hua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@RequestMapping("/first")
@Controller
public class FirstController {


    @GetMapping("/demo")
    public String demo(){
        return "first";
    }

    @ResponseBody
    @GetMapping("/demo2")
    public String demo2(){
        return "other";
    }

}
