package me.ixk.xknote.entity;

import lombok.Builder;
import lombok.Data;

/**
 * XknoteData
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:10
 */
@Data
@Builder
public class XknoteData {

    private Long userId;
    private String nickname;
    private String xknoteName;
    private String documentExt;
}
