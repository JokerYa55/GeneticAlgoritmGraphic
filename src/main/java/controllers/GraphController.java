package controllers;

import beans.PopulationInfo;
import gen_algoritm.CalcResultInterface;
import gen_algoritm.GenInterface;
import gen_algoritm.PopulationInterface;
import gen_algoritm.PopulationItemInterface;
import gen_algoritm.implementation.Population;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.extern.java.Log;

@Log
public class GraphController extends AnchorPane {

    private static final int COUNT = 50;
    PopulationInterface population;
    private static final double A = 0.1;
    private static final double B = 5;
    private static final double DELTA_X = (B - A) / COUNT;
    private Parent root;
    Double[] x = new Double[COUNT];

    @FXML
    LineChart<String, Double> idChart;

    @FXML
    Button idBtnStart;

    @FXML
    Button idBtnNextStep;

    @FXML
    TableView<PopulationInfo> idDataTable;

    @FXML
    private TableColumn<PopulationInfo, Integer> idColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> aColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> bColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> cColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> dColumn;

//    @FXML
//    private TableColumn<PopulationInfo, Double> eColumn;
    @FXML
    private TableColumn<PopulationInfo, Double> fColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> gColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> hColumn;

    @FXML
    private TableColumn<PopulationInfo, Double> funColumn;

    public GraphController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphController.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            root = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        for (int i = 0; i < COUNT; i++) {
            x[i] = A + i * DELTA_X;
        }

        population = new Population(100, x);
        idChart.setTitle("Series");

    }

    public Parent getRoot() {
        return root;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void btnStartClick(ActionEvent actionEvent) {

    }

    public void shiwTable() {
        System.out.println("----------- shiwTable -----------");
        ObservableList<PopulationInfo> populationInfo = FXCollections.observableArrayList();
//        population.getPipulationItemList().forEach((t) -> {
//            System.out.println(String.format("criteria 2 = %s", t.getCriteriaResult()));            
//        });

        population.getPipulationItemList().forEach((PopulationItemInterface t) -> {
            log.info(String.format("criteria 5 = %s name = %s", t.getCriteriaResult(), t.getName()));
            Double[] gen = ((GenInterface) t.getGen()).getGenAsArray();
            populationInfo.add(new PopulationInfo(t.getName(),
                    String.format("%f", gen[0]),
                    String.format("%f", gen[1]),
                    String.format("%f", gen[2]),
                    String.format("%f", gen[3]),
                    String.format("%f", gen[4]),
                    String.format("%f", gen[5]),
                    String.format("%f", gen[6]),
                    String.format("%f", (Double) t.getCriteriaResult())));
        });

        populationInfo.forEach((t) -> {
            System.out.println(String.format("t = %s", t));
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        aColumn.setCellValueFactory(new PropertyValueFactory<>("a"));
        bColumn.setCellValueFactory(new PropertyValueFactory<>("b"));
        cColumn.setCellValueFactory(new PropertyValueFactory<>("c"));
        dColumn.setCellValueFactory(new PropertyValueFactory<>("d"));
        fColumn.setCellValueFactory(new PropertyValueFactory<>("f"));
        gColumn.setCellValueFactory(new PropertyValueFactory<>("g"));
        hColumn.setCellValueFactory(new PropertyValueFactory<>("e"));
        funColumn.setCellValueFactory(new PropertyValueFactory<>("h"));
        idDataTable.setItems(populationInfo);
        log.info("----------- shiwTable end --------");
    }

    public void showGraph1() {
        idChart.getData().clear();
        //Рисуем sin
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("sin(x)");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        List<CalcResultInterface<Double, Double>> baseList = population.getPipulationItemList().get(0).getBaseResultList();
        baseList.forEach((t1) -> {
            datas.add(new XYChart.Data<String, Double>(t1.getX() + "", t1.getY()));
        });
        series1.setData(datas);
        idChart.setCreateSymbols(false);
        idChart.getData().add(series1);

        population.getPipulationItemList().stream().limit(2).forEach((t) -> {
            XYChart.Series series2 = new XYChart.Series();
            series2.setName(t.getName());
            List<CalcResultInterface<Double, Double>> genList = t.getGenResultList();
            ObservableList<XYChart.Data> datas1 = FXCollections.observableArrayList();
            genList.forEach((t1) -> {
                datas1.add(new XYChart.Data<String, Double>(t1.getX() + "", t1.getY()));
            });
            series2.setData(datas1);
            idChart.setCreateSymbols(false);
            idChart.getData().add(series2);
        });
    }

    @FXML
    public void btnNextStepClick(ActionEvent actionEvent) {
        try {
            log.info(String.format("action = %s", actionEvent));
            CompletableFuture future = CompletableFuture.runAsync(() -> {
                for (int i = 0; i < 200; i++) {
                    population.nextStep();
                }
            });
            future.get();
            shiwTable();
            showGraph1();
        } catch (InterruptedException ex) {
            Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
