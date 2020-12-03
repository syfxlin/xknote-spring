package me.ixk.xknote.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import me.ixk.xknote.entity.GitConfig;
import me.ixk.xknote.entity.GitUserInfo;
import me.ixk.xknote.utils.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 10:28
 */
@SpringBootTest
class GitRepoServiceImplTest {

    @Autowired
    GitRepoServiceImpl gitRepoService;

    @Test
    void init() {
        final String path = Storage.link(
            "uid_" + 1 + File.separator + "mynote2" + File.separator + ".git"
        );
        FileUtil.del(path);
        gitRepoService.init(
            "mynote2",
            1L,
            "https://github.com/syfxlin/xknote-spring",
            new GitUserInfo("syfxlin", "syfxlin@gmail.com", "123456")
        );
        assertTrue(FileUtil.exist(path));
    }

    @Test
    void cloneRepo() {
        final String path = Storage.link(
            "uid_" + 1 + File.separator + "time-log"
        );
        FileUtil.del(path);
        // gitRepoService.cloneRepo(
        //     "time-log",
        //     1L,
        //     "https://github.com/syfxlin/time-log",
        //     new GitUserInfo("syfxlin", "syfxlin@gmail.com", "123456")
        // );
        // assertTrue(FileUtil.exist(path));
    }

    @Test
    void config() {
        final GitConfig config = gitRepoService.config("mynote2", 1L);
        assertEquals("syfxlin", config.getName());
        assertEquals("syfxlin@gmail.com", config.getEmail());
        assertEquals(
            "https://github.com/syfxlin/xknote-spring",
            config.getRepo()
        );
    }
}
