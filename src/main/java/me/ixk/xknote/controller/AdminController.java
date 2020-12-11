package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.ixk.xknote.entity.Config;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.ConfigServiceImpl;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理控制器
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:20
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

    @Autowired
    ConfigServiceImpl configService;

    @Autowired
    UsersServiceImpl usersService;

    /**
     * 获取所有用户
     *
     * @return 所有用户
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getUser() {
        return ResponseInfo.stdJson("users", usersService.list());
    }

    /**
     * 删除用户
     *
     * @param id 用户 id
     *
     * @return 正常响应
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(
        @PathVariable(name = "id") Long id
    ) {
        if (id == 1L) {
            return ResponseInfo.stdError(
                "You are an administrator and cannot delete yourself.",
                HttpStatus.CONFLICT
            );
        }
        usersService.removeById(id);
        return ResponseInfo.stdJson();
    }

    /**
     * 获取设置
     *
     * @return 设置
     */
    @GetMapping("/conf")
    public ResponseEntity<Object> getConfig() {
        List<Config> configs = configService.list();
        ObjectNode objectNode = Json.createObject();
        for (Config config : configs) {
            objectNode.put(config.getConfigName(), config.getConfigValue());
        }
        return ResponseInfo.stdJson("config", objectNode);
    }

    /**
     * 设置
     *
     * @param config 设置信息
     *
     * @return 正常响应
     */
    @PutMapping("/conf")
    public ResponseEntity<Object> setConfig(@RequestBody ObjectNode config) {
        Iterator<Map.Entry<String, JsonNode>> fields = config.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            Config config1 = configService
                .query()
                .eq("config_name", next.getKey())
                .one();
            config1.setConfigValue(next.getValue().asText());
            configService.saveOrUpdate(config1);
        }
        return ResponseInfo.stdJson();
    }
}
