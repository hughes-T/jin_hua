package hughes.jin_hua.controller;

import hughes.jin_hua.pojo.ApiResult;
import hughes.jin_hua.service.GameEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RequestMapping("/manager")
@Controller
public class GameManagerController extends GlobalExceptionHandler {

    @Autowired
    private GameEngine engine;

    @ResponseBody
    @GetMapping("/changeGameStatus")
    public Boolean changeGameStatus(@RequestParam("status") String status){
        return engine.changeGameStatus(status);
    }

    /**
     * 查询游戏状态
     */
    @ResponseBody
    @GetMapping("/queryGameInfo")
    public ApiResult queryGameInfo(){
        return engine.queryGameInfo();
    }

    /**
     * 玩家注册
     */
    @ResponseBody
    @PostMapping("/player/register")
    public ApiResult playerRegister(@RequestBody Map<String, String> param){
        return engine.playerRegister(param);
    }

    /**
     * 玩家登录
     */
    @ResponseBody
    @PostMapping("/player/login")
    public ApiResult playerLogin(@RequestBody Map<String, String> param){
        return engine.playerLogin(param);
    }

}
