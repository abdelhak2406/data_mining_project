package app.functions;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import smile.plot.swing.PlotCanvas;
import smile.plot.swing.QQPlot;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MainFct {
    public static String filePath;

    public static ArrayList readFile(String path) throws Exception{
        filePath = path;
        BufferedReader reader= new BufferedReader(new FileReader(path));
        ArrayList<Double[]> matrice= new ArrayList<>();

        String line = reader.readLine();
        while (line != null) {
            String[] line_table=line.split("\t+");
            System.out.println(line_table);

            Double[] line_double=new Double[line_table.length];

            for (int i = 0; i < line_table.length; i++) {
                line_double[i]=Double.parseDouble(line_table[i]);
            }
            matrice.add(line_double);

            line=reader.readLine();
        }

        return matrice;
    }

    public static void print_data(ArrayList<Double[]> data){
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < 8; j++) {
                System.out.println(data.get(i)[j]);
            }
            System.out.println("---------------");
        }
    }

    public static ChartPanel scatter_diagram(ArrayList<Double[]> data, int a, int b){
        XYSeriesCollection collection= new XYSeriesCollection();
        XYSeries series= new XYSeries("");
        for (int i = 0; i < data.size(); i++) {
            series.add(data.get(i)[a],data.get(i)[b]);
        }
        collection.addSeries(series);
        JFreeChart scatter_plot= ChartFactory.createScatterPlot("Scatter Plot",
                "attribute a","Attribute b",collection);

        ChartPanel chart = new ChartPanel(scatter_plot);
        chart.setPreferredSize(new Dimension(190,120));

        return (chart);
    }

    public static HashMap get_frequencies(ArrayList<Double[]> data){
        HashMap<Double,Integer> hash= new HashMap();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < data.size(); j++) {
                hash.put(data.get(j)[i],hash.get(data.get(j)[i])+1);
            }
            System.out.println(hash);
        }
        return hash;
    }

    public static ChartPanel histogram_fct(ArrayList<Double[]> data,int a){
        HistogramDataset histogram= new HistogramDataset();
        double[] values= new double[data.size()];

        for (int i = 0; i < data.size(); i++) {
            values[i]=data.get(i)[a];
        }
        histogram.addSeries("key",values,20);
        JFreeChart chart = ChartFactory.createHistogram("Histogram",
                "Data", "Frequency", histogram);

                ChartPanel panel= new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(370, 130));
        return panel;
    }



    public static ChartPanel boxplot_fct(ArrayList<Double[]> dataset, int a){

        DefaultBoxAndWhiskerCategoryDataset data = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> col = new ArrayList<>();


        for (int i = 0; i < dataset.size(); i++) {
            col.add(dataset.get(i)[a]);
        }
        data.add(col, a, a);

        JFrame f = new JFrame("BoxPlot");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final CategoryAxis xAxis = new CategoryAxis("Attribut");
        final NumberAxis yAxis = new NumberAxis("Values");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setSeriesToolTipGenerator(1, new BoxAndWhiskerToolTipGenerator());
        //renderer.setMeanVisible(false);

        final CategoryPlot plot = new CategoryPlot(data, xAxis, yAxis, renderer);
        final JFreeChart chart = new JFreeChart(
                "BoxPlot", plot);


        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setLayout(new BorderLayout());
        return panel;

    }


    public static JPanel qqplot_fct(ArrayList<Double[]> dataset,int x, int y)
    {
        int size = dataset.size();
        double att1[] = new double[size];
        double att2[] = new double[size];

        for (int i = 0; i < dataset.size(); i++) {
            att1[i]=(dataset.get(i)[x]);
            att2[i]=(dataset.get(i)[y]);
        }

        PlotCanvas canvas = QQPlot.plot(att1, att2);
        JPanel pan = new JPanel(new BorderLayout());
        pan.setPreferredSize(new Dimension(400, 130));
        pan.add(canvas, BorderLayout.CENTER);

        return pan;
    }



    public static double get_moy(ArrayList<Double[]> data, int column){
        double moy=0;
        for (int i = 0; i < data.size(); i++) {
            moy+=data.get(i)[column];
        }
        moy=moy/ data.size();
        return moy;
    }


    public static Double get_mediane(ArrayList<Double[]> data, int column){
        Double[] values=sort(data,column);
        if(values.length%2==1){
            return values[(values.length+1)/2];
        }
        else {
            return (values[values.length/2]+values[(values.length+2)/2])/2;
        }
    }

    public static Double[] sort(ArrayList<Double[]> data, int col){
        Double[] values=new Double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            values[i]=data.get(i)[col];
        }
        Arrays.sort(values);
        return values;
    }

    public static Double milieu_etendu(ArrayList<Double[]> data, int col){
        Double max=0.0; Double min=data.get(0)[col];

        for (int i = 0; i < data.size(); i++) {
            if(max<data.get(i)[col]){
                max=data.get(i)[col];
            }
            if(min>data.get(i)[col]){
                min=data.get(i)[col];
            }
        }
        return (max+min)/2;
    }

    public static Double[] mode_fct(ArrayList<Double[]> data, int col){
        int   i, j;
        //max value est max[0], max count est max[1]
        Double[] max= new Double[2];
        max[0]=0.0;max[1]=0.0;

        for (i = 0; i < data.size(); ++i) {
            int count = 0;
            for (j = 0; j < data.size(); ++j) {
                if (data.get(j)[col] == data.get(i)[col])
                    ++count;
            }

            if (count > max[1]) {
                max[1] = Double.parseDouble(String.valueOf(count));
                max[0] = data.get(i)[col];
            }
        }
        return max;
    }


    public static Double moyenne_tranquee(ArrayList<Double[]> data, int col){
        Double[] values=sort(data,col);
        Double dbl=values.length*0.025;
        int min= dbl.intValue()+1;
        dbl=values.length*0.975;
        int max=dbl.intValue();

        Double moyenne=0.0;
        for (int i = min; i < max; i++) {
            moyenne+=values[i];
        }
        return moyenne/ values.length;
    }



    public static void main(String[] args) throws Exception{
        ArrayList<Double[]> data= MainFct.readFile(filePath);
        print_data(data);
        //ChartPanel chartPanel= scatter_diagram(data,2,4);
        System.out.println(mode_fct(data,0)[0]);
        System.out.println(get_moy(data,0));
        System.out.println(moyenne_tranquee(data,0));
        System.out.println(milieu_etendu(data,0));
        System.out.println(get_mediane(data,0));

    }
}
