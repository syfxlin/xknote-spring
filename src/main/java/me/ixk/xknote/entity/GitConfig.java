package me.ixk.xknote.entity;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 10:14
 */
public class GitConfig {

    private final String repo;
    private final String name;
    private final String email;

    public GitConfig(String repo, String name, String email) {
        this.repo = repo;
        this.name = name;
        this.email = email;
    }

    public String getRepo() {
        return repo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
