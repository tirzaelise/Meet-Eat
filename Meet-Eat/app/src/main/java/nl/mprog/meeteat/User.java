/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the User object. This object consists of an ID, a name and an e-mail
 * address. This object is used to keep track of the user's information when they create an account
 * and is added to Firebase.
 */

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
