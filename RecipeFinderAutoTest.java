public class RecipeFinderAutoTest {

    public static void main(String[] args) {
        System.out.println("Running Automated Tests...\n");

        testLoadRecipeFile();
        testMatchingRecipeFound();
        testNoMatchingRecipe();
        testEmptyIngredientInput();
    }

    public static void testLoadRecipeFile() {
        DecisionTreeRecipeFinder.RecipeList recipes = DecisionTreeRecipeFinder.loadRecipes("recipes_250.csv");
        if (recipes != null && recipes.size() > 0) {
            System.out.println("✅ Test Load Recipe File: PASSED");
        } else {
            System.out.println("❌ Test Load Recipe File: FAILED");
        }
    }

    public static void testMatchingRecipeFound() {
        String[] ingredients = {"cheese", "tomato", "bread"};
        DecisionTreeRecipeFinder.RecipeList recipes = DecisionTreeRecipeFinder.loadRecipes("recipes_250.csv");

        boolean matchFound = false;
        for (int i = 0; i < recipes.size(); i++) {
            DecisionTreeRecipeFinder.Recipe recipe = recipes.get(i);
            int match = 0;
            for (String ing : ingredients) {
                if (recipe.hasIngredient(ing)) {
                    match++;
                }
            }
            if (match >= 2) {
                matchFound = true;
                break;
            }
        }

        if (matchFound) {
            System.out.println("✅ Test Matching Recipe Found: PASSED");
        } else {
            System.out.println("❌ Test Matching Recipe Found: FAILED");
        }
    }

    public static void testNoMatchingRecipe() {
        String[] ingredients = {"xylophone", "dragonfruit"};
        DecisionTreeRecipeFinder.RecipeList recipes = DecisionTreeRecipeFinder.loadRecipes("recipes_250.csv");

        boolean matchFound = false;
        for (int i = 0; i < recipes.size(); i++) {
            DecisionTreeRecipeFinder.Recipe recipe = recipes.get(i);
            int match = 0;
            for (String ing : ingredients) {
                if (recipe.hasIngredient(ing)) {
                    match++;
                }
            }
            if (match >= 2) {
                matchFound = true;
                break;
            }
        }

        if (!matchFound) {
            System.out.println("✅ Test No Matching Recipe: PASSED");
        } else {
            System.out.println("❌ Test No Matching Recipe: FAILED");
        }
    }

    public static void testEmptyIngredientInput() {
        String[] ingredients = {};
        DecisionTreeRecipeFinder.RecipeList recipes = DecisionTreeRecipeFinder.loadRecipes("recipes_250.csv");

        boolean matchFound = false;
        for (int i = 0; i < recipes.size(); i++) {
            DecisionTreeRecipeFinder.Recipe recipe = recipes.get(i);
            int match = 0;
            for (String ing : ingredients) {
                if (recipe.hasIngredient(ing)) {
                    match++;
                }
            }
            if (match >= 2) {
                matchFound = true;
                break;
            }
        }

        if (!matchFound) {
            System.out.println("✅ Test Empty Ingredient Input: PASSED");
        } else {
            System.out.println("❌ Test Empty Ingredient Input: FAILED");
        }
    }
}
