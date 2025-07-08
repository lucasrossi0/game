package com.example.gametest2;

import java.util.*;

public class EmojiManager {

    private final Map<String, String> emojiMap = new HashMap<>();
    private final Map<String, Integer> emojiDrawables = new HashMap<>();
    private final Map<String, String> emojiGifs = new HashMap<>();
    private final Map<Set<String>, String> combinations = new HashMap<>();
    private final Set<String> sidebarEmojis = new LinkedHashSet<>();

    public EmojiManager() {
        // Basic emojis (Unicode)
        emojiMap.put("happy", "\uD83D\uDE00");
        emojiMap.put("arm", "\uD83D\uDCAA");
        emojiMap.put("sad", "\uD83D\uDE22");
        emojiMap.put("car", "\uD83D\uDE97");
        emojiMap.put("fire", "\uD83D\uDD25");
        emojiMap.put("ice", "\u2744\uFE0F");
        emojiMap.put("rocket", "\uD83D\uDE80");
        emojiMap.put("brain", "\uD83E\uDDE0");
        emojiMap.put("money", "\uD83D\uDCB0");
        emojiMap.put("tree", "\uD83C\uDF33");
        emojiMap.put("water", "\uD83D\uDCA7");
        emojiMap.put("cat", "\uD83D\uDC31");
        emojiMap.put("dog", "\uD83D\uDC36");

//        // --- PNG results (place these PNGs in drawable/) ---
//        emojiDrawables.put("super_happy", R.drawable.super_happy);
//        emojiDrawables.put("fast_car", R.drawable.fast_car);
//        emojiDrawables.put("taxi", R.drawable.taxi);
//        emojiDrawables.put("ash", R.drawable.ash);
//        emojiDrawables.put("friendship", R.drawable.friendship);
//        emojiDrawables.put("confused", R.drawable.confused);

        // --- GIF results (place these GIFs in assets/) ---
        emojiGifs.put("explosion", "file:///android_asset/rocketfly.gif");
        emojiGifs.put("genius", "file:///android_asset/genius.gif");
        emojiGifs.put("fruit_tree", "file:///android_asset/fruit_tree.gif");
        emojiGifs.put("space_cat", "file:///android_asset/space_cat.gif");
        emojiGifs.put("road_trip", "file:///android_asset/coete.gif");
        emojiGifs.put("broken_car", "file:///android_asset/broken_car.gif");

        // --- Combinations: 50/50 split ---
        addCombination("happy", "arm", "super_happy");       // PNG
        addCombination("sad", "car", "broken_car");          // GIF
        addCombination("arm", "car", "fast_car");            // PNG
        addCombination("happy", "sad", "confused");          // PNG
        addCombination("fire", "car", "explosion");          // GIF
        addCombination("ice", "fire", "water");              // Unicode
        addCombination("rocket", "fire", "genius");          // GIF
        addCombination("money", "car", "taxi");              // PNG
        addCombination("tree", "fire", "ash");               // PNG
        addCombination("water", "tree", "fruit_tree");       // GIF
        addCombination("cat", "dog", "friendship");          // PNG
        addCombination("cat", "rocket", "space_cat");        // GIF
        addCombination("dog", "car", "road_trip");           // GIF

        sidebarEmojis.addAll(Arrays.asList(
                "happy", "arm", "sad", "car", "fire", "ice", "rocket", "brain",
                "money", "tree", "water", "cat", "dog"
        ));
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

    public Integer getEmojiDrawable(String name) {
        return emojiDrawables.get(name);
    }

    public String getEmojiGif(String name) {
        return emojiGifs.get(name);
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
