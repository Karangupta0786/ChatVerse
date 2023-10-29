package com.android.chatverse;

import com.mesibo.api.MesiboProfile;

import java.util.ArrayList;

public interface SwipeActionListener {
    void onSwipe(int position, MesiboProfile profile,String type);

    void onSelectItem(int Position, ArrayList<MesiboProfile> profile);
}
