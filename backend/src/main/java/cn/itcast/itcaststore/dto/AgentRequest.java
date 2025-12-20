package cn.itcast.itcaststore.dto;

import lombok.Data;

/**
 * 用户向智能体提问的请求体
 */
@Data
public class AgentRequest {

    /**
     * 用户自然语言问题，例如“我想找一本学习Java Web的书”
     */
    private String question;
}


