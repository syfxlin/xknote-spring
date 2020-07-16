package me.ixk.xknote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author syfxlin
 * @since 2020-07-01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String nickname;

    private String email;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonProperty("email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String rememberToken;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
