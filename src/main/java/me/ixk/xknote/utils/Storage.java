package me.ixk.xknote.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.ixk.xknote.config.XknoteConfig;

public class Storage {

    private static String linkPath(String dir) {
        String storagePath = Application
            .getBean(XknoteConfig.class)
            .getStoragePath();
        if (FileUtil.isSub(new File(storagePath), new File(dir))) {
            return dir;
        } else if (dir.startsWith("/") || dir.startsWith("\\")) {
            return storagePath + dir;
        } else {
            return storagePath + "/" + dir;
        }
    }

    public static Stream<File> dirStream(String dir) {
        return Arrays
            .stream(FileUtil.ls(linkPath(dir)))
            .filter(f -> f.isDirectory() && !f.getName().equals(".git"));
    }

    public static Stream<File> fileStream(String dir) {
        return Arrays.stream(FileUtil.ls(linkPath(dir))).filter(File::isFile);
    }

    public static String processPath(File file) {
        return processPath(file.getPath());
    }

    public static String processPath(String path) {
        return path.replace("\\", "/").replaceAll(".+/uid_\\d+", "");
    }

    public static List<String> processPath(List<File> list) {
        return list
            .stream()
            .map(Storage::processPath)
            .collect(Collectors.toList());
    }

    public static Stream<String> processPath(Stream<File> stream) {
        return stream.map(Storage::processPath);
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
        return fileStream(dir).collect(Collectors.toList());
    }

    public static List<File> allFiles(String dir) {
        if (FileUtil.isFile(dir)) {
            return null;
        }
        List<File> list = new LinkedList<>();
        Arrays
            .stream(FileUtil.ls(linkPath(dir)))
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
        return FileUtil.exist(linkPath(path));
    }

    public static File makeDirectory(String path) {
        return FileUtil.mkdir(linkPath(path));
    }

    public static boolean deleteDirectory(String path) {
        return FileUtil.del(linkPath(path));
    }

    public static void move(String oldPath, String newPath) {
        FileUtil.move(
            FileUtil.file(linkPath(oldPath)),
            FileUtil.file(linkPath(newPath)),
            true
        );
    }

    public static String get(String path) {
        return FileUtil.readUtf8String(linkPath(path));
    }

    public static File put(String path, String content) {
        return FileUtil.writeUtf8String(content, linkPath(path));
    }

    public static boolean delete(String path) {
        return FileUtil.del(linkPath(path));
    }

    public static File putFile(byte[] bytes, String path) {
        return FileUtil.writeBytes(bytes, linkPath(path));
    }

    public static byte[] getFile(String path) {
        return FileUtil.readBytes(linkPath(path));
    }

    public static void folder2zip(String path, String zippath, String zipname) {
        File src = new File(path);
        ZipOutputStream zos = null;

        if (!src.exists() || !src.isDirectory()) {
            // 源目录不存在 或不是目录 , 则异常
            throw new RuntimeException("压缩源目录不存在或非目录!" + path);
        }

        File destdir = new File(zippath);

        if (!destdir.exists()) {
            // 创建目录
            destdir.mkdirs();
        }

        File zipfile = new File(zippath + File.separator + zipname);

        File[] srclist = src.listFiles();

        if (srclist == null || srclist.length == 0) {
            // 源目录内容为空,无需压缩
            throw new RuntimeException("源目录内容为空,无需压缩下载!" + path);
        }

        try {
            zos =
                new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(zipfile))
                );

            // 递归压缩目录下所有的文件  ;
            compress(zos, src, src.getName());

            zos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("压缩目标文件不存在!" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("压缩文件IO异常!" + e.getMessage());
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
        }
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
