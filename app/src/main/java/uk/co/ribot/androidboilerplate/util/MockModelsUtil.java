package uk.co.ribot.androidboilerplate.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.co.ribot.androidboilerplate.data.model.Name;
import uk.co.ribot.androidboilerplate.data.model.Profile;
import uk.co.ribot.androidboilerplate.data.model.Ribot;

public class MockModelsUtil {

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static Ribot createRibot() {
        return new Ribot(createProfile());
    }

    public static List<Ribot> createListRibots(int number) {
        List<Ribot> ribots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ribots.add(createRibot());
        }
        return ribots;
    }

    public static Profile createProfile() {
        Profile profile = new Profile();
        profile.email = randomString();
        profile.name = createName();
        profile.dateOfBirth = new Date();
        profile.hexColor = "#0066FF";
        profile.avatar = "http://api.ribot.io/images/" + profile.email;
        profile.bio = randomString();
        return profile;
    }

    public static Name createName() {
        Name name = new Name();
        name.first = randomString();
        name.last = randomString();
        return name;
    }

}