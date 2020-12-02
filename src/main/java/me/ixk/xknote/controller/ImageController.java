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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    ConfigServiceImpl configService;

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(
        @PathVariable("imageId") String imageId
    ) {
        byte[] imageContent = Storage.getFile(getPath("/" + imageId));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<File> files = Storage.allFiles(getPath(""));
        List<String> list = files
            .stream()
            .map(f -> "/api/images/" + f.getName())
            .collect(Collectors.toList());
        return ResponseInfo.stdJson("images", list);
    }

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
