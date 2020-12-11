package me.ixk.xknote.controller;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.ConfigServiceImpl;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.Json;
import me.ixk.xknote.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片控制器
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:03
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    ConfigServiceImpl configService;

    /**
     * 获取图片
     *
     * @param imageId 图片 ID
     *
     * @return 图片字节流
     */
    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(
        @PathVariable("imageId") String imageId
    ) {
        byte[] imageContent = Storage.getFile(getPath("/" + imageId));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    /**
     * 获取所有图片信息
     *
     * @return 图片信息
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<File> files = Storage.allFiles(getPath(""));
        List<String> list = files
            .stream()
            .map(f -> "/api/images/" + f.getName())
            .collect(Collectors.toList());
        return ResponseInfo.stdJson("images", list);
    }

    /**
     * 图片上传
     *
     * @param file 文件
     *
     * @return 图片信息
     * @throws IOException IO 异常
     */
    @PostMapping("")
    public ObjectNode upload(@RequestParam(name = "file") MultipartFile file)
        throws IOException {
        ObjectNode result = Json.createObject();
        String path = "/" + UUID.fastUUID().toString();
        Storage.putFile(file.getBytes(), getPath(path));
        result.put("error", true);
        result.put("path", "/api/images" + path);
        return result;
    }

    /**
     * 删除图片
     *
     * @param name 路径
     *
     * @return 是否删除成功
     */
    @DeleteMapping("")
    public ResponseEntity<Object> delete(
        @RequestParam(name = "name") String name
    ) {
        Storage.delete(getPath(name));
        return ResponseInfo.stdJson();
    }

    private String getPath(String path) {
        Long id = Application.getCurrentUserId();
        return "/image/uid_" + id + path;
    }
}
