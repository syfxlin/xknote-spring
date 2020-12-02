package me.ixk.xknote.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import me.ixk.xknote.utils.Json;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

@Data
public class ResponseInfo {

    public static ResponseEntity<String> text(String text) {
        return text(text, HttpStatus.ACCEPTED);
    }

    public static ResponseEntity<String> text(String text, HttpStatus status) {
        return text(text, status, HttpHeaders.EMPTY);
    }

    public static ResponseEntity<String> text(
        String text,
        HttpStatus status,
        HttpHeaders headers
    ) {
        return ResponseEntity.status(status).headers(headers).body(text);
    }

    public static ResponseEntity<ModelAndView> view(
        ModelAndView modelAndView,
        String name
    ) {
        return view(
            modelAndView,
            name,
            HttpStatus.ACCEPTED,
            new ConcurrentHashMap<>()
        );
    }

    public static ResponseEntity<ModelAndView> view(
        ModelAndView modelAndView,
        String name,
        HttpStatus status
    ) {
        return view(modelAndView, name, status, new ConcurrentHashMap<>());
    }

    public static ResponseEntity<ModelAndView> view(
        ModelAndView modelAndView,
        String name,
        Map<String, ?> objects
    ) {
        return view(modelAndView, name, HttpStatus.ACCEPTED, objects);
    }

    public static ResponseEntity<ModelAndView> view(
        ModelAndView modelAndView,
        String name,
        HttpStatus status,
        Map<String, ?> objects
    ) {
        return view(modelAndView, name, status, objects);
    }

    public static ResponseEntity<ModelAndView> view(
        ModelAndView modelAndView,
        String name,
        HttpStatus status,
        HttpHeaders headers,
        Map<String, ?> objects
    ) {
        modelAndView.setViewName(name);
        modelAndView.addAllObjects(objects);
        return ResponseEntity
            .status(status)
            .headers(headers)
            .body(modelAndView);
    }

    public static ResponseEntity<JsonNode> json(JsonNode node) {
        return json(node, HttpStatus.ACCEPTED);
    }

    public static ResponseEntity<JsonNode> json(
        JsonNode node,
        HttpStatus status
    ) {
        return json(node, status, HttpHeaders.EMPTY);
    }

    public static ResponseEntity<JsonNode> json(
        JsonNode node,
        HttpStatus status,
        HttpHeaders headers
    ) {
        return ResponseEntity.status(status).headers(headers).body(node);
    }

    public static ResponseEntity<Object> stdJson() {
        ObjectNode objectNode = Json.createObject();
        objectNode.put("error", false);
        return ResponseEntity.ok(objectNode);
    }

    public static ResponseEntity<Object> stdJson(String name, Object object) {
        return stdJson(name, Json.convertToNode(object));
    }

    public static ResponseEntity<Object> stdJson(String name, JsonNode node) {
        ObjectNode objectNode = Json.createObject();
        objectNode.put("error", false);
        objectNode.set(name, node);
        return ResponseEntity.ok(objectNode);
    }

    public static ResponseEntity<Object> stdError(String message, int status) {
        return stdError(message, HttpStatus.valueOf(status));
    }

    public static ResponseEntity<Object> stdError(
        String message,
        HttpStatus status
    ) {
        ObjectNode objectNode = Json.createObject();
        objectNode.put("error", message);
        return ResponseEntity.status(status).body(objectNode);
    }
}
