package nl.mprog.meeteat;


class User {
    private String userId;
    private String username;
    private String email;

    User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public User() {

    }

    String getUserId() {
        return userId;
    }

    String getUsername() {
        return username;
    }

    String getEmail() {
        return email;
    }
}
