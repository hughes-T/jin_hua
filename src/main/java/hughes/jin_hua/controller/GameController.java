package hughes.jin_hua.controller;

import hughes.jin_hua.pojo.ApiResult;
import hughes.jin_hua.service.GameEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RequestMapping("/game")
@Controller
public class GameController extends GlobalExceptionHandler  {

    @Autowired
    private GameEngine gameEngine;
    /**
     * 玩家获取游戏页实时信息
     */
    @ResponseBody
    @PostMapping("/getInfo")
    public ApiResult getInfo(@RequestBody Map<String, String> param){
        return gameEngine.getGamePageInfo(param);
    }

}
