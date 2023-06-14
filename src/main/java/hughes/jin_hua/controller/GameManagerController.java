package hughes.jin_hua.controller;

import hughes.jin_hua.service.GameEngine;
import hughes.jin_hua.service.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/manager")
@Controller
public class GameManagerController extends GlobalExceptionHandler {

    @Autowired
    private GameEngine engine;

    @Autowired
    private PlayerManager playerManager;

    @ResponseBody
    @GetMapping("/changeGameStatus")
    public Boolean changeGameStatus(@RequestParam("status") String status){
        return engine.changeGameStatus(status);
    }

    /**
     * 玩家注册
     */


}
