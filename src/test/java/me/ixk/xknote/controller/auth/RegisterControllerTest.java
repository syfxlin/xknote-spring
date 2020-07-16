package me.ixk.xknote.controller.auth;

import me.ixk.xknote.utils.Storage;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    @Test
    void readJson() {
        String settingsJson = Storage.readFormClasspath("/settings.json");
        System.out.println(settingsJson);
    }
}
