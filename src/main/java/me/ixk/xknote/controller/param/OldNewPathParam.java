package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OldNewPathParam {
    @JsonProperty("old_path")
    private String oldPath;

    @JsonProperty("new_path")
    private String newPath;
}
