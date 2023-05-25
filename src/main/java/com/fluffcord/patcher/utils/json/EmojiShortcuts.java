package com.fluffcord.patcher.utils.json;

import java.util.List;

public class EmojiShortcuts {
    private String emoji;
    private List<String> shortcuts;

    public List<String> getShortcuts() {
        return shortcuts;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setShortcuts(List<String> shortcuts) {
        this.shortcuts = shortcuts;
    }
}
