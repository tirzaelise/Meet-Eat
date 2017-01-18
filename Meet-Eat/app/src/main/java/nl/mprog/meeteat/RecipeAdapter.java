package nl.mprog.meeteat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tirza on 17-1-17.
 */

public class RecipeAdapter extends BaseAdapter {
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
            String vegetarian = booleanToString(recipe.isVegetarian(), "Vegetarian");
            String vegan = booleanToString(recipe.isVegan(), "Vegan");

            recipeTitle.setText(recipe.getTitle());
            recipeIngredients.setText(ingredients);
            recipeVegetarian.setText(vegetarian);
            recipeVegan.setText(vegan);
            String url = "https://spoonacular.com/recipeImages/" + recipe.getId() + "-312x231.jpg";

            Picasso.with(context).load(url).into(recipeImage);

        } else {
            initialiseViewsAndRecipe(rowView, position);
            String ingredients = "Ingredients: " + this.recipes.get(position).getIngredients();
            boolean isVegetarian = this.recipes.get(position).isVegetarian();
            boolean isVegan = this.recipes.get(position).isVegan();
            String vegetarian = booleanToString(isVegetarian, "Vegetarian");
            String vegan = booleanToString(isVegan, "Vegan");

            recipeTitle.setText(this.recipes.get(position).getTitle());
            recipeIngredients.setText(ingredients);
            recipeVegetarian.setText(vegetarian);
            recipeVegan.setText(vegan);
            String url = "https://spoonacular.com/recipeImages/" +
                    this.recipes.get(position).getId() + "-312x231.jpg";
            Picasso.with(context).load(url).into(recipeImage);

        }
        Button makeDinner = ((Button) rowView.findViewById(R.id.makeButton));
        setClickListener(makeDinner);

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

    /** Converts a boolean to a string that can be used in a TextView */
    private String booleanToString(boolean isTrue, String text){
        if (isTrue) {
            text = text + ": yes";
        } else {
            text = text + ": no";
        }
        return text;
    }

    /** Sets a listener on the make dinner button */
    private void setClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.writeToDatabase(recipe);
                Toast.makeText(context, "Added dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
