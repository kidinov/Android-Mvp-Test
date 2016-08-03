package org.kidinov.mvp_test.test.common;

import org.kidinov.mvp_test.data.model.Images;
import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.data.model.Location;
import org.kidinov.mvp_test.data.model.StandardResolution;

import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {
    private static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    private static String randomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private static Long randomLong() {
        Random random = new Random();
        return random.nextLong();
    }

    private static Integer randomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    public static InstaFeed makeInstaFeedWithSeqIds(int countOfItems) {
        InstaFeed instaFeed = new InstaFeed();
        instaFeed.setMoreAvailable(true);
        instaFeed.setInstaItems(makeInstaItemsWithSeqIds(countOfItems));
        return instaFeed;
    }

    private static RealmList<InstaItem> makeInstaItemsWithSeqIds(int countOfItems) {
        RealmList<InstaItem> instaItems = new RealmList<>();
        for (int i = 0; i < countOfItems; i++) {
            instaItems.add(makeInstaItem(i));
        }
        return instaItems;
    }

    private static InstaItem makeInstaItem(int id) {
        InstaItem item = new InstaItem();
        item.setLocation(new Location("_location_"));
        item.setCreatedTime(id * 1000000L );
        item.setImages(makeInstaImages());
        item.setId(String.valueOf(id));
        return item;
    }

    public static InstaFeed makeInstaFeed(String prefix, int countOfItems, int dateСoefficient) {
        InstaFeed instaFeed = new InstaFeed();
        instaFeed.setMoreAvailable(true);
        instaFeed.setInstaItems(makeInstaItems(prefix, countOfItems, dateСoefficient));
        return instaFeed;
    }

    private static RealmList<InstaItem> makeInstaItems(String prefix, int countOfItems, int dateСoefficient) {
        RealmList<InstaItem> instaItems = new RealmList<>();
        for (int i = 0; i < countOfItems; i++) {
            instaItems.add(makeInstaItem(prefix, i, dateСoefficient));
        }
        return instaItems;
    }

    private static InstaItem makeInstaItem(String prefix, int i, int dateСoefficient) {
        InstaItem item = new InstaItem();
        item.setLocation(new Location(prefix + "_location_" + i));
        item.setCreatedTime(i * 1000000L * dateСoefficient);
        item.setImages(makeInstaImages());
        item.setId(randomUuid());
        return item;
    }

    private static Images makeInstaImages() {
        Images images = new Images();
        images.setStandardResolution(makeInstaStandardResolution());
        return images;
    }

    private static StandardResolution makeInstaStandardResolution() {
        StandardResolution standardResolution = new StandardResolution();
        standardResolution.setHeight(randomInt());
        standardResolution.setUrl(randomString());
        standardResolution.setWidth(randomInt());
        return standardResolution;
    }

}