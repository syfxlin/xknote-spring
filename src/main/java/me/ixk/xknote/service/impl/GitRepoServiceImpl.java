package me.ixk.xknote.service.impl;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import me.ixk.xknote.entity.GitConfig;
import me.ixk.xknote.entity.GitInfo;
import me.ixk.xknote.entity.GitUserInfo;
import me.ixk.xknote.utils.Crypt;
import me.ixk.xknote.utils.GitUtil;
import me.ixk.xknote.utils.GitUtil.GitException;
import me.ixk.xknote.utils.Storage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 8:10
 */
@Service
public class GitRepoServiceImpl {

    @Autowired
    GitInfoServiceImpl gitInfoService;

    @Autowired
    Crypt crypt;

    public int init(String path, long id, String repoUrl, GitUserInfo user) {
        if (user == null) {
            user = getUser(id);
            if (user == null) {
                return 404;
            }
        }
        final Git git = GitUtil.init(getPath(path, id));
        GitUtil.remote(git, repoUrl);
        GitUtil.user(
            git,
            new GitUserInfo(
                user.getName(),
                user.getEmail(),
                crypt.encrypt(user.getPassword())
            )
        );
        return 200;
    }

    public int cloneRepo(
        String path,
        long id,
        String repoUrl,
        GitUserInfo user
    ) {
        if (user == null) {
            user = getUser(id);
            if (user == null) {
                return 404;
            }
        }
        final Git git = GitUtil.cloneRepo(repoUrl, getPath(path, id), user);
        GitUtil.user(
            git,
            new GitUserInfo(
                user.getName(),
                user.getEmail(),
                crypt.encrypt(user.getPassword())
            )
        );
        return 200;
    }

    public GitConfig config(String path, long id) {
        try {
            final Git git = GitUtil.open(getPath(path, id));
            final URIish remote = GitUtil.remote(git);
            final GitUserInfo user = GitUtil.user(git);
            return new GitConfig(
                remote.toString(),
                user.getName(),
                user.getEmail()
            );
        } catch (GitException e) {
            return null;
        }
    }

    public int configInfo(String path, long id, GitUserInfo user) {
        Git git = GitUtil.open(getPath(path, id));
        GitUtil.user(
            git,
            new GitUserInfo(
                user.getName(),
                user.getEmail(),
                crypt.encrypt(user.getPassword())
            )
        );
        return 200;
    }

    public int configRemote(
        String path,
        long id,
        String repoUrl,
        GitUserInfo user
    ) {
        if (user == null) {
            user = getUser(id);
            if (user == null) {
                return 404;
            }
        }
        Git git = GitUtil.open(getPath(path, id));
        GitUtil.remote(git, repoUrl);
        return 200;
    }

    public int pull(String path, long id) {
        Git git = GitUtil.open(getPath(path, id));
        final GitUserInfo user = GitUtil.user(git);
        GitUtil.pull(
            git,
            new GitUserInfo(
                user.getName(),
                user.getEmail(),
                crypt.decrypt(user.getPassword())
            )
        );
        return 200;
    }

    public int push(String path, long id, boolean force) {
        final Git git = GitUtil.open(getPath(path, id));
        final GitUserInfo user = GitUtil.user(git);
        GitUtil.push(
            git,
            force,
            new GitUserInfo(
                user.getName(),
                user.getEmail(),
                crypt.decrypt(user.getPassword())
            )
        );
        return 200;
    }

    public boolean check(String path, long id) {
        return FileUtil.exist(getPath(path, id) + File.separator + ".git");
    }

    public String diff(String path, long id) {
        final Git git = GitUtil.open(getPath(path, id));
        return GitUtil.diff(git);
    }

    public String diff(String path, long id, String commit, String filePath) {
        final Git git = GitUtil.open(getPath(path, id));
        return GitUtil.diff(git, commit, filePath);
    }

    public Iterable<RevCommit> log(String path, long id, String filePath) {
        final Git git = GitUtil.open(getPath(path, id));
        return GitUtil.log(git, filePath);
    }

    public Ref rollback(String path, long id, String commit, String filePath) {
        final Git git = GitUtil.open(getPath(path, id));
        return GitUtil.rollback(git, commit, filePath);
    }

    public String status(String path, long id) {
        final Git git = GitUtil.open(getPath(path, id));
        final Status status = GitUtil.status(git);
        StringBuilder builder = new StringBuilder();
        for (String name : status.getAdded()) {
            builder.append("Added: ").append(name).append("\n");
        }
        for (String name : status.getChanged()) {
            builder.append("Changed: ").append(name).append("\n");
        }
        for (String name : status.getConflicting()) {
            builder.append("Conflicting: ").append(name).append("\n");
        }
        for (String index : status.getIgnoredNotInIndex()) {
            builder.append("IgnoredNotInIndex: ").append(index).append("\n");
        }
        for (String name : status.getMissing()) {
            builder.append("Missing: ").append(name).append("\n");
        }
        for (String name : status.getModified()) {
            builder.append("Modified: ").append(name).append("\n");
        }
        for (String name : status.getRemoved()) {
            builder.append("Removed: ").append(name).append("\n");
        }
        for (String name : status.getUntracked()) {
            builder.append("Untracked: ").append(name).append("\n");
        }
        for (String name : status.getUntrackedFolders()) {
            builder.append("UntrackedFolders: ").append(name).append("\n");
        }
        return builder.toString();
    }

    private File getPath(String path, long id) {
        return FileUtil.file(Storage.link("uid_" + id + File.separator + path));
    }

    private GitUserInfo getUser(long id) {
        GitUserInfo user;
        final GitInfo info = gitInfoService.query().eq("uid", id).one();
        if (info == null) {
            return null;
        }
        user =
            new GitUserInfo(
                info.getGitName(),
                info.getGitEmail(),
                crypt.decrypt(info.getGitPassword())
            );
        return user;
    }
}
