package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class CheckList {
    @JsonProperty("check_list")
    private List<String> checkList;
}
