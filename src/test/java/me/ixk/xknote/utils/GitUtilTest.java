package me.ixk.xknote.utils;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 7:30
 */
@Slf4j
@SpringBootTest
class GitUtilTest {

    @Test
    void config() {
        final String path = Storage.link("uid_1/the-road-to-transition");
        final File file = FileUtil.file(path);
        final Git git = GitUtil.open(file);
        log.info("{}", GitUtil.user(git));
    }
}
