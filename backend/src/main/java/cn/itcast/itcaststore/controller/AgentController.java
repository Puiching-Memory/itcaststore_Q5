package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.dto.AgentRequest;
import cn.itcast.itcaststore.service.AgentService;
import cn.itcast.itcaststore.util.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能体相关接口：根据用户自然语言问题推荐图书
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * 用户输入自然语言问题，智能体返回推荐结果
     */
    @PostMapping("/recommend")
    public ResponseResult<String> recommend(@RequestBody AgentRequest request) {
        String question = request.getQuestion();
        String recommendation = agentService.getRecommendation(question);
        return ResponseResult.success(recommendation);
    }
}


