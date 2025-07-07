package com.example.gametest2;

import java.util.*;

public class EmojiManager {

    private final Map<String, String> emojiMap = new HashMap<>();
    private final Map<Set<String>, String> combinations = new HashMap<>();
    private final Set<String> sidebarEmojis = new LinkedHashSet<>();

    public EmojiManager() {
        // Basic emojis
        emojiMap.put("happy", "\uD83D\uDE00"); // 😀
        emojiMap.put("arm", "\uD83D\uDCAA");   // 💪
        emojiMap.put("sad", "\uD83D\uDE22");   // 😢
        emojiMap.put("car", "\uD83D\uDE97");   // 🚗
        emojiMap.put("fire", "\uD83D\uDD25");  // 🔥
        emojiMap.put("ice", "\u2744\uFE0F");   // ❄️
        emojiMap.put("rocket", "\uD83D\uDE80"); // 🚀
        emojiMap.put("brain", "\uD83E\uDDE0"); // 🧠
        emojiMap.put("money", "\uD83D\uDCB0"); // 💰
        emojiMap.put("tree", "\uD83C\uDF33");  // 🌳
        emojiMap.put("water", "\uD83D\uDCA7"); // 💧
        emojiMap.put("cat", "\uD83D\uDC31");   // 🐱
        emojiMap.put("dog", "\uD83D\uDC36");   // 🐶

        // Combinations
        addCombination("happy", "arm", "super_happy");
        emojiMap.put("super_happy", "\uD83D\uDE01"); // 😁

        addCombination("sad", "car", "broken_car");
        emojiMap.put("broken_car", "\uD83D\uDE97\uD83D\uDE22"); // 🚗😢

        addCombination("arm", "car", "fast_car");
        emojiMap.put("fast_car", "\uD83C\uDFC1"); // 🏁

        addCombination("happy", "sad", "confused");
        emojiMap.put("confused", "\uD83D\uDE15"); // 😕

        addCombination("fire", "car", "explosion");
        emojiMap.put("explosion", "\uD83D\uDE92\uD83D\uDD25"); // 🚒🔥

        addCombination("ice", "fire", "water");
        // 💧 already defined

        addCombination("rocket", "fire", "launch");
        emojiMap.put("launch", "\uD83D\uDE80\uD83D\uDD25"); // 🚀🔥

        addCombination("brain", "rocket", "genius");
        emojiMap.put("genius", "\uD83E\uDDE0\uD83D\uDE80"); // 🧠🚀

        addCombination("money", "car", "taxi");
        emojiMap.put("taxi", "\uD83D\uDE96"); // 🚖

        addCombination("tree", "fire", "ash");
        emojiMap.put("ash", "\uD83D\uDD25\uD83C\uDF32"); // 🔥🌲

        addCombination("water", "tree", "fruit_tree");
        emojiMap.put("fruit_tree", "\uD83C\uDF4E\uD83C\uDF33"); // 🍎🌳

        addCombination("cat", "dog", "friendship");
        emojiMap.put("friendship", "\uD83D\uDC31\uD83D\uDC36\u2764\uFE0F"); // 🐱🐶❤️

        addCombination("cat", "rocket", "space_cat");
        emojiMap.put("space_cat", "\uD83D\uDC31\uD83D\uDE80"); // 🐱🚀

        addCombination("dog", "car", "road_trip");
        emojiMap.put("road_trip", "\uD83D\uDC36\uD83D\uDE97"); // 🐶🚗

        // Initial sidebar emojis
        sidebarEmojis.add("happy");
        sidebarEmojis.add("arm");
        sidebarEmojis.add("sad");
        sidebarEmojis.add("car");
        sidebarEmojis.add("fire");
        sidebarEmojis.add("ice");
        sidebarEmojis.add("rocket");
        sidebarEmojis.add("brain");
        sidebarEmojis.add("money");
        sidebarEmojis.add("tree");
        sidebarEmojis.add("water");
        sidebarEmojis.add("cat");
        sidebarEmojis.add("dog");
    }

    private void addCombination(String a, String b, String result) {
        Set<String> key = new HashSet<>();
        key.add(a);
        key.add(b);
        combinations.put(key, result);
    }

    public String combine(String a, String b) {
        Set<String> key = new HashSet<>();
        key.add(a);
        key.add(b);
        return combinations.get(key);
    }

    public String getEmojiUnicode(String name) {
        return emojiMap.getOrDefault(name, "?");
    }

    public Set<String> getSidebarEmojis() {
        return new LinkedHashSet<>(sidebarEmojis);
    }

    public boolean isInSidebar(String name) {
        return sidebarEmojis.contains(name);
    }

    public void addToSidebar(String name) {
        sidebarEmojis.add(name);
    }
}
