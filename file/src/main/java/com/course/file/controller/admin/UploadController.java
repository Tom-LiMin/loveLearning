package com.course.file.controller.admin;

import com.course.server.dto.FileDto;
import com.course.server.dto.ResponseDto;
import com.course.server.enums.FileUseEnum;
import com.course.server.service.FileService;
import com.course.server.util.Base64ToMultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

@RequestMapping("/admin")
@RestController
public class UploadController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    public static final String BUSINESS_NAME = "文件上传";

    @Resource
    private FileService fileService;

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Value("${file.path}")
    private String FILE_PATH;

    @RequestMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto) throws IOException {
        LOG.info("上传文件开始：");  // 因为只是个hash值，就不打印了
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);

        // 如果文件不存在则创建
        String dir = useEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH + dir);
        if (!fullDir.exists()) {
            fullDir.mkdir();
        }

        // String path = dir + File.separator + fileName + "-" + key + "." + suffix;
        String path = new StringBuffer(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .toString();
        String localPath = new StringBuffer(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .append(".")
                .append(fileDto.getShardIndex()).toString();
        String fullPath = FILE_PATH + localPath;
        File dest = new File(fullPath);
        shard.transferTo(dest);
        LOG.info(dest.getAbsolutePath());

        LOG.info("保存文件记录开始");
        fileDto.setPath(path);
        fileService.save(fileDto);

        if (fileDto.getShardIndex() == fileDto.getShardTotal()) {
            this.merge(fileDto);
        }

        ResponseDto responseDto = new ResponseDto<>();
//        responseDto.setContent(new StringBuilder(FILE_DOMAIN).append(path));   //"http://127.0.0.1:9003/file/f/teacher/"+key+"-"+fileName
        fileDto.setPath(FILE_DOMAIN + path);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @GetMapping("/merge")
    public void merge(FileDto fileDto) throws FileNotFoundException {
        LOG.info("合并分片开始");
        String path = fileDto.getPath();  // 注意这个取得是全路径
        path.replace(FILE_DOMAIN,""); // 要获取的是相对路径
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(FILE_PATH + path);
        // 新建 I/O 流
        FileOutputStream outputStream = new FileOutputStream(newFile,true);  // 选择追加写入的方式
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[10 * 1024 * 1024];
        int len;

        try {
            for (int i = 0; i < shardTotal; i++) {
                //读取分片
                fileInputStream = new FileInputStream(new File(new StringBuffer(FILE_PATH).append(path).append(".").append((i+1)).toString()));
                while ((len = fileInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes,0,len);
                }
            }
            //读取第一个分片

            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                LOG.info("IO流关闭");
            } catch (Exception e) {
                LOG.error("IO流关闭", e);
            }

        } catch (IOException e) {
            LOG.error("分片合并异常",e);
        }

        LOG.info("合并分片结束");
    }
}
