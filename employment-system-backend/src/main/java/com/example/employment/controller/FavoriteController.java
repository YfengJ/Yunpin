package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Favorite;
import com.example.employment.entity.Job; // 引入 Job
import com.example.employment.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites") // 确保有 /api
public class FavoriteController {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @PostMapping("/toggle")
    public Result toggle(@RequestBody Favorite favorite) {
        if (favorite.getUserId() == null || favorite.getJobId() == null) {
            return Result.error("参数错误");
        }
        int count = favoriteMapper.countByUserAndJob(favorite.getUserId(), favorite.getJobId());
        if (count > 0) {
            favoriteMapper.deleteByUserAndJob(favorite.getUserId(), favorite.getJobId());
            return Result.success("取消收藏");
        } else {
            favoriteMapper.insert(favorite);
            return Result.success("收藏成功");
        }
    }

    @GetMapping("/my-ids")
    public Result<List<Long>> getMyFavoriteIds(@RequestParam Long userId) {
        List<Long> ids = favoriteMapper.findJobIdsByUserId(userId);
        return Result.success(ids);
    }

    // 🔥 新增：获取我的收藏列表 (详细版)
    @GetMapping("/list")
    public Result<List<Job>> getMyFavoriteJobs(@RequestParam Long userId) {
        List<Job> jobs = favoriteMapper.findFavoriteJobsByUserId(userId);
        return Result.success(jobs);
    }
}