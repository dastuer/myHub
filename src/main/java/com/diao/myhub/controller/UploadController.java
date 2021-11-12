package com.diao.myhub.controller;

import com.alibaba.fastjson.JSON;
import com.diao.myhub.dto.ImageDTO;
import com.diao.myhub.provider.TencentCloudProvider;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@PropertySource(value = "classpath:private.properties")
public class UploadController {

    @Autowired
    private TencentCloudProvider cloudProvider;
    @Autowired
    private COSClient cosClient;
    @Value("${spring.profiles.active}")
    private String env;
    // 上传文件传输到本地的路径

    @Value("${upload.filepath}")
    private String filepath;

    @RequestMapping("/imgUpload")
    @ResponseBody
    public String imgUpload(@RequestParam("editormd-image-file") MultipartFile file,HttpServletRequest req){

        String url = "";
        if ("test".equals(env)||"prodL1".equals(env)){
            url = uploadToServer(file,req);
        }else {
            url = uploadToCloud(file);
        }
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl(url);
        imageDTO.setMessage("ok");
        imageDTO.setSuccess(1);
        return JSON.toJSONString(imageDTO);
    }
    private String uploadToCloud( MultipartFile file){
        String type = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        String key= null;
        try {
            InputStream inputStream = file.getInputStream();
            key = cloudProvider.uploadFile(inputStream,type,size,originalFilename,cosClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cloudProvider.getUrl(cosClient,key);

    }
    private String uploadToServer(MultipartFile file, HttpServletRequest req){
        String path = "";
        // 处理上传文件路径
        if ("prodL1".equals(env)){
            path = filepath;
        }
        else {
            path = req.getSession().getServletContext().getRealPath("/uploadFiles/");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String realPath=path+date+"/";
        File dir = new File(realPath);
        if (!dir.exists()){
            if (!dir.mkdirs()){
                return null;
            }
        }
        // 处理上传文件文件名
        String filename = file.getOriginalFilename();
        if (filename==null){
            return "null";
        }
        String fileExt = "" ;
        if (filename.lastIndexOf('.')!=-1){
            fileExt= filename.substring(filename.lastIndexOf('.'));
        }
        String realName = UUID.randomUUID().toString().replace("-","")+fileExt;
        try {
            file.transferTo(new File(realPath+realName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 文件访问url,对与prodL1环境，这个文件被映射到H盘jar包所在路径下的imgs文件夹
        return "/uploadFiles/"+date+"/"+realName;
    }
}
