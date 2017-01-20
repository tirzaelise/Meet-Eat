package nl.mprog.meeteat;

import android.content.Context;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

class DatabaseHandler {
    private DatabaseReference database;

    void writeToDatabase(Dinner dinner) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").push().setValue(dinner);
    }

    /** Reads the database based on the user's input of his area and dinner preference */
    void readDatabase(String area, final ArrayList<Dinner> dinners, final DinnerAdapter adapter,
                      final ResultFragment fragment) {
        database = FirebaseDatabase.getInstance().getReference();
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

    void updateFreeSpaces(String dinnerId, final Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findId = database.child("dinners").orderByChild("id").equalTo(dinnerId);

        findId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    Dinner dinner = snapshot.getValue(Dinner.class);
                    String guestString = dinner.getGuests().toString();
                    ArrayList<String> guests = dinner.getGuests();
                    int freeSpaces = StringUtils.countMatches(guestString, "null");

                    if (freeSpaces > 0) {
                        updateGuests(guests, dinner, databaseKey);
                        Toast.makeText(context, "Joined dinner", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "There are no more free spaces for this dinner",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to join dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Find the user's name using his user ID */
    private void updateGuests(final ArrayList<String> guests, final Dinner dinner, final String
            databaseKey) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findName = database.child("users").orderByChild("userId").equalTo(userId);

        findName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String username = user.getUsername();
                    updateFirebaseGuests(guests, username, dinner, databaseKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /** Searches for the first "null" occurrence in the guests array and replaces this with the
     * guest's name */
    private void updateFirebaseGuests(ArrayList<String> guests, String name, Dinner dinner,
                                      String databaseKey) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).equals("null")) {
                guests.set(i, name);
                break;
            }
            dinner.setGuests(guests);
            database.child("dinners").child(databaseKey).setValue(dinner);
        }
    }

    /** Returns an ArrayList of the dinners the user is hosting */
    ArrayList<Dinner> getHostingDinners() {
        ArrayList<Dinner> hostingDinners = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findHosting = database.child("dinners").orderByChild("host").equalTo(user.getUid());

        findHosting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.wtf()
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return hostingDinners;
    }

    /** Saves a user in Firebase under his user ID */
    void saveUser(User user) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getUserId()).setValue(user);
    }
}
