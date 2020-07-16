package me.ixk.xknote.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoteServiceImplTest {

    @Test
    void get() {
        NoteServiceImpl noteService = new NoteServiceImpl();
        JsonNode node = noteService.get("/uid_3/mynote2/后端学习笔记.md");
        System.out.println(node.toString());
    }

    @Test
    void getAll() {
        NoteServiceImpl noteService = new NoteServiceImpl();
        JsonNode notes = noteService.getAll("/uid_3");
        System.out.println(notes.toString());
    }

    @Test
    void checkStatus() {
        NoteServiceImpl noteService = new NoteServiceImpl();
        JsonNode node = noteService.checkStatus(
            Arrays.asList("/mynote2/note2.md", "/mynote2/后端学习笔记.md"),
            "/uid_3"
        );
        System.out.println(node.toString());
    }
}
