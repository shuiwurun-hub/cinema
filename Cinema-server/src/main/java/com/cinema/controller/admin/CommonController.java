package com.cinema.controller.admin;

import com.cinema.constant.MessageConstant;
import com.cinema.result.Result;
import com.cinema.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.UUID;

/**
 * 文件上传
 */

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     *
     *
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);
        try {
            //原始文件�?
            String originalFilename = file.getOriginalFilename();
            //截取原始文件后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构建新文件名�?
            String objectname = UUID.randomUUID().toString() + extension;
//文件的请求路�?
            String filePath = aliOssUtil.upload(file.getBytes(),objectname);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败: {}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);

    }


}
