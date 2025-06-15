package com.example.moviereviewapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviereviewapp.Activities.DiscussionForum;
import com.example.moviereviewapp.Activities.MainScreen;
import com.example.moviereviewapp.Activities.SearchActivity;
import com.example.moviereviewapp.Activities.UserProfileActivity;
import com.example.moviereviewapp.R;

public class BaseActivity extends AppCompatActivity {

    protected LinearLayout bottomBar;
    protected ImageView ivHome, ivSearch, ivPlay, ivProfile;
    private int lastScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomBar(Activity activity, String currentScreen) {
        ivHome = activity.findViewById(R.id.ivHome);
        ivSearch = activity.findViewById(R.id.ivSearch);
        ivPlay = activity.findViewById(R.id.ivPlay);
        ivProfile = activity.findViewById(R.id.ivProfile);
        bottomBar = activity.findViewById(R.id.bottomBar);

        // Gán icon tương ứng với screen hiện tại
        switch (currentScreen) {
            case "home":
                ivHome.setImageResource(R.drawable.clicked_home_icon);
                break;
            case "search":
                ivSearch.setImageResource(R.drawable.clicked_search_icon);
                break;
            case "play":
                ivPlay.setImageResource(R.drawable.clicked_play_icon);
                break;
            case "profile":
                ivProfile.setImageResource(R.drawable.clicked_user_icon);
                break;
        }

        // ScrollView effect
        ScrollView scrollView = activity.findViewById(R.id.scrollView);
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                int scrollY = scrollView.getScrollY();
                if (scrollY > lastScrollY) {
                    bottomBar.setBackgroundColor(0xAA0F0F0F);
                } else if (scrollY < lastScrollY) {
                    bottomBar.setBackgroundColor(0xFF111111);
                }
                lastScrollY = scrollY;
            });
        }

        ivHome.setOnClickListener(v -> {
            if (!currentScreen.equals("home")) {
                activity.startActivity(new Intent(activity, MainScreen.class));
                activity.overridePendingTransition(0, 0);
            }
        });

        ivSearch.setOnClickListener(v -> {
            if (!currentScreen.equals("search")) {
                activity.startActivity(new Intent(activity, SearchActivity.class));
                activity.overridePendingTransition(0, 0);
            }
        });

        ivPlay.setOnClickListener(v -> {
            if (!currentScreen.equals("play")) {

                activity.overridePendingTransition(0, 0);
            }
        });

        ivProfile.setOnClickListener(v -> {
            if (!currentScreen.equals("profile")) {
                activity.startActivity(new Intent(activity, UserProfileActivity.class));
                activity.overridePendingTransition(0, 0);
            }
        });
    }


}
