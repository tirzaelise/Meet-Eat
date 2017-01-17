package nl.mprog.meeteat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class RecipeResultFragment extends Fragment implements DinnerAsyncTask.OnTaskCompleted,
        InfoAsyncTask.OnInfoRetrieved {
    private String food;
    private String host;
    private String startTime;
    private int freeSpaces;
    private String area;
    private DinnerAdapter adapter;
    private ArrayList<Dinner> dinners;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = getView();
        Bundle arguments = this.getArguments();

        if (arguments != null && rootView != null) {
            dinners = new ArrayList<>();
            getArguments(arguments);
            getDinnerInfo();
            showResult(rootView);
        }
    }

    private void getArguments(Bundle arguments) {
        food = arguments.getString("food");
        host = arguments.getString("host");
        startTime = arguments.getString("startTime");
        freeSpaces = arguments.getInt("freeSpaces");
        area = arguments.getString("area");
    }

    private void getDinnerInfo() {
        String query = "search?&query=" + food;
        DinnerAsyncTask dinnerAsyncTask = new DinnerAsyncTask(this, this, host, startTime,
                freeSpaces, area);
        dinnerAsyncTask.execute(query);
    }

    private void showResult(View rootView) {
        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
        adapter = new DinnerAdapter(this, dinners);
        listView.setAdapter(adapter);
    }

    @Override
    public void onTaskCompleted(ArrayList<Dinner> retrievedDinners) {
        for (int i = 0; i < retrievedDinners.size(); i++) {
            Dinner dinner = retrievedDinners.get(i);
            InfoAsyncTask infoAsyncTask = new InfoAsyncTask(this, dinner);
            String query =  dinner.getId() + "/information";
            infoAsyncTask.execute(query);
        }

    }

    @Override
    public void onInfoRetrieved(Dinner dinner) {
        dinners.add(dinner);
        adapter.notifyDataSetChanged();
    }
}
