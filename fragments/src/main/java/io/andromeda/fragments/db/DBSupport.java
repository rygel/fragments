package io.andromeda.fragments.db;

import io.andromeda.fragments.Fragment;
import io.andromeda.fragments.Fragments;

import java.util.List;

/**
 * Created by Alexander on 31.01.2017.
 */
public class DBSupport {
    private int DEFAULT_TOP_FRAGMENTS;

    public void createTable(Fragments fragments) {

    }

    public void addClick(Fragment fragment) {

    }

    public List<Fragment> getTopFragments() {
        return getTopFragments(DEFAULT_TOP_FRAGMENTS);
    }

    public List<Fragment> getTopFragments(int numberOfTopFragments) {
        return null;
    }
}
