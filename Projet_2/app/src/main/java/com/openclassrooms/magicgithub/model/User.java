package com.openclassrooms.magicgithub.model;

import java.util.Objects;
import java.util.Random;
import androidx.annotation.Nullable;
import static com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS_RANDOM;

public class User {

    private static int countId = 1;
    private int id;
    private String login;
    private String avatarUrl;

    public User(String login, String avatarUrl) {
        this.id = countId;
        this.login = login;
        this.avatarUrl = avatarUrl;
        countId++;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getAvatarUrl() { return avatarUrl; }

    /**
     * Generate random user
     */
    public static User random(){
        User newUserRandom = FAKE_USERS_RANDOM.get(new Random().nextInt(FAKE_USERS_RANDOM.size()));
        return new User(newUserRandom.login, newUserRandom.avatarUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        return (((User) obj).avatarUrl == this.avatarUrl && ((User) obj).login == this.login && ((User)obj).id == this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, avatarUrl);
    }
}