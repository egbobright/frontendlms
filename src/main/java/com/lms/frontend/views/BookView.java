package com.lms.frontend.views;

import com.lms.frontend.models.Book;
import com.lms.frontend.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class BookView {
    private final BorderPane mainLayout;
    private final TableView<Book> bookTable;
    private final ObservableList<Book> bookList;

    private final TextField titleField;
    private final TextField authorField;
    private final TextField isbnField;
    private final DatePicker publishedDatePicker;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button refreshButton;
    private final Button clearButton;

    private final BookService bookService;
    private Book selectedBook;

    public BookView() {
        this.bookService = new BookService();
        this.bookList = FXCollections.observableArrayList();
        this.mainLayout = new BorderPane();

        // Initialize UI components
        this.titleField = new TextField();
        this.authorField = new TextField();
        this.isbnField = new TextField();
        this.publishedDatePicker = new DatePicker();

        this.addButton = new Button("Add");
        this.updateButton = new Button("Update");
        this.deleteButton = new Button("Delete");
        this.refreshButton = new Button("Refresh");
        this.clearButton = new Button("Clear");

        this.bookTable = createTableView();

        setupLayout();
        setupEventHandlers();
        loadBooks();
    }

    private TableView<Book> createTableView() {
        TableView<Book> table = new TableView<>();
        table.setItems(bookList);

        TableColumn<Book, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(150);

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(120);

        TableColumn<Book, LocalDate> dateColumn = new TableColumn<>("Published Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        dateColumn.setPrefWidth(120);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, dateColumn);
        return table;
    }

    private void setupLayout() {
        // Form Panel
        GridPane formPanel = new GridPane();
        formPanel.setPadding(new Insets(10));
        formPanel.setHgap(10);
        formPanel.setVgap(10);

        formPanel.add(new Label("Title:"), 0, 0);
        formPanel.add(titleField, 1, 0);

        formPanel.add(new Label("Author:"), 0, 1);
        formPanel.add(authorField, 1, 1);

        formPanel.add(new Label("ISBN:"), 0, 2);
        formPanel.add(isbnField, 1, 2);

        formPanel.add(new Label("Published Date:"), 0, 3);
        formPanel.add(publishedDatePicker, 1, 3);

        titleField.setPrefWidth(300);
        authorField.setPrefWidth(300);
        isbnField.setPrefWidth(300);
        publishedDatePicker.setPrefWidth(300);

        // Button Panel
        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(10));
        buttonPanel.getChildren().addAll(addButton, updateButton, deleteButton, clearButton, refreshButton);

        // Right Panel (Form + Buttons)
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.getChildren().addAll(new Label("Book Details"), formPanel, buttonPanel);
        rightPanel.setStyle("-fx-background-color: #f0f0f0;");

        // Main Layout
        mainLayout.setCenter(bookTable);
        mainLayout.setRight(rightPanel);
        mainLayout.setPadding(new Insets(10));

        // Initial button states
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void setupEventHandlers() {
        // Table selection handler
        bookTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedBook = newSelection;
                        populateForm(newSelection);
                        updateButton.setDisable(false);
                        deleteButton.setDisable(false);
                    }
                }
        );

        // Add button handler
        addButton.setOnAction(e -> addBook());

        // Update button handler
        updateButton.setOnAction(e -> updateBook());

        // Delete button handler
        deleteButton.setOnAction(e -> deleteBook());

        // Refresh button handler
        refreshButton.setOnAction(e -> loadBooks());

        // Clear button handler
        clearButton.setOnAction(e -> clearForm());
    }

    private void addBook() {
        if (!validateForm()) {
            return;
        }

        Book newBook = new Book();
        newBook.setTitle(titleField.getText());
        newBook.setAuthor(authorField.getText());
        newBook.setIsbn(isbnField.getText());
        newBook.setPublishedDate(publishedDatePicker.getValue());

        try {
            Book addedBook = bookService.addBook(newBook);
            bookList.add(addedBook);
            clearForm();
            showSuccess("Book added successfully!");
        } catch (Exception ex) {
            // Error already shown in service
        }
    }

    private void updateBook() {
        if (selectedBook == null) {
            showWarning("No book selected", "Please select a book to update.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        selectedBook.setTitle(titleField.getText());
        selectedBook.setAuthor(authorField.getText());
        selectedBook.setIsbn(isbnField.getText());
        selectedBook.setPublishedDate(publishedDatePicker.getValue());

        try {
            Book updatedBook = bookService.updateBook(selectedBook.getId(), selectedBook);
            bookTable.refresh();
            showSuccess("Book updated successfully!");
        } catch (Exception ex) {
            // Error already shown in service
        }
    }

    private void deleteBook() {
        if (selectedBook == null) {
            showWarning("No book selected", "Please select a book to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Book");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedBook.getTitle() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    bookService.deleteBook(selectedBook.getId());
                    bookList.remove(selectedBook);
                    clearForm();
                    showSuccess("Book deleted successfully!");
                } catch (Exception ex) {
                    // Error already shown in service
                }
            }
        });
    }

    private void loadBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            bookList.clear();
            bookList.addAll(books);
        } catch (Exception ex) {
            // Error already shown in service
        }
    }

    private void populateForm(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        publishedDatePicker.setValue(book.getPublishedDate());
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        publishedDatePicker.setValue(null);
        selectedBook = null;
        bookTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showWarning("Validation Error", "Title is required.");
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            showWarning("Validation Error", "Author is required.");
            return false;
        }
        if (isbnField.getText().trim().isEmpty()) {
            showWarning("Validation Error", "ISBN is required.");
            return false;
        }
        if (publishedDatePicker.getValue() == null) {
            showWarning("Validation Error", "Published date is required.");
            return false;
        }
        return true;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return mainLayout;
    }
}