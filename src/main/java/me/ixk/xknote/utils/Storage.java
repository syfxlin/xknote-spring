package me.ixk.xknote.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.ixk.xknote.config.XkNoteConfig;
import me.ixk.xknote.exception.IllegalOperationDirectoryException;

public class Storage {

    public static String link(final String path) {
        String storagePath = FileUtil.normalize(
            Application.getBean(XkNoteConfig.class).getStoragePath()
        );
        String processedPath = FileUtil.normalize(
            storagePath + File.separator + path
        );
        if (!processedPath.startsWith(storagePath)) {
            throw new IllegalOperationDirectoryException(path);
        }
        return processedPath;
    }

    public static String path(File file) {
        return path(file.getPath());
    }

    public static String path(String path) {
        return FileUtil.normalize(path).replaceAll(".+/uid_\\d+", "");
    }

    public static List<String> path(List<File> list) {
        return list.stream().map(Storage::path).collect(Collectors.toList());
    }

    public static Stream<String> path(Stream<File> stream) {
        return stream.map(Storage::path);
    }

    public static Stream<File> dirStream(String dir) {
        return Arrays
            .stream(FileUtil.ls(link(dir)))
            .filter(f -> f.isDirectory() && !".git".equals(f.getName()));
    }

    public static Stream<File> dirFile(String dir) {
        return Arrays.stream(FileUtil.ls(link(dir))).filter(File::isFile);
    }

    public static List<File> directories(String dir) {
        return dirStream(dir).collect(Collectors.toList());
    }

    public static List<File> allDirectories(String dir) {
        List<File> list = new LinkedList<>();
        dirStream(dir)
            .forEach(
                file -> {
                    list.add(file);
                    list.addAll(allDirectories(file.getPath()));
                }
            );
        return list;
    }

    public static List<File> files(String dir) {
        return dirFile(dir).collect(Collectors.toList());
    }

    public static List<File> allFiles(String dir) {
        if (FileUtil.isFile(dir)) {
            return null;
        }
        List<File> list = new LinkedList<>();
        Arrays
            .stream(FileUtil.ls(link(dir)))
            .forEach(
                file -> {
                    list.add(file);
                    List<File> files = allFiles(file.getPath());
                    if (files != null) {
                        list.addAll(files);
                    }
                }
            );
        return list;
    }

    public static String readFormClasspath(String path) {
        return readFromClasspath(path, StandardCharsets.UTF_8);
    }

    public static String readFromClasspath(String path, Charset charset) {
        return IoUtil.read(Storage.class.getResourceAsStream(path), charset);
    }

    public static boolean exist(String path) {
        return FileUtil.exist(link(path));
    }

    public static File makeDirectory(String path) {
        return FileUtil.mkdir(link(path));
    }

    public static boolean deleteDirectory(String path) {
        return FileUtil.del(link(path));
    }

    public static void move(String oldPath, String newPath) {
        FileUtil.move(
            FileUtil.file(link(oldPath)),
            FileUtil.file(link(newPath)),
            true
        );
    }

    public static String get(String path) {
        return FileUtil.readUtf8String(link(path));
    }

    public static File put(String path, String content) {
        return FileUtil.writeUtf8String(content, link(path));
    }

    public static boolean delete(String path) {
        return FileUtil.del(link(path));
    }

    public static File putFile(byte[] bytes, String path) {
        return FileUtil.writeBytes(bytes, link(path));
    }

    public static byte[] getFile(String path) {
        return FileUtil.readBytes(link(path));
    }

    private static void compress(ZipOutputStream zos, File src, String name)
        throws IOException {
        if (src == null || !src.exists()) {
            return;
        }
        if (src.isFile()) {
            byte[] bufs = new byte[10240];

            ZipEntry zentry = new ZipEntry(name);
            zos.putNextEntry(zentry);

            FileInputStream in = new FileInputStream(src);

            BufferedInputStream bin = new BufferedInputStream(in, 10240);

            int readcount = 0;

            while ((readcount = bin.read(bufs, 0, 10240)) != -1) {
                zos.write(bufs, 0, readcount);
            }

            zos.closeEntry();
            bin.close();
        } else {
            // 文件夹
            File[] fs = src.listFiles();

            if (fs == null || fs.length == 0) {
                zos.putNextEntry(new ZipEntry(name + File.separator));
                zos.closeEntry();
                return;
            }

            for (File f : fs) {
                compress(zos, f, name + File.separator + f.getName());
            }
        }
    }
}
