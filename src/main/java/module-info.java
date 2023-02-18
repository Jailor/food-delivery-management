module com.andrei.pt_30424_pelle_andrei_assignment_4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    exports com.andrei;
    exports com.andrei.Controller;
    exports com.andrei.DataAccess;
    exports com.andrei.BusinessLogic;
    exports com.andrei.Controller.ReportController;

    // necessary for runtime access. If the entire module is declared open, these are no longer necessary.
    // an open module makes all packages inside the module accessible for deep reflection. Spring and JavaFX use this.
    opens com.andrei to javafx.fxml;
    opens com.andrei.Controller to javafx.fxml;
    opens com.andrei.DataAccess to javafx.fxml;
    opens com.andrei.BusinessLogic to javafx.fxml;
    opens com.andrei.Controller.ReportController to javafx.fxml;

    // requires keyword - specify a module that is required by the current module (it depends on this module for functionality
    // exports keyword - specify packages exported by the current module
    // provides keyword - specify the service implementations provided by the current module
    // uses keyword - specify the service that the module consumes
}