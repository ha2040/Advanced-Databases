module se.lu.ics {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    exports se.lu.ics;
    opens se.lu.ics.controllers to javafx.fxml;
    opens se.lu.ics.models to javafx.base;
 
}