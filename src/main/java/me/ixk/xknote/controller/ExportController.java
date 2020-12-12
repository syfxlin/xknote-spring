package me.ixk.xknote.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import java.io.File;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.Storage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导出控制器
 *
 * @author Otstar Lin
 * @date 2020/12/12 下午 4:35
 */
@RestController
@RequestMapping("/api/export")
@PreAuthorize("isAuthenticated()")
public class ExportController {

    @GetMapping
    public ResponseEntity<Object> export(
        @RequestParam(name = "path", defaultValue = "/") String path
    ) {
        Long id = Application.getCurrentUserId();
        String zipName = "uid_" + id + ".zip";
        File zip = ZipUtil.zip(
            Storage.link("/uid_" + id + path),
            FileUtil.getTmpDirPath() + File.separator + zipName
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "Content-Disposition",
            String.format("attachment;filename=\"%s", zipName)
        );
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(zip.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new FileSystemResource(zip));
    }

    @GetMapping("/images")
    public ResponseEntity<Object> exportImages() {
        Long id = Application.getCurrentUserId();
        String zipName = "uid_" + id + "_img.zip";
        File zip = ZipUtil.zip(
            Storage.link("/image/uid_" + id),
            FileUtil.getTmpDirPath() + File.separator + zipName
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "Content-Disposition",
            String.format("attachment;filename=\"%s", zipName)
        );
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(zip.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new FileSystemResource(zip));
    }
}
