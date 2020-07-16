package me.ixk.xknote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class BlogInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private String blogSystem;

    private String blogUrl;

    private String blogUsername;

    private String blogPassword;

    private String blogToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
