package org.kidinov.mvp_test.test.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.kidinov.mvp_test.data.model.Name;
import org.kidinov.mvp_test.data.model.Profile;
import org.kidinov.mvp_test.data.model.Ribot;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {
    private static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static Ribot makeRibot(String uniqueSuffix) {
        return new Ribot(makeProfile(uniqueSuffix));
    }

    public static List<Ribot> makeListRibots(int number) {
        List<Ribot> ribots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ribots.add(makeRibot(String.valueOf(i)));
        }
        return ribots;
    }

    private static Profile makeProfile(String uniqueSuffix) {
        Profile.Builder builder = new Profile.Builder();
        return builder
                .setName(makeName(uniqueSuffix))
                .setEmail("email" + uniqueSuffix + "@ribot.co.uk")
                .setDateOfBirth(new Date())
                .setHexColor("#0066FF")
                .setAvatar("http://api.io/images/" + uniqueSuffix)
                .setBio(randomUuid())
                .build();
    }

    private static Name makeName(String uniqueSuffix) {
        return new Name("Name-" + uniqueSuffix, "Surname-" + uniqueSuffix);
    }

}