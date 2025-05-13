package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.moviereviewapp.R;

public class ProgressButton {
    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;
    private Animation fade_in;

    public ProgressButton (Context context, View view) {

        fade_in = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in);

        cardView = view.findViewById(R.id.cardView_SplashScreen);
        layout = view.findViewById(R.id.layout_SplashScreen);
        progressBar = view.findViewById(R.id.progressBar_SplashScreen);
        textView = view.findViewById(R.id.textView_SplashScreen);
    }

    public void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Loading...");
        textView.setAnimation(fade_in);
        progressBar.setAnimation(fade_in);
    }

    public void buttonFinished() {
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
        textView.setTextColor(cardView.getResources().getColor(R.color.blue_huy2));
        layout.setBackground(cardView.getResources().getDrawable(R.color.light_grey_huy));
        cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.light_grey_huy));
    }
}
