package nl.mprog.meeteat;


class User {
    private String userId;
    private String username;

    User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public User() {

    }

    String getUserId() {
        return userId;
    }

    String getUsername() {
        return username;
    }
}
