package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.domain.User;
import cn.itcast.itcaststore.repository.UserRepository;
import cn.itcast.itcaststore.security.JwtTokenProvider;
import cn.itcast.itcaststore.dto.LoginRequest;
import cn.itcast.itcaststore.dto.RegisterRequest;
import cn.itcast.itcaststore.dto.AuthResponse;
import cn.itcast.itcaststore.util.ResponseResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseResult<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseResult.error("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // 直接存储明文密码
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setTelephone(request.getTelephone());
        user.setActiveCode(UUID.randomUUID().toString());
        user.setState(1); // 直接激活，简化流程
        user.setRole("普通用户");
        
        user = userRepository.save(user);
        
        String token = tokenProvider.generateToken(user.getUsername(), user.getId(), user.getRole());
        return ResponseResult.success(new AuthResponse(token, user));
    }

    @PostMapping("/login")
    public ResponseResult<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        // 直接比较明文密码
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (user.getState() == 0) {
            return ResponseResult.error("账户未激活，请先激活账户");
        }
        
        String token = tokenProvider.generateToken(user.getUsername(), user.getId(), user.getRole());
        return ResponseResult.success(new AuthResponse(token, user));
    }
}
