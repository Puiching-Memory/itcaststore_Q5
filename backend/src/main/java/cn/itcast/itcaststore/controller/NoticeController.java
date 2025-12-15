package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.domain.Notice;
import cn.itcast.itcaststore.repository.NoticeRepository;
import cn.itcast.itcaststore.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping
    public ResponseResult<List<Notice>> getNotices() {
        List<Notice> notices = noticeRepository.findAllByOrderByIdDesc();
        return ResponseResult.success(notices);
    }

    @GetMapping("/{id}")
    public ResponseResult<Notice> getNotice(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("公告不存在"));
        return ResponseResult.success(notice);
    }
}
