package com.example.healthcareapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthcareapp.Adapter.OnboadingAdapter;
import com.example.healthcareapp.Model.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    private LinearLayout layoutDots;
    Button skip, next;
    ArrayList<OnboardingItem> slideList = new ArrayList<>();

    List<OnboardingItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager2);
        layoutDots = findViewById(R.id.dots);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        slideList.add(new OnboardingItem(R.drawable.welcome_screen, "Welcome Screen", "Start your healthy journey.\nBe healthy and achieve your goals"));
        slideList.add(new OnboardingItem(R.drawable.track_your_progress, "Track Your Progress", "See your growth daily.\nStay on track."));
        slideList.add(new OnboardingItem(R.drawable.stay_motivated, "Stay Motivated", "Boost your drive every day.\nStay focused, stay strong."));

        OnboadingAdapter adapter = new OnboadingAdapter(slideList);
        viewPager2.setAdapter(adapter);

        setupDots();
        setCurrentDot(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
                if (position==slideList.size()-1){
                    next.setTextColor(Color.BLACK);
                    next.setText("Finish");
                } else {
                    next.setTextColor(Color.GRAY);
                    next.setText("Next");
                }
            }
        });

        next.setOnClickListener(v -> {
            int pos = viewPager2.getCurrentItem();
            if (pos < slideList.size() - 1) {
                viewPager2.setCurrentItem(pos + 1);
            } else {
                startActivity(new Intent(this, SignUpLoginScreenActivity.class));
                finish();
            }
        });

        skip.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpLoginScreenActivity.class));
            finish();
        });

    }

    private void setupDots() {
        ImageView[] dots = new ImageView[slideList.size()];
        layoutDots.removeAllViews();
        layoutDots.setGravity(Gravity.START);
        layoutDots.setGravity(Gravity.CENTER);

        for (int i = 0; i < slideList.size(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_inactive));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            layoutDots.addView(dots[i], params);
        }
    }

    private void setCurrentDot(int index) {
        int count = layoutDots.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView dot = (ImageView) layoutDots.getChildAt(i);
            dot.setImageDrawable(ContextCompat.getDrawable(this, i == index ? R.drawable.dot_active : R.drawable.dot_inactive));
        }
    }
}
