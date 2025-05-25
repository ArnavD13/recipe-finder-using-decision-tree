import java.util.*;
import java.io.*;

// Custom library: recipefinder (RecipeNode & RecipeTree)

class RecipeNode {
    public String ingredient;
    public Map<String, RecipeNode> nextIngredients;
    public String recipe;

    public RecipeNode(String ingredient) {
        this.ingredient = ingredient;
        this.nextIngredients = new HashMap<>();
        this.recipe = null;
    }
}

class RecipeTree {
    public RecipeNode root;
    private Map<String, List<String>> reverseMap;
    private Map<String, List<String>> recipeToIngredientsMap;

    public RecipeTree() {
        root = new RecipeNode("Start");
        reverseMap = new HashMap<>();
        recipeToIngredientsMap = new HashMap<>();
        loadRecipes("recipes_250.csv");
    }

    private void addToReverseMap(String ingredient, String recipe) {
        if (!reverseMap.containsKey(ingredient)) {
            reverseMap.put(ingredient, new ArrayList<>());
        }
        if (!reverseMap.get(ingredient).contains(recipe)) {
            reverseMap.get(ingredient).add(recipe);
        }
    }

    private void addToRecipeIngredientMap(String recipe, String ingredient) {
        if (!recipeToIngredientsMap.containsKey(recipe)) {
            recipeToIngredientsMap.put(recipe, new ArrayList<>());
        }
        if (!recipeToIngredientsMap.get(recipe).contains(ingredient)) {
            recipeToIngredientsMap.get(recipe).add(ingredient);
        }
    }

    private void addRecipe(String[] ingredients, String recipe) {
        RecipeNode current = root;
        for (String ing : ingredients) {
            ing = ing.toLowerCase().trim();
            if (!current.nextIngredients.containsKey(ing)) {
                current.nextIngredients.put(ing, new RecipeNode(ing));
            }
            current = current.nextIngredients.get(ing);
            addToReverseMap(ing, recipe);
            addToRecipeIngredientMap(recipe, ing);
        }
        current.recipe = recipe;
    }

    public void loadRecipes(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String[] ingredients = Arrays.copyOf(parts, parts.length - 1);
                String recipeName = parts[parts.length - 1].trim();
                addRecipe(ingredients, recipeName);
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Error reading recipe file: " + e.getMessage());
        }
    }

    public void searchByIngredients(Set<String> ingredients) {
        Set<String> matchingRecipes = new HashSet<>();

        for (String ingredient : ingredients) {
            if (reverseMap.containsKey(ingredient)) {
                matchingRecipes.addAll(reverseMap.get(ingredient));
            }
        }

        Iterator<String> it = matchingRecipes.iterator();
        while (it.hasNext()) {
            String recipe = it.next();
            List<String> recipeIngredients = recipeToIngredientsMap.get(recipe);
            if (!ingredients.containsAll(recipeIngredients)) {
                it.remove();
            }
        }

        if (matchingRecipes.isEmpty()) {
            System.out.println("No recipe matches all those ingredients.");
        } else {
            System.out.println("Recipes with all specified ingredients:");
            for (String r : matchingRecipes) {
                System.out.println("- " + r);
            }
        }
    }

    public void ingredientsByRecipe(String recipeName) {
        if (recipeToIngredientsMap.containsKey(recipeName)) {
            System.out.println("Ingredients for recipe '" + recipeName + "':");
            for (String ing : recipeToIngredientsMap.get(recipeName)) {
                System.out.println("- " + ing);
            }
        } else {
            System.out.println("No such recipe found: " + recipeName);
        }
    }
}

public class RecipeFinder {
    public static void findRecipe(RecipeNode node, Scanner scanner, RecipeTree tree) {
        Set<String> userIngredients = new HashSet<>();

        while (true) {
            System.out.println("Enter an ingredient (or type 'done' to search, 'reverse' for recipe search, 'ingredients' to get ingredients from a recipe):");
            String input = scanner.next().toLowerCase();

            if (input.equals("done")) {
                tree.searchByIngredients(userIngredients);
                return;
            } else if (input.equals("reverse")) {
                System.out.println("Enter a single ingredient to find recipes:");
                String ing = scanner.next().toLowerCase();
                tree.searchByIngredients(Collections.singleton(ing));
                return;
            } else if (input.equals("ingredients")) {
                System.out.println("Enter the name of the recipe:");
                scanner.nextLine(); // consume newline
                String recipeName = scanner.nextLine().trim();
                tree.ingredientsByRecipe(recipeName);
                return;
            }

            userIngredients.add(input);

            if (node.nextIngredients.containsKey(input)) {
                node = node.nextIngredients.get(input);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RecipeTree tree = new RecipeTree();
        System.out.println("Welcome to the Ingredient-Based Recipe Finder!");
        findRecipe(tree.root, scanner, tree);
        scanner.close();
    }
}