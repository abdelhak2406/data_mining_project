package app.functions;

import scala.Array;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Apriori {

        public ArrayList<ArrayList<String>> itemset;
        public int minSup;
        public HashMap<String, Integer> frequentItemsList = new HashMap<>();
        public ArrayList<ArrayList<String>> condidatesList = new ArrayList<>();


        public Apriori(ArrayList<Double[]> data, int support){
                this.itemset = this.preprocessData(data);
                this.minSup = itemset.get(0).size()*support/100;
        }

        public ArrayList<ArrayList<String>> preprocessData(ArrayList<Double[]> data){
                ArrayList<ArrayList<String>> descritized_data = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < 7; i++) {
                        descritized_data.add(MainFct.discretisationEqual(this.getColumn(data, i), 4, i+1));
                }
                return descritized_data;
        }

        public ArrayList<ArrayList<String>> transposeData(ArrayList<ArrayList<String>> data){
                ArrayList<ArrayList<String>> new_data = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < data.get(0).size(); i++) {
                        ArrayList<String> tempItemset = new ArrayList<>();
                        for (int j = 0; j < data.size(); j++) {
                                tempItemset.add(data.get(j).get(i));
                        }
                        new_data.add(tempItemset);
                }
                return new_data;
        }

        public ArrayList<Double> getColumn(ArrayList<Double[]> data, int column){
                ArrayList<Double> columnArray = new ArrayList<>();
                for(int i=0 ;i<data.size();i++) {
                        columnArray.add(data.get(i)[column]);
                }
                return  columnArray;
        }

        public ArrayList<String[]> calculateFrequentItems(){
                //création de la liste des items fréquents
                ArrayList<String[]> frequentItems = new ArrayList<>();
                ArrayList<String> frequentCondidates = new ArrayList<>();
                ArrayList<String> firstCondidates = new ArrayList<>();
                for (int i = 0; i < this.itemset.size(); i++) {
                        String[] items = {"I"+ (i+1) + "1", "I"+ (i+1) + "2", "I"+ (i+1) + "3", "I"+ (i+1) + "4"};
                        for (String item: items) {
                                int frequence = 0;
                                for (int j = 0; j < this.itemset.get(i).size(); j++) {
                                        if (this.itemset.get(i).get(j).equals(item)){
                                                frequence += 1;
                                        }
                                }
                                if (frequence >= this.minSup){
                                        this.frequentItemsList.put(item, frequence);
                                        frequentCondidates.add(item);
                                }
                        }
                }
                firstCondidates = frequentCondidates;
                int iteration = 0;
                while(frequentCondidates.size() > iteration) {
                        this.condidatesList.add(frequentCondidates);
                        frequentCondidates = this.join(firstCondidates, iteration+2);
                        ArrayList<String> tempFrequent = new ArrayList<>();
                        ArrayList<ArrayList<String>> itemset = this.transposeData(this.itemset);
                        for (String item : frequentCondidates) {
                                String[] items = item.split(",");
                                int frequence = 0;

                                for (int i = 0; i < itemset.size(); i++) {
                                        int cpt = 0;
                                        for (String condidat: items) {
                                                if (itemset.get(i).contains(condidat)){
                                                        cpt += 1;
                                                }
                                        }
                                        if(cpt == items.length){
                                                frequence += 1;
                                        }
                                }
                                if(frequence >= this.minSup){
                                        tempFrequent.add(item);
                                        frequentItemsList.put(item, frequence);
                                }
                        }
                        iteration += 1;

                        //update first condidates list
                        ArrayList<String> tempFirstCondidates = new ArrayList<>();
                        for (String item: firstCondidates) {
                                int cpt = 0;
                                for (String selectedItem: tempFrequent) {
                                        String[] tempItems = selectedItem.split(",");
                                        for (int i = 0; i < tempItems.length; i++) {
                                                if (item.equals(tempItems[i])){
                                                        cpt += 1;
                                                }
                                        }
                                }
                                if(cpt != 0){
                                        tempFirstCondidates.add(item);
                                }
                        }

                        firstCondidates = tempFirstCondidates;
                }

                Iterator it = frequentItemsList.entrySet().iterator();
                while (it.hasNext()) {
                        Map.Entry item = (Map.Entry)it.next();
                        String temp=((String)item.getKey());
                        frequentItems.add((String[]) temp.split(","));
                        it.remove(); // avoids a ConcurrentModificationException
                }

                return frequentItems;
        }

        public ArrayList<String> join(ArrayList<String> list, int nbItems){
                ArrayList<String> joinedItems = new ArrayList<>();
                for (int i = 0; i < list.size()-(nbItems-1); i++) {
                        for (int j = i+1; j < list.size(); j++) {
                                String newItem = list.get(i);
                                int cpt = 0;
                                for (int k = 0; k < nbItems-1; k++) {
                                        if (j+k < list.size()){
                                                newItem += "," + list.get(j+k);
                                                cpt += 1;
                                        }
                                }
                                if (cpt == nbItems-1){
                                        joinedItems.add(newItem);
                                }
                        }
                }
                return joinedItems;
        }

        public ArrayList<ArrayList<String>> getCondidatesList(){
                return this.condidatesList;
        }

        public static void main(String[] args) throws Exception {
                /**
                 * If we want to test it her's how!
                 * just change the name mainTest to main and execute it.
                 */
                ArrayList<Double[]> data= MainFct.readFile("C:\\Users\\Raouftams\\Downloads\\seeds_dataset.txt");
                Apriori apriori = new Apriori(data, 20);
                ArrayList<String[]> items = apriori.calculateFrequentItems();
                ArrayList<ArrayList<String>> condidatesLists = apriori.getCondidatesList();

                /*
                for (String[] item : items) {
                        System.out.print("{");
                        for (String subItem: item) {
                                System.out.print(subItem + ", ");
                        }
                        System.out.print("}");
                        System.out.println("");
                }


                 */

                int i = 0;
                for (ArrayList<String> condidateList: condidatesLists) {
                        System.out.println("C"+ (i+1));
                        for (String item : condidateList) {
                                System.out.print(item + "; ");
                        }
                        System.out.println("");
                        i += 1;
                }



        }
}

