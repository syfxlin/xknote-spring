package me.ixk.xknote.entity;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 10:13
 */
public class GitUserInfo {

    private final String name;
    private final String email;
    private final String password;

    public GitUserInfo(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = null;
    }

    public GitUserInfo(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return (
            "GitUserInfo{" +
            "name='" +
            name +
            '\'' +
            ", email='" +
            email +
            '\'' +
            '}'
        );
    }
}
