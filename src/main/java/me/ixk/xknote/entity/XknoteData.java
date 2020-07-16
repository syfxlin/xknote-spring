package me.ixk.xknote.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class XknoteData {
    private Long userId;
    private String nickname;
    private String xknoteName;
    private String documentExt;
}
