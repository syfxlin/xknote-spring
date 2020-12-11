package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * 检查列表
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:18
 */
@Data
public class CheckList {

    @JsonProperty("check_list")
    private List<String> checkList;
}
