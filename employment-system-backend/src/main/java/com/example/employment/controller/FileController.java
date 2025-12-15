package com.example.employment.controller;

import com.example.employment.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    // 读取配置：D:/employment_system_files/
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        try {
            if (file.isEmpty()) return Result.error("文件为空");

            // 1. 检查文件夹是否存在，不存在就创建
            File dir = new File(fileUploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2. 生成新文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String newFileName = UUID.randomUUID().toString() + suffix;

            // 3. 保存文件到 D 盘
            file.transferTo(new File(fileUploadPath + newFileName));

            // 4. 返回访问链接 (固定使用 8080 端口)
            String url = "http://localhost:8080/files/" + newFileName;

            System.out.println("✅ 图片上传成功: " + url);
            return Result.success(url);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}