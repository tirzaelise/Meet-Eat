/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class handles the database actions. This includes reading the database, writing new dinners
 * to the database, retrieving specific data from the database (such as dinners hosted by the
 * current user or dinners joined by the current user), deleting dinners from the database, updating
 * dates, titles and ingredients of dinners, updating the amount of free spaces for dinners and
 * saving users to the database when they create an account. The database is a Firebase database.
 */

package nl.mprog.meeteat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

class DatabaseHandler {

    /** Writes a new dinner to the database. */
    void writeToDatabase(Dinner dinner) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").push().setValue(dinner);
    }

    /** Reads the database based on the user's input area. */
    void readDatabase(String area, final ArrayList<Dinner> dinners, final DinnerAdapter adapter,
                      final DinnerResultFragment fragment, final View view) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query areaQuery = database.child("dinners").orderByChild("area").equalTo(area);

        areaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                replaceTextInView(dataSnapshot.getChildrenCount(), view);

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Dinner dinner = snapshot.getValue(Dinner.class);
                    dinners.add(dinner);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(fragment.getActivity(), R.string.dbReadFail, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /** Replaces the loading text with no results if necessary. */
    private void replaceTextInView(long amountResults, View view) {
        TextView loadingText = (TextView) view.findViewById(R.id.loading);

        if (amountResults != 0) {
            loadingText.setVisibility(View.INVISIBLE);
        } else {
            loadingText.setText(R.string.noResults);
        }
    }

    /**
     * The push key of the dinner is retrieved and the list of guests is updated if the user who
     * wants to join the dinner is not the host of the dinner.
     */
    void updateFreeSpaces(final Context context, final Dinner dinner, final int position,
                          final DinnerAdapter adapter, final ArrayList<Dinner> dinners,
                          final int amountJoining) {
        String dinnerId = dinner.getId();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findKey = database.child("dinners").orderByChild("id").equalTo(dinnerId);

        findKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    ArrayList<String> userInfo = getUserInfo(context);

                    if (!userInfo.get(0).equals(dinner.getHostId())) {
                        updateFirebaseGuests(userInfo, databaseKey, position, adapter, dinners,
                                context, amountJoining);
                    } else {
                        Toast.makeText(context, R.string.notJoinOwnDinner, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, R.string.failedJoin, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Retrieves the user's ID and name.
     */
    private ArrayList<String> getUserInfo(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "");
        String userId = sharedPrefs.getString("userId", "");
        return new ArrayList<>(Arrays.asList(userId, username));
    }

    /**
     * Searches for the first "null" occurrence in the guests array and replaces this with the
     * guest's name. After that, the Dinner entry in the database is updated.
     */
    private void updateFirebaseGuests(ArrayList<String> userInfo, String databaseKey, int position,
                                      DinnerAdapter adapter, ArrayList<Dinner> dinners,
                                      Context context, int amountJoining) {
        Dinner dinner = dinners.get(position);
        ArrayList<String> guestNames = dinner.getGuestNames();
        ArrayList<String> guestIds = dinner.getGuestIds();

        guestIds = updateGuestList(userInfo.get(0), guestIds, amountJoining);
        guestNames = updateGuestList(userInfo.get(1), guestNames, amountJoining);

        dinner.setGuestIds(guestIds);
        dinner.setGuestNames(guestNames);
        dinners.set(position, dinner);
        adapter.notifyDataSetChanged();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").child(databaseKey).setValue(dinner);
        getHostEmail(userInfo.get(1), dinner, context);

        Toast.makeText(context, R.string.successJoin, Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the first occurrence of "null" in an array with the new guest name or ID for the
     * amount of people that the user specified they want to join the dinner with.
     */
    private ArrayList<String> updateGuestList(String newGuest, ArrayList<String> guestList,
                                              int amountJoining) {

        for (int i = 0; i < amountJoining; i++) {
            int firstFreeSpace = guestList.indexOf("null");
            guestList.set(firstFreeSpace, newGuest);
        }
        return guestList;
    }

    /** Finds the e-mail address of the host of the dinner and sends them an e-mail if someone
     * has joined their dinner. */
    private void getHostEmail(final String username, final Dinner dinner, final Context context) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users")
                .child(dinner.getHostId());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User host = dataSnapshot.getValue(User.class);
                String hostMail = host.getEmail();
                String email = joinEmail(username, dinner, context);
                sendEmail(email, hostMail, context.getResources().getString(R.string.notifyHost),
                        context.getResources().getString(R.string.successJoin), context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, R.string.failHostEmail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Sends an e-mail to the host of the dinner that will be joined to ask for details. */
    private void sendEmail(String body, String recipientEmail, String title, String subject,
                           Context context) {
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setType("message/rfc822");
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            context.startActivity(Intent.createChooser(mailIntent, title));
        } catch (android.content.ActivityNotFoundException e){
            Toast.makeText(context, R.string.noEmailClients, Toast.LENGTH_SHORT).show();
        }
    }

    /** Returns the predefined e-mail string to join a dinner.  */
    private String joinEmail(String username, Dinner dinner, Context context) {
        Resources resources = context.getResources();

        return String.format(resources.getString(R.string.joinEmail), dinner.getTitle(),
                dinner.getDate(), username);
    }

    /** Retrieves the dinners that the user is hosting from Firebase. */
    void getHostingDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                           final Activity activity, final View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findHosting = database.child("dinners").orderByChild("hostId").equalTo(userId);

            findHosting.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    replaceTextInView(dataSnapshot.getChildrenCount(), view);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dinner dinner = snapshot.getValue(Dinner.class);
                        dinners.add(dinner);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, R.string.failedRetrieveDinners, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    /** Retrieves the dinners that the user has joined from Firebase. */
    void getJoinedDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                          final Activity activity, final View view) {
        final FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findJoined = database.child("dinners").orderByChild("guestIds");

            findJoined.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Dinner dinner = snapshot.getValue(Dinner.class);
                        ArrayList<String> guestIds = dinner.getGuestIds();

                        if (guestIds.contains(userId)) {
                            dinners.add(dinner);
                            adapter.notifyDataSetChanged();
                        }
                        replaceTextInView(dinners.size(), view);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, R.string.failedRetrieveDinners, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    /** Saves a user in Firebase under his user ID. */
    void saveUser(User user, Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getUserId()).setValue(user);

        SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE).edit();
        editor.putString("userId", user.getUserId());
        editor.putString("username", user.getUsername());
        editor.putString("userEmail", user.getEmail()).apply();
    }

    /** Saves the user's name and ID in SharedPreferences. */
    void getUsername(final String userId, final Context context) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findName = database.child("users").orderByChild("userId").equalTo(userId);

        findName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String username = user.getUsername();

                    SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                            Context.MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putString("userId", userId).apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, R.string.failedSaveName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Updates a dinner in the database. */
    void updateDinner(final Dinner dinner, final Activity activity) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query updateDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        updateDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    database.child("dinners").child(databaseKey).setValue(dinner);
                    Toast.makeText(activity, R.string.successUpdate, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, R.string.failUpdate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Finds a dinner in the database according to its ID and removes the current user from the
     * list of guests.
     */
    void removeGuest(final Dinner dinner, final Activity activity, final ArrayList<Dinner> dinners,
                    final SavedAdapter adapter, final int position) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        findDinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    removeUser(databaseKey, dinners, adapter, position, activity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, R.string.failFind, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Removes the current user from a dinner in the database. */
    private void removeUser(String key, ArrayList<Dinner> dinners, SavedAdapter adapter,
                            int position, Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Dinner dinner = dinners.get(position);

            ArrayList<String> guestIds = dinner.getGuestIds();
            String userId = user.getUid();
            int lastOccurrence = guestIds.lastIndexOf(userId);

            if (lastOccurrence != -1) {
                dinner = removeFromDinner(dinner, lastOccurrence);
                adapter.notifyDataSetChanged();
                adapter.setData(dinners);

                database.child("dinners").child(key).setValue(dinner);
                String email = unjoinEmail(dinner, activity);
                Resources resources = activity.getResources();

                sendEmail(email, dinner.getHostEmail(), resources.getString(R.string.notifyHost),
                        resources.getString(R.string.removeGuest), activity);
            } else {
                Toast.makeText(activity, R.string.removedAlready, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Removes the user's name and ID from the guest list. */
    private Dinner removeFromDinner(Dinner dinner, int lastOccurrence) {
        ArrayList<String> guestIds = dinner.getGuestIds();
        ArrayList<String> guestNames = dinner.getGuestNames();

        guestIds.set(lastOccurrence, "null");
        guestNames.set(lastOccurrence, "null");
        return dinner;
    }

    /** Body of an email when a user is no longer joining a dinner. */
    private String unjoinEmail(Dinner dinner, Activity activity) {
        Resources resources = activity.getResources();
        String name = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("username", "");

        return String.format(resources.getString(R.string.unjoinEmail), dinner.getTitle(),
                dinner.getDate(), name);
    }

    /** Removes a dinner from the database. */
    void deleteDinner(final Dinner dinner, final Activity activity, final SavedAdapter adapter,
                              final ArrayList<Dinner> dinners) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        findDinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    emailGuests(dinner, activity);
                    database.child("dinners").child(databaseKey).removeValue();
                    dinners.remove(dinner);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, R.string.failDelete, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Notifies the guests of that the host has cancelled their dinner. */
    private void emailGuests(Dinner dinner, Activity activity) {
        HashSet<String> uniqueGuests = new HashSet<>(dinner.getGuestIds());
        uniqueGuests.remove("null");
        ArrayList<String> uniqueArray = new ArrayList<>(uniqueGuests);

        for (int i = 0; i < uniqueArray.size(); i++) {
            String guestId = uniqueArray.get(i);
            findGuestEmail(guestId, dinner, activity);
        }
    }

    /** Finds the email of a user given their ID and sends them an e-mail. */
    private void findGuestEmail(String userId, final Dinner dinner, final Activity activity) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String email = user.getEmail();
                String body = cancelMail(dinner, activity);

                Resources resources = activity.getResources();
                sendEmail(body, email, resources.getString(R.string.notifyGuest),
                        resources.getString(R.string.cancelDinner), activity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, R.string.failFindEmailGuest, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Body of an email when the host cancels the dinner. */
    private String cancelMail(Dinner dinner, Activity activity) {
        Resources resources = activity.getResources();
        String name = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("username", "");

        return String.format(resources.getString(R.string.cancelEmail), dinner.getTitle(),
                dinner.getDate(), name);
    }
}
