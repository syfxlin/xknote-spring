package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteItem {
    private String path;

    private String title;
    private String author;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private String content;
}
