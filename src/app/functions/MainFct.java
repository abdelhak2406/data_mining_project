package app.functions;

import app.controller.MainWindowController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer;
import org.jfree.data.statistics.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import smile.plot.swing.PlotCanvas;
import smile.plot.swing.QQPlot;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;


public class MainFct {
    public static String filePath;

    public static ArrayList readFile(String path) throws Exception{
        filePath = path;
        BufferedReader reader= new BufferedReader(new FileReader(path));
        ArrayList<Double[]> matrice= new ArrayList<>();

        String line = reader.readLine();
        while (line != null) {
            String[] line_table;
            if (MainWindowController.fileExtension.equals("txt")){
                line_table = line.split("\t+");
            }else {
                line_table = line.split(",");
            }


            Double[] line_double=new Double[line_table.length];

            for (int i = 0; i < line_table.length; i++) {
                line_double[i]=Double.parseDouble(line_table[i]);
            }
            matrice.add(line_double);

            line=reader.readLine();
        }
        reader.close();

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
        chart.setPreferredSize(new Dimension(518,186));

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
        panel.setPreferredSize(new Dimension(436, 148));

        return panel;
    }



    public static ChartPanel boxplot_fct(ArrayList<Double[]> dataset, int a){


        ArrayList<Double> col = new ArrayList<>();
        for (int i = 0; i < dataset.size(); i++) {
            col.add(dataset.get(i)[a]);
        }

        DefaultBoxAndWhiskerXYDataset data = new DefaultBoxAndWhiskerXYDataset("");
        BoxAndWhiskerItem item = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(col);

        data.add(new Date(), item);

        Double upper = max_list(dataset,a);
        Double lower = min_list(dataset,a);

        JFreeChart plot = ChartFactory.createBoxAndWhiskerChart("BOXPLOT","","Column"+a, data ,true);

        XYPlot idfPlot = (XYPlot) plot.getPlot();

        ValueAxis y_axis = idfPlot.getRangeAxis();
        y_axis.setRange(lower -1, upper+1);
        XYBoxAndWhiskerRenderer renderer = new XYBoxAndWhiskerRenderer();
        idfPlot.setRenderer(renderer);

        ChartPanel chartPan = new ChartPanel(plot);
        chartPan.setPreferredSize(new Dimension(275, 333));
        return chartPan;

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
        pan.setPreferredSize(new Dimension(461, 186));
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
            return values[(values.length)/2];
        }
        else {
            return (values[(values.length-1)/2]+values[(values.length+1)/2])/2;
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

    public static ArrayList<Double> mode_fct(ArrayList<Double[]> data, int col){
        int   i, j;
        Double max=0.0,maxcount=0.0;
        ArrayList<Double> returnList= new ArrayList<Double>();
        int count;

        for (i = 0; i < data.size(); i++) {
            count = 0;
            for (j = 0; j < data.size(); j++) {
                if (data.get(j)[col].equals(data.get(i)[col]) ){
                    count++;
                }
            }
            if (count > maxcount) {
                maxcount = Double.parseDouble(String.valueOf(count));
                max = data.get(i)[col];

            }
        }
        returnList.add(max);

        for ( i = 0; i < data.size(); i++) {
            count=0;
            for ( j = 0; j < data.size(); j++) {
                if(data.get(j)[col].equals(data.get(i)[col]) ){
                    ++count;
                }
            }
            if (count==maxcount && !returnList.contains(data.get(i)[col])){
                returnList.add(data.get(i)[col]);
            }
        }

        return returnList;
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



    static Double max_list(ArrayList<Double[]> list, int column) {
        int i; Double max=list.get(0)[column];
        for(i=1;i<list.size();i++){
            Double element=list.get(i)[column];
            if(element>max){max= element;}
        }
        return max;
    }
    static Double min_list(ArrayList<Double[]> list, int column) {
        int i; Double min=list.get(0)[column];
        for(i=1;i<list.size();i++){
            if(list.get(i)[column]<min)
            {min= list.get(i)[column];}
        }
        return min;
    }
    static Double[] sort(Double[] array){
        int i, j , indexMin = 0;
        double min, temp ; int t=array.length;
        for(i=0;i<t-1;i++){
            min=array[i];
            for(j=i+1;j<t;j++)
            {  if (array[j] < min)
            {   min = array[j];
                temp = array[i];
                array[i]=min;
                array[j]=temp;}
            }

        }
        return array;
    }
    public static Double etendu(ArrayList<Double[]> list, int column){
        Double max = max_list(list,column);
        Double min = min_list(list,column);
        return max-min;

    }

    public static Double quartiles(ArrayList<Double[]> list, int quartile, int column){
        double result = 0;  ;
        //////////////////Not Getting the right column
        Double[] mycol =new Double[list.size()]; int i ;
        for(i = 0; i < list.size()  ; i++) { mycol[i] = list.get(i)[column];}
        Double[] sorted = sort(mycol);
        int taille = sorted.length , index;
        if (quartile == 1){
            index = taille/4; System.out.println("index = " +index);
            /// pair
            if(taille%4==0) { return ((sorted[index] + sorted[index-1]) /2) ;}
            else return sorted[index-1];
        } else {
            if(quartile==2){
                index = taille/2; System.out.println("index = " +index);
                /// pair
                if(taille%2==0){ return ((sorted[index] + sorted[index-1] )/2) ;}
                else return sorted[index-1];

            } else if(quartile==3){
                index = taille*3/4; System.out.println("index = " +index);
                /// pair
                if(taille*3%4==0){ return ((sorted[index] + sorted[index-1] )/2) ;}
                else return sorted[index-1];
            }
            else{ System.out.println("Unexpected value: " + quartile);
            }
        }



        return result;
    }


    public static Double ecart_interquartile(ArrayList<Double[]> list, int column){
        double q1 = quartiles(list,1,column);
        double q3 = quartiles(list,3,column);
        return q3-q1;

    }

    public static Double variance(ArrayList<Double[]> list, int column) {
        //
        Double ecarttype = ecarttype(list,column);
        return ecarttype*ecarttype;
    }

    public static Double ecarttype(ArrayList<Double[]> list, int column){
        //boucle de somme de (x-moyenne)carre
        //return racine(1/n(somme))
        Double element , somme=0.0 , moyenne = get_moy(list,column); Double difference;
        int i;
        for(i=0;i<list.size();i++){
            element = list.get(i)[column];
            difference=(element-moyenne);
            somme = somme+difference*difference;
        }
        return Math.sqrt(somme/list.size());
    }

    public static ArrayList<Double> outliers(ArrayList<Double[]> list, int column){
        //calculate outliers
        int i , taille=list.size();
        Double outlier1,outlier2 , q1 , q3 , iqr ,element;
        q1 = quartiles(list,1,column);
        q3 = quartiles(list,3,column);
        iqr =  ecart_interquartile(list,column);
        outlier1 = q1-1.5*iqr;
        outlier2 = q3+1.5*iqr;
        ArrayList<Double> outliers = new ArrayList<>();
        for(i=0;i<taille;i++){
            element = list.get(i)[column];
            if ( element >outlier2) {
                outliers.add(element);
            }
            else {if ( element <outlier1) {
                outliers.add(element);
            }}
        }
        return outliers;

    }

    public static double correlationCoef(ArrayList<Double[]> data, int column1, int column2){
        Double somme = new Double(0.0);
        Double nAB = new Double(0.0);
        Double denomin = new Double(0.0);
        for (int i = 0; i < data.size(); i++) {
            somme += data.get(i)[column1] * data.get(i)[column2];
        }

        nAB = data.size() * get_moy(data, column1) * get_moy(data, column2);
        denomin = (data.size()-1) * ecarttype(data, column1) * ecarttype(data, column2);

        return (somme-nAB)/denomin;
    }

    //--------------------------------part2--------------------------
    public static void minMaxNormalization(ArrayList<Double> column){
        double min = Collections.min(column);
        double max = Collections.max(column);

        for (int i = 0; i < column.size(); i++) {
            double tempValue = (column.get(i) - min)/(max - min);
            column.set(i, tempValue);
        }

        System.out.println(column);
    }

    public static void zScoreNormalization(ArrayList<Double> column){
        double sum = 0;
        double moy = getMoy(column);
        for (int i = 0; i < column.size(); i++) {
            sum += Math.abs(column.get(i) - moy);
        }
        System.out.println(column.size());
        double s = sum/column.size();

        for (int i = 0; i < column.size(); i++) {
            double val = column.get(i);
            column.set(i, (val - moy)/s);
        }

        System.out.println(column);
    }

    public static double getMoy(ArrayList<Double> column){
        double sum = 0;
        for (double val:column) {
            sum += val;
        }
        return (sum/column.size());
    }

    //----------------------- discrétisation ----------------------
    public static ArrayList<String> discretisationEqual(ArrayList<Double>column, int q, int c){
        double min = Collections.min(column);
        double max = Collections.max(column);
        double int_length= (max-min)/q;
        double[] array={int_length,int_length*2,int_length*3};
        ArrayList<String> result= new ArrayList<>(column.size());

        for (int i = 0; i < column.size(); i++) {
            if(column.get(i)<min+array[0]){
                result.add('I'+String.valueOf(c)+'1');
            }
            else if(column.get(i)<min+array[1]){
                result.add('I'+String.valueOf(c)+'2');
            }
            else if(column.get(i)<min+array[2]){
                result.add('I'+String.valueOf(c)+'3');
            }
            else {
                result.add('I'+String.valueOf(c)+'4');
            }
        }
        return result;


    }

    public static ArrayList<String> discretisation_effectif(ArrayList<Double> column,int q, int c){
        Double[] ar=new Double[column.size()];
        for (int i = 0; i < column.size(); i++) {
            ar[i]=column.get(i);
        }
        Double[] sorted=sort(ar);
        Double [] quartiles= new Double[q];
        for (int i = 0; i < q; i++) {
            quartiles[i]=sorted[(Math.round(column.size()/q*(i+1)))];
        }
        //System.out.println(quartiles[0]+"   "+quartiles[1]+"   "+quartiles[2]+"   "+quartiles[3]);
        ArrayList<String> result= new ArrayList<>();
        for (int i = 0; i < column.size(); i++) {
            for (int j = 0; j < q; j++) {
                if (column.get(i) <= quartiles[j]) {
                    result.add('I' + String.valueOf(c) + String.valueOf(j + 1));
                    break;
                }
            }
        }
        return result;
    }

    public static void frequentItem(double minSup, ArrayList<String> column, int c){
        String[] items = {"I"+c+"1","I"+c+"2","I"+c+"3","I"+c+"4"};
        String[][] frequency= new String[2][column.size()];
        int j=0;
        for(String d:items){
            int i=0;
            for(String col:column){

                if(d.equals(col)){
                    i++;
                }
            }
            if(Double.valueOf(i)/Double.valueOf(column.size())>minSup){
                frequency[0][j]=d;
                frequency[1][j]=String.valueOf(i);

                System.out.println(frequency[0][j]+", freq"+frequency[1][j]);
            }
            j++;
        }
    }


    public static void main(String[] args) throws Exception{
        ArrayList<Double[]> data= MainFct.readFile("datasets\\seeds.txt");
        ArrayList<Double> ar = new ArrayList();
        Double[] instance = {};
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 7; i++) {

            for (int j= 0; j < data.size(); j++) {
                ar.add(data.get(j)[i]);
            }
            list.add(discretisation_effectif(ar,4,i+1));

            ar.clear();

        }
        for (int i = 0; i < list.size(); i++) {
            frequentItem(0.2,list.get(i),i+1);
        }

    }
}
