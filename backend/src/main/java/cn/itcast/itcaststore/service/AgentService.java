package cn.itcast.itcaststore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用 Langflow 智能体服务，获取图书推荐结果
 */
@Service
public class AgentService {

    @Value("${langflow.base-url}")
    private String langflowBaseUrl;

    @Value("${langflow.flow-id}")
    private String langflowFlowId;

    @Value("${langflow.api-key:}")
    private String langflowApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 调用 Langflow，根据用户问题获取推荐结果
     *
     * @param question 用户自然语言问题
     * @return Langflow 返回的推荐文本（具体格式可根据你的 Flow 输出调整）
     */
    public String getRecommendation(String question) {
        // 这里使用 Langflow 标准 REST 运行接口路由，请根据你实际的 Flow API 路径调整
        String url = langflowBaseUrl + "/api/v1/run/" + langflowFlowId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (langflowApiKey != null && !langflowApiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + langflowApiKey);
        }

        // 约定 Langflow Flow 的输入为 { "question": "..." }
        Map<String, Object> input = new HashMap<>();
        input.put("question", question);

        Map<String, Object> body = new HashMap<>();
        body.put("input", input);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        Map<String, Object> result = response.getBody();

        if (result == null) {
            return "暂时无法获取推荐结果，请稍后再试。";
        }

        // 根据你在 Langflow 中的 Flow 输出结构，提取推荐文本
        // 这里假设最终返回中包含一个 "result" 字段是推荐结果文本
        Object recommendation = result.get("result");
        return recommendation != null ? recommendation.toString() : result.toString();
    }
}


