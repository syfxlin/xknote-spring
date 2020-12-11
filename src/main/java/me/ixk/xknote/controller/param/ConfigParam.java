package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * 配置
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:19
 */
@Data
public class ConfigParam {

    private JsonNode tinymceSetting;
    private JsonNode aceSetting;
    private JsonNode xkSetting;
}
