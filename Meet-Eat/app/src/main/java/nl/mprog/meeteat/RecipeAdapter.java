package nl.mprog.meeteat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
 * Created by tirza on 17-1-17.
 */

class RecipeAdapter extends BaseAdapter {
    private ArrayList<Dinner> recipes;
    private Context context;
    private LayoutInflater inflater;
    private Dinner recipe;
    private TextView recipeTitle;
    private TextView recipeIngredients;
    private TextView recipeVegetarian;
    private TextView recipeVegan;
    private ImageView recipeImage;

    RecipeAdapter(Context context, ArrayList<Dinner> recipes) {
        this.context = context;
        this.recipes = recipes;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.layout_recipe_row, parent, false);
            initialiseViewsAndRecipe(rowView, position);
            String ingredients = "Ingredients: " + recipe.getIngredients();

            recipeTitle.setText(recipe.getTitle());
            recipeIngredients.setText(ingredients);
            booleanToVisibility(recipe.isVegetarian(), recipeVegetarian);
            booleanToVisibility(recipe.isVegan(), recipeVegan);
            String url = "https://spoonacular.com/recipeImages/" + recipe.getId() + "-312x231.jpg";

            Picasso.with(context).load(url).into(recipeImage);

        } else {
            initialiseViewsAndRecipe(rowView, position);
            String ingredients = "Ingredients: " + this.recipes.get(position).getIngredients();
            boolean isVegetarian = this.recipes.get(position).isVegetarian();
            boolean isVegan = this.recipes.get(position).isVegan();

            recipeTitle.setText(recipes.get(position).getTitle());
            recipeIngredients.setText(ingredients);
            booleanToVisibility(isVegetarian, recipeVegetarian);
            booleanToVisibility(isVegan, recipeVegan);
            String url = "https://spoonacular.com/recipeImages/" +
                    this.recipes.get(position).getId() + "-312x231.jpg";
            Picasso.with(context).load(url).into(recipeImage);
        }

        Button makeDinner = ((Button) rowView.findViewById(R.id.makeButton));
        setClickListener(makeDinner, position);

        return rowView;
    }

    /** Initialises the views in the row layout and a value for the recipe */
    private void initialiseViewsAndRecipe(View view, int position) {
        recipe = recipes.get(position);
        recipeTitle = (TextView) view.findViewById(R.id.recipeTitle);
        recipeIngredients = (TextView) view.findViewById(R.id.recipeIngredients);
        recipeVegetarian = (TextView) view.findViewById(R.id.recipeVegetarian);
        recipeVegan = (TextView) view.findViewById(R.id.recipeVegan);
        recipeImage = (ImageView) view.findViewById(R.id.recipeImage);
    }

    /** Determines the visibility of a TextView according to a boolean */
    private void booleanToVisibility(boolean isTrue, TextView textView){
        if (isTrue) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    /** Adds a dinner to the Firebase database if the user is logged in */
    private void setClickListener(Button button, final int position) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Dinner clickedRecipe = recipes.get(position);
                    DatabaseHandler databaseHandler = new DatabaseHandler();
                    databaseHandler.writeToDatabase(clickedRecipe);
                    Toast.makeText(context, "Added dinner", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please log in to add a dinner", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
