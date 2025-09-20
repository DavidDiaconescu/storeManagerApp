package org.example.agentvanzari;

import Domain.Agent;
import Domain.Comanda;
import Domain.Produs;
import Service.Service;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelloApplication extends Application {

    private final Service service = new Service();
    private Stage primaryStage;
    private int currentOrderId = -1;
    private final Set<Integer> deliveredOrders = new HashSet<>();
    private ListView<String> listaComenzi;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginScene();

    }

    private void showLoginScene() {
        primaryStage.setTitle("Gestiunea comenzilor de catre un agent de vanzari");

        Label loginLabel = new Label("Autentificare Agent");
        TextField usernameField = new TextField(); usernameField.setPromptText("Email");
        PasswordField passwordField = new PasswordField(); passwordField.setPromptText("Parola");
        Button loginButton = new Button("Intra");
        Button createAccountButton = new Button("Creeaza cont");

        VBox loginLayout = new VBox(10, loginLabel, usernameField, passwordField, loginButton, createAccountButton);
        loginLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(loginLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();

        loginButton.setOnAction(e -> {
            if (service.loginAgent(usernameField.getText(), passwordField.getText())) {
                showMainMenu();
            } else {
                new Alert(Alert.AlertType.ERROR, "Email sau parolă incorecte!").showAndWait();
            }
        });
        createAccountButton.setOnAction(e -> openRegistrationWindow());
    }

    private void showMainMenu() {
        ListView<String> productsList = new ListView<>();
        ListView<String> ordersList   = new ListView<>();

        Button refreshButton       = new Button("Reincarca date");
        Button placeOrderButton    = new Button("Plaseaza comanda");
        Button addProductButton    = new Button("Adauga produs");
        Button updateProductButton = new Button("Actualizează produs");
        Button deleteProductButton = new Button("Șterge produs");
        Button markDeliveredButton = new Button("Marchează ca livrat");

        refreshButton.setOnAction(e -> {
            loadProducts(productsList);
            loadOrders(ordersList);
        });

        ordersList.setOnMouseClicked((MouseEvent e) -> {
            if (e.getClickCount() == 2 && ordersList.getSelectionModel().getSelectedItem()!=null) {
                String selected = ordersList.getSelectionModel().getSelectedItem();
                int orderId = Integer.parseInt(selected.split(":")[0].trim());
                showOrderDetails(orderId);
            }
        });

        placeOrderButton.setOnAction(e -> openPlaceOrderWindow());
        addProductButton.setOnAction(e -> openAddProductWindow(productsList));
        updateProductButton.setOnAction(e -> openUpdateProductWindow(productsList));
        deleteProductButton.setOnAction(e -> {
            String sel = productsList.getSelectionModel().getSelectedItem();
            if (sel==null) {
                new Alert(Alert.AlertType.WARNING, "Selectează un produs de șters!").showAndWait();
                return;
            }
            int prodId = Integer.parseInt(sel.split(":")[0].trim());
            service.stergeProdus(prodId);
            loadProducts(productsList);
        });
        markDeliveredButton.setOnAction(e -> {
            String sel = ordersList.getSelectionModel().getSelectedItem();
            if (sel==null) {
                new Alert(Alert.AlertType.WARNING, "Selectează o comandă!").showAndWait();
                return;
            }
            int orderId = Integer.parseInt(sel.split(":")[0].trim());
            service.marcheazaComandaCaLivrata(orderId);
            loadOrders(ordersList);

        });

        HBox prodButtons = new HBox(10, placeOrderButton, addProductButton, updateProductButton, deleteProductButton);
        HBox orderButtons = new HBox(10, refreshButton, markDeliveredButton);
        prodButtons.setAlignment(Pos.CENTER);
        orderButtons.setAlignment(Pos.CENTER);

        VBox left = new VBox(10, new Label("Lista Produse:"), productsList, prodButtons);
        VBox right = new VBox(10, new Label("Lista Comenzi:"), ordersList, orderButtons);
        left.setAlignment(Pos.CENTER);
        right.setAlignment(Pos.CENTER);

        Scene scene = new Scene(new HBox(20, left, right), 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        loadProducts(productsList);
        loadOrders(ordersList);
    }

    private void openAddProductWindow(ListView<String> productsList) {
        Stage dlg = new Stage();
        dlg.setTitle("Adaugă produs nou");

        TextField nameField = new TextField(); nameField.setPromptText("Denumire produs");
        TextField priceField = new TextField(); priceField.setPromptText("Preț unitar (RON)");
        TextField stockField = new TextField(); stockField.setPromptText("Cantitate stoc");
        Button addBtn = new Button("Adaugă");

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            double price;
            int stock;
            try {
                if (name.isEmpty()) throw new IllegalArgumentException();
                price = Double.parseDouble(priceField.getText().trim());
                stock = Integer.parseInt(stockField.getText().trim());
                if (price < 0 || stock < 0) throw new NumberFormatException();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Date invalide!").showAndWait();
                return;
            }

            service.adaugaProdus(name, price, stock);
            new Alert(Alert.AlertType.INFORMATION, "Produs adăugat!").showAndWait();
            dlg.close();
            loadProducts(productsList);
        });

        VBox v = new VBox(10,
                new Label("Denumire:"), nameField,
                new Label("Preț (RON):"), priceField,
                new Label("Cantitate:"), stockField,
                addBtn);
        v.setAlignment(Pos.CENTER);
        dlg.setScene(new Scene(v, 300, 300));
        dlg.show();
    }


    private void openUpdateProductWindow(ListView<String> productsList) {
        String sel = productsList.getSelectionModel().getSelectedItem();
        if (sel==null) {
            new Alert(Alert.AlertType.WARNING, "Selectează un produs pentru actualizare!").showAndWait();
            return;
        }
        int prodId = Integer.parseInt(sel.split(":")[0].trim());

        Stage dlg = new Stage();
        dlg.setTitle("Actualizează stoc produs");
        TextField stockField = new TextField(); stockField.setPromptText("Noua cantitate");
        Button ok = new Button("Actualizează");

        ok.setOnAction(e -> {
            try {
                int newStock = Integer.parseInt(stockField.getText().trim());
                if (newStock < 0) throw new NumberFormatException();
                service.actualizeazaCantitateProdus(prodId, newStock);
                dlg.close();
                loadProducts(productsList);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Cantitate invalidă!").showAndWait();
            }
        });

        VBox v = new VBox(10, stockField, ok);
        v.setAlignment(Pos.CENTER);
        Scene scene = new Scene(v, 300, 250);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dlg.setScene(scene);

        dlg.show();
    }

    private void reincarcaDate() {
        List<Comanda> lista = service.getComenzi();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Comanda c : lista) {
            items.add(c.getIdComanda() + ": Comandă plasată");
        }

        listaComenzi.setItems(items); // ← `listaComenzi` e `ListView`-ul tău
    }

    private void openPlaceOrderWindow() {
        Stage orderStage = new Stage();
        orderStage.setTitle("Plasează Comandă");

        ListView<String> productView = new ListView<>();
        productView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button startBtn    = new Button("Incepe comandă");
        Button addBtn      = new Button("Adaugă produse");
        Button finalizeBtn = new Button("Finalizează comandă");

        startBtn.setOnAction(e -> {
            currentOrderId = service.creeazaComanda(0, "plasata");
            new Alert(Alert.AlertType.INFORMATION,
                    "Comandă nouă începută: ID " + currentOrderId).showAndWait();
        });

        addBtn.setOnAction(e -> {
            if (currentOrderId<0) {
                new Alert(Alert.AlertType.WARNING, "Trebuie să începi o comandă!").showAndWait();
                return;
            }
            List<String> sel = productView.getSelectionModel().getSelectedItems();
            if (sel.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Selectează produse!").showAndWait();
                return;
            }
            for (String item : sel) {
                int prodId = Integer.parseInt(item.split(":")[0].trim());
                service.adaugaProdusLaComanda(currentOrderId, prodId, 1);
            }
            new Alert(Alert.AlertType.INFORMATION, "Produsele au fost adăugate!").showAndWait();
        });

        finalizeBtn.setOnAction(e -> {
            if (currentOrderId<0) {
                new Alert(Alert.AlertType.WARNING, "Nu există o comandă începută!").showAndWait();
            } else {
                currentOrderId = -1;
                orderStage.close();
            }
        });

        VBox root = new VBox(10,
                new Label("Selectează produse (ID: Denumire - Stoc):"),
                productView, startBtn, addBtn, finalizeBtn
        );
        root.setAlignment(Pos.CENTER);
        orderStage.setScene(new Scene(root, 500, 500));
        orderStage.show();
        loadProducts(productView);
    }

    private void loadProducts(ListView<String> list) {
        list.getItems().clear();
        for (Produs p : service.getListaProduse()) {
            list.getItems().add(p.getIdProdus() + ": " + p.getNume() + " - Stoc: " + p.getCantitate());
        }
    }

    private void loadOrders(ListView<String> list) {
        list.getItems().clear();
        List<Comanda> comenzi = service.getComenzi();

        for (Comanda c : comenzi) {
            String label = c.getIdComanda() + ": Comandă plasată";
            if (c.isLivrata()) {
                label += " 🟢";
            }

            list.getItems().add(label);
        }
    }


    private void showOrderDetails(int orderId) {
        Stage det = new Stage();
        det.setTitle("Detalii Comandă #" + orderId);
        ListView<String> details = new ListView<>();
        Label totalLabel = new Label("Total: 0.00 RON");

        service.getComenzi().stream()
                .filter(c -> c.getIdComanda() == orderId)
                .findFirst()
                .ifPresent(comanda -> {
                    double total = 0;

                    if (comanda.getProduse().isEmpty()) {
                        details.getItems().add("Această comandă nu conține produse.");
                    } else {
                        for (Produs p : comanda.getProduse()) {
                            double pret = p.getPret();
                            int cantitate = 1; // Presupunem 1 momentan — dacă ai mapă Produs → Cantitate, folosește-o!
                            double subtotal = pret * cantitate;
                            total += subtotal;

                            details.getItems().add(
                                    p.getIdProdus() + ": " + p.getNume() +
                                            " - Preț: " + pret +
                                            " x " + cantitate + " = " + subtotal + " RON"
                            );
                        }
                    }

                    totalLabel.setText(String.format("Total: %.2f RON", total));
                });

        VBox box = new VBox(10, details, totalLabel);
        box.setAlignment(Pos.CENTER);
        det.setScene(new Scene(box, 400, 300));
        det.show();
    }



    private void openRegistrationWindow() {
        Stage s = new Stage(); s.setTitle("Crează cont nou");
        TextField name = new TextField(); name.setPromptText("Nume complet");
        TextField user = new TextField(); user.setPromptText("Email");
        PasswordField pass = new PasswordField(); pass.setPromptText("Parola");
        Button b = new Button("Crează cont");
        b.setOnAction(e -> {
            if (service.createAgentAccount(name.getText(), user.getText(), pass.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Cont creat!").showAndWait();
                s.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Eroare la creare cont!").showAndWait();
            }
        });
        VBox v = new VBox(10, new Label("Creare cont"), name, user, pass, b);
        v.setAlignment(Pos.CENTER);
        s.setScene(new Scene(v,300,300)); s.show();
    }
}
