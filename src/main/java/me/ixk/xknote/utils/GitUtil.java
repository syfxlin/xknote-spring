package me.ixk.xknote.utils;

import cn.hutool.core.io.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import me.ixk.xknote.entity.GitUserInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 7:16
 */
public class GitUtil {

    public static Git cloneRepo(String uri, File dir, GitUserInfo user) {
        try (
            Git git = Git
                .cloneRepository()
                .setURI(uri)
                .setDirectory(dir)
                .setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(
                        user.getName(),
                        user.getPassword()
                    )
                )
                .call()
        ) {
            return git;
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static Git init(File dir) {
        try (Git git = Git.init().setDirectory(dir).call()) {
            return git;
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static Git open(File dir) {
        try (Git git = Git.open(dir)) {
            return git;
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    public static GitUserInfo user(Git git) {
        final StoredConfig config = git.getRepository().getConfig();
        final String name = config.getString("user", null, "name");
        final String email = config.getString("user", null, "email");
        final String password = config.getString("user", null, "password");
        return new GitUserInfo(name.trim(), email.trim(), password.trim());
    }

    public static void user(Git git, GitUserInfo info) {
        final StoredConfig config = git.getRepository().getConfig();
        config.setString("user", null, "name", info.getName());
        config.setString("user", null, "email", info.getEmail());
        config.setString("user", null, "password", info.getPassword());
        try {
            config.save();
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    public static URIish remote(Git git) {
        try {
            final List<RemoteConfig> remoteConfigs = git.remoteList().call();
            if (remoteConfigs.size() <= 0) {
                return null;
            }
            final List<URIish> uris = remoteConfigs.get(0).getURIs();
            if (uris.size() <= 0) {
                return null;
            }
            return uris.get(0);
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static RemoteConfig remote(Git git, String url) {
        try {
            git.remoteRemove().setRemoteName("origin").call();
            return git
                .remoteAdd()
                .setName("origin")
                .setUri(new URIish(url))
                .call();
        } catch (URISyntaxException | GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static Iterable<RevCommit> log(Git git, String path) {
        try {
            return git.log().addPath(path.substring(1)).call();
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static String diff(Git git) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            final List<DiffEntry> diffs = git
                .diff()
                .setOutputStream(out)
                .setOldTree(prepareTreeParserByRef(git, "HEAD"))
                .call();
            return IoUtil.read(
                new ByteArrayInputStream(out.toByteArray()),
                StandardCharsets.UTF_8
            );
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static String diff(Git git, String commit, String path) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            final List<DiffEntry> diffs = git
                .diff()
                .setOldTree(prepareTreeParserById(git, commit))
                .setPathFilter(PathFilter.create(path.substring(1)))
                .setOutputStream(out)
                .call();
            return IoUtil.read(
                new ByteArrayInputStream(out.toByteArray()),
                StandardCharsets.UTF_8
            );
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    private static AbstractTreeIterator prepareTreeParserById(
        Git git,
        String objectId
    ) {
        final Repository repository = git.getRepository();
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    public static AbstractTreeIterator prepareTreeParserByRef(
        Git git,
        String ref
    ) {
        final Repository repository = git.getRepository();
        try (RevWalk walk = new RevWalk(repository)) {
            Ref head = repository.exactRef(ref);
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    public static Status status(Git git) {
        try {
            return git.status().call();
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static Ref rollback(Git git, String commit, String path) {
        try {
            return git
                .checkout()
                .setStartPoint(git.getRepository().resolve(commit).getName())
                .setAllPaths(false)
                .addPath(path.substring(1))
                .call();
        } catch (GitAPIException | IOException e) {
            throw new GitException(e);
        }
    }

    public static PullResult pull(Git git, GitUserInfo user) {
        try {
            return git
                .pull()
                .setRemote("origin")
                .setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(
                        user.getName(),
                        user.getPassword()
                    )
                )
                .call();
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static Iterable<PushResult> push(
        Git git,
        boolean force,
        GitUserInfo user
    ) {
        try {
            git
                .commit()
                .setMessage("Update: " + LocalDateTime.now())
                .setAll(true)
                .call();
            return git
                .push()
                .setForce(force)
                .setRemote("origin")
                .setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(
                        user.getName(),
                        user.getPassword()
                    )
                )
                .call();
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    public static class GitException extends RuntimeException {

        public GitException() {}

        public GitException(String message) {
            super(message);
        }

        public GitException(String message, Throwable cause) {
            super(message, cause);
        }

        public GitException(Throwable cause) {
            super(cause);
        }

        public GitException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
        ) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
