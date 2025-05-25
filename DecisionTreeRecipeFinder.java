class DecisionTreeRecipeFinder {
    // Custom implementation of Recipe class
    static class Recipe {
        String name;
        String[] ingredients;
        int ingredientCount;

        Recipe(String name, int maxIngredients) {
            this.name = name;
            this.ingredients = new String[maxIngredients];
            this.ingredientCount = 0;
        }

        void addIngredient(String ingredient) {
            if (ingredientCount < ingredients.length) {
                ingredients[ingredientCount++] = ingredient.trim().toLowerCase();
            }
        }

        boolean hasIngredient(String ingredient) {
            for (int i = 0; i < ingredientCount; i++) {
                if (ingredients[i].equals(ingredient)) {
                    return true;
                }
            }
            return false;
        }
    }

    // Custom implementation of ArrayList for recipes
    static class RecipeList {
        Recipe[] recipes;
        int size;

        RecipeList(int capacity) {
            recipes = new Recipe[capacity];
            size = 0;
        }

        void add(Recipe recipe) {
            if (size < recipes.length) {
                recipes[size++] = recipe;
            }
        }

        Recipe get(int index) {
            return recipes[index];
        }

        int size() {
            return size;
        }
    }

    public static void main(String[] args) {
        RecipeList recipes = loadRecipes("recipes_250.csv");
        String[] givenIngredients = new String[100]; // Assuming max 100 ingredients
        int ingredientCount = 0;

        // Simple console input
        System.out.println("Enter one ingredient at a time. Type 'done' when finished:");
        while (true) {
            System.out.print("Ingredient: ");
            char[] input = new char[100];
            int charCount = 0;
            
            try {
                int c;
                while ((c = System.in.read()) != '\n' && c != -1 && charCount < 100) {
                    input[charCount++] = (char)c;
                }
            } catch (Exception e) {
                System.out.println("Error reading input");
                continue;
            }

            String ingredient = new String(input, 0, charCount).trim().toLowerCase();
            
            if (ingredient.equals("done")) break;
            if (ingredient.length() == 0) {
                System.out.println("Please enter a valid ingredient.");
                continue;
            }

            givenIngredients[ingredientCount++] = ingredient;
        }

        // Filter recipes with at least 2 matching ingredients
        RecipeList possibleRecipes = new RecipeList(recipes.size());
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            int matchCount = 0;
            
            for (int j = 0; j < ingredientCount; j++) {
                if (recipe.hasIngredient(givenIngredients[j])) {
                    matchCount++;
                }
            }

            if (matchCount >= 2) {
                possibleRecipes.add(recipe);
            }
        }

        // Display results
        if (possibleRecipes.size() == 0) {
            System.out.println("\nNo recipes found with at least 2 matching ingredients.");
        } else {
            System.out.println("\nBased on your ingredients, you can make:");
            for (int i = 0; i < possibleRecipes.size(); i++) {
                Recipe recipe = possibleRecipes.get(i);
                System.out.println("- " + recipe.name);
                System.out.print("  Ingredients: ");
                for (int j = 0; j < recipe.ingredientCount; j++) {
                    System.out.print(recipe.ingredients[j]);
                    if (j < recipe.ingredientCount - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static RecipeList loadRecipes(String fileName) {
        RecipeList recipes = new RecipeList(250); // Assuming max 250 recipes
        
        try {
            java.io.FileReader fr = new java.io.FileReader(fileName);
            char[] buffer = new char[1024];
            StringBuilder currentLine = new StringBuilder();
            int charsRead;
            boolean isFirstLine = true;

            while ((charsRead = fr.read(buffer)) != -1) {
                for (int i = 0; i < charsRead; i++) {
                    char c = buffer[i];
                    if (c == '\n') {
                        if (isFirstLine) {
                            isFirstLine = false;
                        } else {
                            processRecipeLine(currentLine.toString(), recipes);
                        }
                        currentLine.setLength(0);
                    } else {
                        currentLine.append(c);
                    }
                }
            }
            
            if (currentLine.length() > 0 && !isFirstLine) {
                processRecipeLine(currentLine.toString(), recipes);
            }
            
            fr.close();
        } catch (Exception e) {
            System.out.println("Error loading recipes: " + e.getMessage());
        }
        
        return recipes;
    }

    private static void processRecipeLine(String line, RecipeList recipes) {
        // Find the first comma
        int firstCommaIndex = -1;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                firstCommaIndex = i;
                break;
            }
        }
        
        if (firstCommaIndex == -1) return;
        
        String name = line.substring(0, firstCommaIndex).trim();
        Recipe recipe = new Recipe(name, 50); // Assuming max 50 ingredients per recipe
        
        // Parse ingredients
        StringBuilder ingredient = new StringBuilder();
        for (int i = firstCommaIndex + 1; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ',') {
                if (ingredient.length() > 0) {
                    recipe.addIngredient(ingredient.toString());
                    ingredient.setLength(0);
                }
            } else {
                ingredient.append(c);
            }
        }
        
        if (ingredient.length() > 0) {
            recipe.addIngredient(ingredient.toString());
        }
        
        recipes.add(recipe);
    }
}