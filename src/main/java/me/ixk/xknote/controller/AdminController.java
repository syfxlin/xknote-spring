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
import me.ixk.xknote.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {
    @Autowired
    ConfigServiceImpl configService;

    @Autowired
    UsersServiceImpl usersService;

    @GetMapping("/users")
    public ResponseEntity<Object> getUser() {
        return ResponseInfo.stdJson("users", usersService.list());
    }

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

    @GetMapping("/conf")
    public ResponseEntity<Object> getConfig() {
        List<Config> configs = configService.list();
        ObjectNode objectNode = JSON.createObject();
        for (Config config : configs) {
            objectNode.put(config.getConfigName(), config.getConfigValue());
        }
        return ResponseInfo.stdJson("config", objectNode);
    }

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
