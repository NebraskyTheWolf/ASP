package com.fluffcord.app.utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.ContextThemeWrapper;

import com.discord.views.CheckedSetting;
import com.fluffcord.app.FluffCord;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

public class LayoutUtils {
    public static final int divider = FluffCord.getResourceId("@style/UiKit.Settings.Divider");
    public static final int header = FluffCord.getResourceId("@style/UiKit.Settings.Item.Header");

    public static View addDivider(ViewGroup viewGroup) {
        View dividerView = new View(new ContextThemeWrapper(viewGroup.getContext(), divider));
        viewGroup.addView(dividerView);
        return dividerView;
    }

    public static MaterialTextView addHeader(ViewGroup viewGroup, String title) {
        MaterialTextView headerView = new MaterialTextView(
                new ContextThemeWrapper(viewGroup.getContext(), header));
        headerView.setText(title);
        viewGroup.addView(headerView);
        return headerView;
    }

    public static CheckedSetting addCheckedButton(ViewGroup viewGroup,String title,String desc) {
        CheckedSetting checkBoxView = new CheckedSetting(
                new ContextThemeWrapper(viewGroup.getContext(), header), null);

        viewGroup.addView(checkBoxView);
        return checkBoxView;
    }
}
