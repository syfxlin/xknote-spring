package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ConfigParam {
    private JsonNode tinymceSetting;
    private JsonNode aceSetting;
    private JsonNode xkSetting;
}
