package com.yijiinfo.system.service;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class ImageService {
    String storePath = "D:\\图片压缩测试\\";

    /**
     * 指定大小进行缩放
     * 若图片横比200小，高比300小，不变
     * 若图片横比200小，高比300大，高缩小到300，图片比例不变
     * 若图片横比200大，高比300小，横缩小到200，图片比例不变
     * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
     */
    public void upload1(MultipartFile file) throws Exception {
        File dest = new File(storePath+ file.getOriginalFilename() + "_200x300.jpg");
        Thumbnails.of(file.getInputStream())
                .size(200, 300)
                .toFile(dest);
    }

    /**
     * 按照比例进行缩放
     * scale(比例)
     * */
    public void upload2(MultipartFile file) throws Exception {
        File dest = new File(storePath+ file.getOriginalFilename() + "_25%.jpg");
        Thumbnails.of(file.getInputStream())
                .scale(0.25f)
                .toFile(dest);
    }

    /**
     *  不按照比例，指定大小进行缩放
     *  keepAspectRatio(false) 默认是按照比例缩放的
     * */
    public void upload3(MultipartFile file) throws Exception {
        File dest = new File(storePath+ file.getOriginalFilename() + "_200x200.jpg");
        Thumbnails.of(file.getInputStream())
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile(dest);
    }

    /**
     *  输出图片到流对象
     *
     * */
    public void upload4(MultipartFile file) throws Exception {
        OutputStream os = new FileOutputStream(storePath+ file.getOriginalFilename() + "_OutputStream.png");
        Thumbnails.of(file.getInputStream())
                .size(1280, 1024)
                .toOutputStream(os);
    }

    /**
     *  输出图片到BufferedImage
     * */
    public void upload5(MultipartFile file) throws Exception {
        File dest = new File(storePath+ file.getOriginalFilename() + "_BufferedImage.jpg");
        BufferedImage thumbnail = Thumbnails.of(file.getInputStream())
                .size(1280, 1024)
                .asBufferedImage();
        ImageIO.write(thumbnail, "jpg", dest);
    }
}
