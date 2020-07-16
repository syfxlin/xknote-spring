package me.ixk.xknote.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FolderServiceImplTest {

    @Test
    void getFlat() {
        FolderServiceImpl folderService = new FolderServiceImpl();
        JsonNode jsonNode = folderService.getFlat("/uid_1/");
        System.out.println(jsonNode);
    }

    @Test
    void get() {
        FolderServiceImpl folderService = new FolderServiceImpl();
        JsonNode jsonNode = folderService.get("/uid_1/");
        System.out.println(jsonNode);
    }
}
