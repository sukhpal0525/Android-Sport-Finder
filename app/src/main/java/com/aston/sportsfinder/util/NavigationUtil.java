package com.aston.sportsfinder.util;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationUtil {

    //Deselect bottom navigation highlighting
    public static void deselectBottomNavItems(BottomNavigationView bottomNavView) {
        for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
            bottomNavView.getMenu().getItem(i).setChecked(false);
        }
    }
}
