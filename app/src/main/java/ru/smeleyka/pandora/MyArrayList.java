package ru.smeleyka.pandora;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by admin on 12.02.2018.
 */

public class MyArrayList extends ArrayList implements Set {

    public MyArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public MyArrayList() {
    }

    public MyArrayList(@NonNull Collection c) {
        super(c);
    }
}

