package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView extends RecyclerView {

    private GestureDetector gestureDetector;

    public CustomRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Chặn fling quá mạnh
                if (Math.abs(velocityX) > 3000) {
                    return true;
                }

                float dx = e2.getX() - e1.getX();
                if (Math.abs(dx) < 100) {
                    // Vuốt nhẹ – ép scroll sang trái/phải 1 item
                    LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                    if (layoutManager != null) {
                        int currentPosition = layoutManager.findFirstVisibleItemPosition();
                        if (dx < 0) {
                            smoothScrollToPosition(currentPosition + 1);
                        } else {
                            smoothScrollToPosition(Math.max(currentPosition - 1, 0));
                        }
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        // Chặn fling quá mạnh
        if (Math.abs(velocityX) > 3000) {
            return false;
        }
        return super.fling(velocityX, velocityY);
    }
}
