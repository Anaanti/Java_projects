import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Expense Class
class Expense {
    private String name;
    private double amount;
    private String category;

    public Expense(String name, double amount, String category) {
        this.name = name;
        this.amount = amount;
        this.category = category;
    }

    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
}

// Expense Tracker App
public class ExpenseTracker extends Application {
    private ListView<String> expenseListView = new ListView<>();
    private ArrayList<Expense> expenses = new ArrayList<>();
    private PieChart pieChart;
    private Label totalExpenseLabel = new Label("Total Expense: ₹0");

    // Defining the Pie Chart update method in the ExpenseTracker class
    private void updatePieChart() {
        // Clear existing chart data
        pieChart.getData().clear();

        // Create a map to hold the sum of expenses per category
        Map<String, Double> categorySums = new HashMap<>();

        // Calculate sum of expenses per category
        for (Expense expense : expenses) {
            categorySums.put(expense.getCategory(), categorySums.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }

        // Create PieChart data from the category sums
        for (Map.Entry<String, Double> entry : categorySums.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Components
        Label expenseNameLabel = new Label("Expense Name:");
        TextField expenseNameField = new TextField();

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Food", "Entertainment", "Bills", "Transportation", "Healthcare", "Other");

        Button addButton = new Button("Add Expense");
        Button delButton = new Button("Delete Expense");

        // Initialize the PieChart
        pieChart = new PieChart();

        // GridPane for expense entry form
        GridPane entryGrid = new GridPane();
        entryGrid.setHgap(10);
        entryGrid.setVgap(10);
        entryGrid.add(expenseNameLabel, 0, 0);
        entryGrid.add(expenseNameField, 1, 0);
        entryGrid.add(amountLabel, 0, 1);
        entryGrid.add(amountField, 1, 1);
        entryGrid.add(categoryLabel, 0, 2);
        entryGrid.add(categoryComboBox, 1, 2);
        entryGrid.add(addButton, 1, 3);
        entryGrid.add(totalExpenseLabel, 1, 7);
        entryGrid.add(delButton, 1 , 7);

        // HBox for layout (ListView + PieChart)
        HBox mainLayout = new HBox(20);  // Space between ListView and PieChart
        mainLayout.setPadding(new javafx.geometry.Insets(10));

        // ListView for displaying expenses
        VBox listViewLayout = new VBox(10);
        listViewLayout.getChildren().addAll(new Label("Expenses:"), expenseListView);

        // Adding components to HBox layout
        mainLayout.getChildren().addAll(listViewLayout, pieChart);

        // VerticalBox for the entire UI
        VBox rootLayout = new VBox(10);
        rootLayout.getChildren().addAll(entryGrid, mainLayout);

        addButton.setOnAction(e -> {
            String name = expenseNameField.getText();
            String category = categoryComboBox.getValue();

            // Validate input
            if (!name.isEmpty() && !amountField.getText().isEmpty() && category != null && !category.isEmpty()) {
                double amount = Double.parseDouble(amountField.getText());

                // Create Expense Object
                Expense expense = new Expense(name, amount, category);
                expenses.add(expense);

                // Add to ListView
                String expenseEntry = name + " - ₹" + amount + " (" + category + ")";
                expenseListView.getItems().add(expenseEntry);

                // Update PieChart
                updatePieChart();

                // Update Total Expense
                double totalExpense = 0;
                for (Expense exp : expenses) {
                    totalExpense += exp.getAmount();
                }
                totalExpenseLabel.setText("Total Expense: ₹" + totalExpense);

                // Clear input fields
                expenseNameField.clear();
                amountField.clear();
                categoryComboBox.getSelectionModel().clearSelection();
            }
        });

        delButton.setOnAction(e ->{
            int selectedIndex = expenseListView.getSelectionModel().getSelectedIndex();
            if(selectedIndex != -1){
                expenseListView.getItems().remove(selectedIndex);
                expenses.remove(selectedIndex);

                updatePieChart();

                double totalExpense = 0;
                for(Expense exp : expenses){
                    totalExpense += exp.getAmount();
                }
                totalExpenseLabel.setText("Total Expense: ₹" + totalExpense);
            }
            else{
                System.out.println("No expense Selected.");
            }
        });

        // Scene Setup
        Scene scene = new Scene(rootLayout, 600, 400);  // Increased window size for layout
        primaryStage.setScene(scene);
        primaryStage.setTitle("Expense Tracker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
