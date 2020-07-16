package me.ixk.xknote.entity;

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
public class PasswordResets implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;

    private String token;

    private LocalDateTime createdAt;
}
