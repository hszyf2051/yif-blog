package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.utils.QiniuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public ResultVo Upload(@RequestParam("image") MultipartFile file) {
        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." +
                StringUtils.substringAfterLast(originalFilename,".");
        // 上传文件 七牛云服务器 把图片发到服务器上
        // 降低自身服务器的带宽消耗

        boolean upload = qiniuUtils.upload(file,fileName);
        if (upload) {
            return ResultVo.success(QiniuUtils.url + "/" + fileName);
        }
        return ResultVo.fail(20001,"上传失败");
    }
}
