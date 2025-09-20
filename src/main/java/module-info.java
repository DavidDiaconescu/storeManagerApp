module org.example.agentvanzari {
    requires javafx.controls;
    requires javafx.fxml;

    // librarii adaugate
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.xerial.sqlitejdbc;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens org.example.agentvanzari to javafx.fxml;
    opens Domain to org.hibernate.orm.core;
    exports org.example.agentvanzari;
}
