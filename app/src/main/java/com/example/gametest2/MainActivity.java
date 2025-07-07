package com.example.gametest2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private LinearLayout sidebar;
    private FrameLayout whiteboard;
    private EmojiManager emojiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the app header (ActionBar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sidebar = findViewById(R.id.sidebar);
        whiteboard = findViewById(R.id.whiteboard);
        emojiManager = new EmojiManager();

        // Add initial emojis to sidebar
        for (String emojiName : emojiManager.getSidebarEmojis()) {
            addEmojiToSidebar(emojiName);
        }

        // Set up reset button
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void addEmojiToSidebar(final String emojiName) {
        // Only add if not already present in sidebar view
        for (int i = 0; i < sidebar.getChildCount(); i++) {
            View child = sidebar.getChildAt(i);
            if (emojiName.equals(child.getTag())) return;
        }

        emojiManager.addToSidebar(emojiName);

        TextView emojiView = new TextView(this);
        emojiView.setText(emojiManager.getEmojiUnicode(emojiName));
        emojiView.setTextSize(32);
        emojiView.setGravity(Gravity.CENTER);
        emojiView.setPadding(8, 16, 8, 16);
        emojiView.setTag(emojiName);
        emojiView.setTextColor(Color.BLACK);

        // Drag from sidebar: create a new emoji on the board at center
        emojiView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View boardEmoji = createDraggableEmoji(emojiName);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    // Place at center of whiteboard
                    params.leftMargin = whiteboard.getWidth() / 2 - 60;
                    params.topMargin = whiteboard.getHeight() / 2 - 60;
                    whiteboard.addView(boardEmoji, params);
                    return true;
                }
                return false;
            }
        });

        sidebar.addView(emojiView);
    }

    // Helper to create a draggable emoji on the whiteboard (TextView, PNG, or GIF)
    private View createDraggableEmoji(String emojiName) {
        String gifPath = emojiManager.getEmojiGif(emojiName);
        Integer drawableId = emojiManager.getEmojiDrawable(emojiName);
        View emoji;
        if (gifPath != null) {
            ImageView gifView = new ImageView(this);
            Glide.with(this)
                    .asGif()
                    .load(gifPath)
                    .into(gifView);
            gifView.setLayoutParams(new FrameLayout.LayoutParams(120, 120));
            gifView.setTag(emojiName);
            setDragListener(gifView, emojiName);
            emoji = gifView;
        } else if (drawableId != null) {
            ImageView img = new ImageView(this);
            img.setImageResource(drawableId);
            img.setAdjustViewBounds(true);
            img.setMaxWidth(96);
            img.setMaxHeight(96);
            img.setTag(emojiName);
            setDragListener(img, emojiName);
            emoji = img;
        } else {
            TextView tv = new TextView(this);
            tv.setText(emojiManager.getEmojiUnicode(emojiName));
            tv.setTextSize(32);
            tv.setTextColor(Color.BLACK);
            tv.setGravity(Gravity.CENTER);
            tv.setTag(emojiName);
            tv.setBackgroundColor(Color.TRANSPARENT);
            setDragListener(tv, emojiName);
            emoji = tv;
        }
        return emoji;
    }

    private void setDragListener(View emoji, String emojiName) {
        emoji.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = event.getRawX() - params.leftMargin;
                        dY = event.getRawY() - params.topMargin;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.leftMargin = (int) (event.getRawX() - dX);
                        params.topMargin = (int) (event.getRawY() - dY);
                        v.setLayoutParams(params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        checkForCombination(v);
                        return true;
                }
                return false;
            }
        });
    }

    private void checkForCombination(View movedEmoji) {
        String name1 = (String) movedEmoji.getTag();
        Set<View> toRemove = new HashSet<>();
        String result = null;
        int centerX1 = (int) (movedEmoji.getX() + movedEmoji.getWidth() / 2);
        int centerY1 = (int) (movedEmoji.getY() + movedEmoji.getHeight() / 2);

        for (int i = 0; i < whiteboard.getChildCount(); i++) {
            View other = whiteboard.getChildAt(i);
            if (other != movedEmoji && other.getTag() != null) {
                int centerX2 = (int) (other.getX() + other.getWidth() / 2);
                int centerY2 = (int) (other.getY() + other.getHeight() / 2);
                int distance = (int) Math.hypot(centerX1 - centerX2, centerY1 - centerY2);

                // Consider as "overlapping" if centers are close (tweak threshold as needed)
                if (distance < 60) {
                    String name2 = (String) other.getTag();
                    result = emojiManager.combine(name1, name2);
                    if (result != null) {
                        toRemove.add(other);
                        toRemove.add(movedEmoji);
                        break;
                    }
                }
            }
        }

        if (result != null) {
            // Remove old emojis
            for (View v : toRemove) {
                whiteboard.removeView(v);
            }
            // Add new combined emoji at the place of movedEmoji
            View newEmoji = createDraggableEmoji(result);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) movedEmoji.getX();
            params.topMargin = (int) movedEmoji.getY();
            whiteboard.addView(newEmoji, params);
            addEmojiToSidebar(result);
            Toast.makeText(this, "Created: " + result, Toast.LENGTH_SHORT).show();
        }
    }

    private void resetGame() {
        // Clear all emojis from the whiteboard
        whiteboard.removeAllViews();
        // Clear the sidebar and restore initial emojis
        sidebar.removeAllViews();
        emojiManager = new EmojiManager(); // Reset to initial state
        for (String emojiName : emojiManager.getSidebarEmojis()) {
            addEmojiToSidebar(emojiName);
        }
    }
}
