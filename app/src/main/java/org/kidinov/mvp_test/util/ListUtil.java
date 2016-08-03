package org.kidinov.mvp_test.util;


import android.support.annotation.NonNull;

import org.kidinov.mvp_test.data.model.InstaItem;

import java.util.List;

public class ListUtil {
    public static boolean containsAll(@NonNull List<InstaItem> list1, @NonNull List<InstaItem> list2) {
        boolean equal;
        for (InstaItem t : list2) {
            equal = false;
            for (InstaItem t1 : list1) {
                if (t.getId().equals(t1.getId())) {
                    equal = true;
                    break;
                }
            }
            if (!equal) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareInstaItemLists(@NonNull List<InstaItem> list1, @NonNull List<InstaItem> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        boolean equal;
        for (InstaItem t : list1) {
            equal = false;
            for (InstaItem t1 : list2) {
                if (t.getId().equals(t1.getId())) {
                    equal = true;
                    break;
                }
            }
            if (!equal) {
                return false;
            }
        }
        return true;
    }
}
