package com.avb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AVBError{
    private String code;
    private String message;

    public AVBError(String text) {
        toParseJson(text);
    }

    /**
     * Парсит строку с ошибкой
     *
     * @param text
     */
    private void toParseJson(String text) {
        String[] entries = text.split(",");
        for (String entry : entries) {
            String[] keys = entry.split(":");
            for (int i = 0; i < keys.length; i++) {
                keys[i] = keys[i].replace("{", "")
                        .replace("}", "")
                        .replace("\"", "");
            }
            if (keys[0].equals("code")) {
                this.code = keys[1];
            } else if (keys[0].equals("message")) {
                this.message = keys[1];
            } else {
                this.code = "404";
                this.message = text;
            }
        }
    }
}
