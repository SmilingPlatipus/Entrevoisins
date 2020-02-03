package com.openclassrooms.entrevoisins.utils;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class SelectViewAction implements ViewAction{

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "select an element on the list";
    }

    @Override
    public void perform(UiController uiController, View view) {
        view.performClick();
    }
}
