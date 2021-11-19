package app.controller;

import app.Tendances_centrales;
import app.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.functions.MainFct;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jfree.chart.ChartPanel;


import javax.swing.*;
import java.util.ArrayList;

public class DashboardWindowController {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TableView<Tendances_centrales> table_mcentrale=new TableView<Tendances_centrales>();


    @FXML private TableView<String> table_mdispersion;



    @FXML
    private Button btn_submit;

    @FXML
    private ComboBox<String> combo_firstCol;


    @FXML
    private ComboBox<String> combo_secondCol;


    @FXML
    private BarChart chart_histogram;

    @FXML
    private Pane chart_boxPlot;

    @FXML
    private Pane chart_scatterPlot;

    @FXML
    private Pane chart_qPlot;

    @FXML
    private Pane chart_otherGraph2;

    @FXML
    private GridPane chart_tables;





    @FXML
    void submitAction(ActionEvent event) throws Exception {

        String arg1="";
        String arg2 ="";
        arg1+= combo_firstCol.getValue();
        arg2+= combo_secondCol.getValue();


        final SwingNode swingNodeScatterPot = new SwingNode();
        final SwingNode swingNodeBoxPot = new SwingNode();
        final SwingNode swingNodeQQPlot = new SwingNode();
        final SwingNode swingNodeHistogram = new SwingNode();
        int iarg1;
        int iarg2;

        System.out.println(MainFct.filePath);
        ArrayList<Double[]> data= MainFct.readFile(MainFct.filePath);
        //MainFct.print_data(data);

        if (!arg1.equals("null") && !arg2.equals("null")){

             iarg1= Integer.parseInt(arg1) ;
             iarg2 = Integer.parseInt(arg2) ;

            ChartPanel scatterplot = MainFct.scatter_diagram(data, iarg1, iarg2);
            swingNodeScatterPot.setContent(scatterplot);
            chart_scatterPlot.getChildren().add(swingNodeScatterPot);


            ChartPanel boxplot = MainFct.boxplot_fct(data, iarg1);
            swingNodeBoxPot.setContent(boxplot);
            chart_boxPlot.getChildren().add(swingNodeBoxPot);


            JPanel qqplot  = MainFct.qqplot_fct(data, iarg1, iarg2);
            swingNodeQQPlot.setContent(qqplot);
            chart_qPlot.getChildren().add(swingNodeQQPlot);


            ChartPanel histogram = MainFct.histogram_fct(data, iarg1);
            swingNodeHistogram.setContent(histogram);
            /// check this one
            chart_otherGraph2.getChildren().add(swingNodeHistogram);

            Utilities utilities= new Utilities();
            Double[] table_centrale= utilities.return_centrale(data,combo_firstCol.getSelectionModel().getSelectedIndex());
            ObservableList<Tendances_centrales> tendance= FXCollections.observableArrayList(
                    new Tendances_centrales(table_centrale[0],table_centrale[1],table_centrale[2],
                            table_centrale[3],table_centrale[4]));

            table_mcentrale.setItems(tendance);


        }

        else if(!arg1.equals("null")){
            iarg1 = Integer.parseInt(arg1) ;

            ChartPanel boxplot = MainFct.boxplot_fct(data, iarg1);
            swingNodeBoxPot.setContent(boxplot);
            chart_boxPlot.getChildren().add(swingNodeBoxPot);

            ChartPanel histogram = MainFct.histogram_fct(data, iarg1);
            swingNodeHistogram.setContent(histogram);
            chart_otherGraph2.getChildren().add(swingNodeHistogram);

            Utilities utilities= new Utilities();
            Double[] table_centrale= utilities.return_centrale(data,combo_firstCol.getSelectionModel().getSelectedIndex());
            ObservableList<Tendances_centrales> tendance= FXCollections.observableArrayList(
                    new Tendances_centrales(table_centrale[0],table_centrale[1],table_centrale[2],
                            table_centrale[3],table_centrale[4]));

            table_mcentrale.setItems(tendance);
        }
        else
        { // no attribut selected
            System.out.println("SELETCT AN ATTRIBUT AT LEAST.");

        }





    }
    @FXML
    void switchToHomeWin(ActionEvent event) throws Exception{
        Utilities u = new Utilities();
        u.switchWindow(event, "/resources/views/MainWindow.fxml", root, stage, scene);
    }



    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel = new JPanel();
                panel.add(new JButton("Click me!"));
                swingNode.setContent(panel);
            }
        });
}}



