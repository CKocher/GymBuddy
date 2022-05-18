package com.example.gymbuddy.contract;

import com.airbnb.lottie.LottieAnimationView;

public interface SplashScreenContract {
    // View
    interface View {
        void initializeViews();
        void setupLottieAnimation(LottieAnimationView view, String animationName);
        void launchMainActivity();
        void setupSplashHandler();
    }
}
