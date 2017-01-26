package nl.mprog.meeteat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

class DatabaseHandler {

    /** Writes a new dinner to the database. */
    void writeToDatabase(Dinner dinner) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").push().setValue(dinner);
    }

    /** Reads the database based on the user's input area. */
    void readDatabase(String area, final ArrayList<Dinner> dinners, final DinnerAdapter adapter,
                      final ResultFragment fragment) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query areaQuery = database.child("dinners").orderByChild("area").equalTo(area);

        areaQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dinner dinner = dataSnapshot.getValue(Dinner.class);
                dinners.add(dinner);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Dinner dinner = dataSnapshot.getValue(Dinner.class);
                dinners.remove(dinner);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(fragment.getActivity(), "Failed to read database",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * The push key of the dinner is retrieved and the list of guests is updated.
     */
    void updateFreeSpaces(final Context context, final Dinner dinner, final int position,
                          final DinnerAdapter adapter, final ArrayList<Dinner> dinners) {
        String dinnerId = dinner.getId();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findKey = database.child("dinners").orderByChild("id").equalTo(dinnerId);

        findKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    ArrayList<String> userInfo = getUserInfo(context);
                    updateFirebaseGuests(userInfo, databaseKey, position, adapter, dinners,
                            context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to join dinner", Toast.LENGTH_SHORT).show();
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
                                      Context context) {
        Dinner dinner = dinners.get(position);
        ArrayList<String> guestNames = dinner.getGuestNames();
        ArrayList<String> guestIds = dinner.getGuestIds();

        guestIds = updateGuestList(userInfo.get(0), guestIds);
        guestNames = updateGuestList(userInfo.get(1), guestNames);

        dinner.setGuestIds(guestIds);
        dinner.setGuestNames(guestNames);
        dinners.set(position, dinner);
        adapter.notifyDataSetChanged();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").child(databaseKey).setValue(dinner);
        getHostEmail(userInfo.get(1), dinner, context);

        Toast.makeText(context, "Joined dinner", Toast.LENGTH_SHORT).show();
    }

    /** Update the first occurrence of "null" in an array with the new guest name or ID. */
    private ArrayList<String> updateGuestList(String newGuest, ArrayList<String> guestList) {
        for (int i = 0; i < guestList.size(); i++) {
            if (guestList.get(i).equals("null")) {
                guestList.set(i, newGuest);
                break;
            }
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
                String email = host.getEmail();
                Log.wtf("host", email);
                sendJoinedEmail(username, email, dinner, context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Could not retrieve host's e-mail address",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Sends an e-mail to the host of the dinner that will be joined to ask for details. */
    private void sendJoinedEmail(String username, String hostEmail, Dinner dinner,
                                 Context context) {
        String body = joinEmail(username, dinner);

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setType("message/rfc822");
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{hostEmail});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Joined Dinner");
        mailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            context.startActivity(Intent.createChooser(mailIntent, "Send mail"));
        } catch (android.content.ActivityNotFoundException e){
            Toast.makeText(context, "There are no e-mail clients installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Returns the predefined e-mail string to join a dinner.  */
    private String joinEmail(String username, Dinner dinner) {
        return "Hello!\n\n I have joined your dinner at " + dinner.getDate() + ". Please " +
                "contact me for details, such as your address.\n Thank you in advance.\n\n" +
                " Kind regards, \n" + username;
    }

    /** Retrieves the dinners that the user is hosting from Firebase. */
    void getHostingDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                           final Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findHosting = database.child("dinners").orderByChild("hostId").equalTo(userId);

            findHosting.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Dinner dinner = snapshot.getValue(Dinner.class);
                        dinners.add(dinner);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, "Could not retrieve dinners",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Please log in to view this page",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Retrieves the dinners that the user has joined from Firebase. */
    void getJoinedDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                          final Activity activity) {
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

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

                        if (isInArray(userId, guestIds)) {
                            dinners.add(dinner);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, "Could not retrieve joined dinners",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /** Checks if a certain value is in an array. */
    private boolean isInArray(String value, ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    /** Saves a user in Firebase under his user ID. */
    void saveUser(User user, Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getUserId()).setValue(user);

        SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE).edit();
        editor.putString("userId", user.getUserId());
        editor.putString("username", user.getUsername()).apply();
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
                Toast.makeText(context, "Could not save name", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "Updated dinner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Failed to update dinner", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Could not find dinner", Toast.LENGTH_SHORT).show();
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
            ArrayList<String> guestNames = dinner.getGuestNames();
            String userId = user.getUid();
            int lastOccurrence = guestIds.lastIndexOf(userId);

            if (lastOccurrence != -1) {
                guestIds.set(lastOccurrence, "null");
                guestNames.set(lastOccurrence, "null");
                dinner.setGuestIds(guestIds);
                dinner.setGuestNames(guestNames);

                dinners.set(position, dinner);
                adapter.notifyDataSetChanged();
                adapter.setData(dinners);

                database.child("dinners").child(key).setValue(dinner);
            } else {
                Toast.makeText(activity, "You have already been removed from this dinner",
                        Toast.LENGTH_SHORT).show();
            }
        }
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
                    database.child("dinners").child(databaseKey).removeValue();
                    dinners.remove(dinner);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Could not remove dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
