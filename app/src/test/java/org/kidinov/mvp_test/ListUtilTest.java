package org.kidinov.mvp_test;


import org.junit.Test;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.util.ListUtil;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ListUtilTest {
    @Test
    public void containsAllReturnTrueIfLeftListContainsRight() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(15).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();

        assertTrue(ListUtil.containsAll(leftList, rightList));
    }

    @Test
    public void containsAllReturnFalseIfLeftListNotContainsRight() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeedWithSeqIds(13).getInstaItems();

        assertFalse(ListUtil.containsAll(leftList, rightList));
    }

    @Test
    public void containsAllReturnFalseIfListsAreDifferent() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeed("", 13, 1).getInstaItems();

        assertFalse(ListUtil.containsAll(leftList, rightList));
    }

    @Test
    public void compareInstaItemListsReturnsTrueIfListsAreEqual() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();

        assertTrue(ListUtil.compareInstaItemLists(leftList, rightList));
    }

    @Test
    public void compareInstaItemListsReturnsFalseIfListsAreDifferentSize() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeedWithSeqIds(11).getInstaItems();

        assertFalse(ListUtil.compareInstaItemLists(leftList, rightList));
    }

    @Test
    public void compareInstaItemListsReturnsFalseIfListsAreNotEqual() {
        List<InstaItem> leftList = TestDataFactory.makeInstaFeedWithSeqIds(10).getInstaItems();
        List<InstaItem> rightList = TestDataFactory.makeInstaFeed("", 10, 1).getInstaItems();

        assertFalse(ListUtil.compareInstaItemLists(leftList, rightList));
    }
}
