package com.Lynn.core.controller;

import com.Lynn.common.web.Constants;
import com.Lynn.core.service.product.UploadService;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by FantasmYii on 2018/3/22.
 */
@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /*
    * 出现bug 参数注释掉，方法里的内容注释掉，可以走到debug处，可能出先的问题，参数不匹配
    * 上传图片
    * */
    @RequestMapping(value = "/upload/uploadPic.do")
    public void uploadPic(@RequestParam(required = false) MultipartFile pic
            , HttpServletResponse response) throws IOException {
        System.out.println("进入到uploadservice中");
        System.out.println(pic.getOriginalFilename());
        String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());

        String url = Constants.IMG_URL + path;

        JSONObject jo = new JSONObject();
        jo.put("url", url);
        //将数据返回到页面，.write中写json格式的字符串，但避免手拼，所以new JSONObject对象
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jo.toString());

    }
    //上传多张图片
    @RequestMapping(value = "/upload/uploadPics.do")
    public @ResponseBody List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics
     ,HttpServletResponse response) throws IOException {
        List<String> urls=new ArrayList<String>();
        for (MultipartFile pic:pics){
            String path=uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize());
            String url=Constants.IMG_URL+path;
            urls.add(url);
        }
        return urls;
    }
    //使用富文本编译器的上传图片
    //注意参数，无敌版接收，使用spring强转MultipartFile
    //将要传递的参数放到request域中，强转。因为前台没有pic
    @RequestMapping(value = "/upload/uploadFck.do")
    public @ResponseBody void uploadFck(HttpServletRequest request
            ,HttpServletResponse response) throws IOException {
        //强转，接收，遍历
        MultipartRequest mr=(MultipartRequest) request;
        Map<String,MultipartFile> fileMap=mr.getFileMap();
        Set<Map.Entry<String,MultipartFile>> entrySet=fileMap.entrySet();
        for(Map.Entry<String,MultipartFile> entry :entrySet){
            MultipartFile pic=entry.getValue();
            String path=uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize());
            String url=Constants.IMG_URL+path;
            JSONObject jo=new JSONObject();
            //0代表成功，key值是固定的，例如error，官方文档可查
            jo.put("error",0);
            jo.put("url",url);
            //将数据返回到页面，.write中写json格式的字符串，但避免手拼，所以new JSONObject对象
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jo.toString());
        }

    }
}