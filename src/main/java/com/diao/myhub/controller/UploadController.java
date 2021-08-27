package com.diao.myhub.controller;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.dto.ImageDTO;
import com.diao.myhub.provider.TencentCloudProvider;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class UploadController {

    @Autowired
    private TencentCloudProvider cloudProvider;
    @Autowired
    private COSClient cosClient;
    @RequestMapping("/imgUpload")
    @ResponseBody
    public String imgUpload(@RequestParam("editormd-image-file") MultipartFile file){
        String type = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        String key= null;
        try {
            InputStream inputStream = file.getInputStream();
            key = cloudProvider.uploadFile(inputStream,type,originalFilename,cosClient);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageDTO imageDTO = new ImageDTO();

        imageDTO.setUrl(cloudProvider.getUrl(cosClient,key));
        imageDTO.setMessage("cc");
        imageDTO.setSuccess(1);
        cosClient.shutdown();
        return JSON.toJSONString(imageDTO);
    }
}
