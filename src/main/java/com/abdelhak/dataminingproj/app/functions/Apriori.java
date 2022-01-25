package com.abdelhak.dataminingproj.app.functions;

import scala.Array;
import scala.Int;

import java.util.*;

public class Apriori {

        public ArrayList<ArrayList<String>> itemset;
        public int minSup;
        public int discretizationQ; // = 1 if equal discretization and 2 if effective discretization
        public int discretizationType;
        public HashMap<String, Integer> frequentItemsList = new HashMap<>();
        public ArrayList<ArrayList<String>> condidatesList = new ArrayList<>();


        public Apriori(ArrayList<Double[]> data, int support, int discretizationQ, int discretizationType){
            this.discretizationQ = discretizationQ;
            this.discretizationType = discretizationType;
            this.itemset = this.preprocessData(data);
            this.minSup = itemset.get(0).size()*support/100;
        }

        public ArrayList<ArrayList<String>> preprocessData(ArrayList<Double[]> data){
                ArrayList<ArrayList<String>> descritized_data = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < 7; i++) {
                    if (this.discretizationType == 1){
                            descritized_data.add(MainFct.discretisationEqual(this.getColumn(data, i), this.discretizationQ, i+1));
                    }else{
                            descritized_data.add(MainFct.discretisationEffectif(this.getColumn(data, i), this.discretizationQ, i+1));
                    }

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
                    String[] items = new String[this.discretizationQ];
                    for (int j = 0; j < this.discretizationQ; j++) {
                        items[j] = "I" + (i+1) + "" + (j+1);
                    }
                        for (String item: items) {
                                int frequence = 0;
                                for (int j = 0; j < this.itemset.get(i).size(); j++) {
                                        if (this.itemset.get(i).get(j).equals(item)){
                                                frequence += 1;
                                        }
                                }
                                firstCondidates.add(item);
                                if (frequence >= this.minSup){
                                        this.frequentItemsList.put(item, frequence);
                                        frequentCondidates.add(item);
                                }
                        }
                }
                this.condidatesList.add(firstCondidates);
                firstCondidates = frequentCondidates;
                int iteration = 0;
                while(frequentCondidates.size() > iteration) {
                    if (iteration > 0){
                        this.condidatesList.add(frequentCondidates);
                    }
                        frequentCondidates = this.generateItemCandidates(firstCondidates, iteration+2);
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

        public ArrayList<String> generateItemCandidates(ArrayList<String> listItems, int k){
                /**
                 * you give a list of items and k
                 * and it gives you the list of possibilities
                 */
                // listItems = {10, 20, 30};    // input array
                //int k = 2;                             // sequence length
                String [] listItem = new String[listItems.size()];
                listItem = listItems.toArray(listItem);
                List<String[]> subsets = new ArrayList<>();

                int[] s = new int[k];                  // here we'll keep indices
                // pointing to elements in input array

                if (k <= listItem.length) {
                        // first index sequence: 0, 1, 2, ...
                        for (int i = 0; (s[i] = i) < k - 1; i++);
                        subsets.add(this.getSubset(listItem, s));
                        for(;;) {
                                int i;
                                // find position of item that can be incremented
                                for (i = k - 1; i >= 0 && s[i] == listItem.length - k + i; i--);
                                if (i < 0) {
                                        break;
                                }
                                s[i]++;                    // increment this item
                                for (++i; i < k; i++) {    // fill up remaining items
                                        s[i] = s[i - 1] + 1;
                                }
                                subsets.add(this.getSubset(listItem, s));
                        }
                }

                ArrayList<String> condidatesList = new ArrayList<>();
                for (String[] items: subsets) {
                        String newItem = "";
                        for (String item: items) {
                                newItem += item + ",";
                        }
                        newItem = newItem.substring(0, newItem.length()-1);
                        condidatesList.add(newItem);
                }
                return  condidatesList;
        }

        String[] getSubset(String[] input, int[] subset) {
                // generate actual subset by index sequence
                String[] result = new String[subset.length];
                for (int i = 0; i < subset.length; i++)
                        result[i] = input[subset[i]];
                return result;
        }

        public ArrayList<ArrayList<String>> getCondidatesList(){
                return this.condidatesList;
        }


        public static void mainTest(String[] args) throws Exception {
                /**
                 * If we want to test it her's how!
                 * just change the name mainTest to main and execute it.
                 */
                /*
                ArrayList<Double[]> data= MainFct.readFile("C:\\Users\\Raouftams\\Downloads\\seeds_dataset.txt");
                Apriori apriori = new Apriori(data, 20);
                ArrayList<String[]> items = apriori.calculateFrequentItems();
                ArrayList<ArrayList<String>> condidatesLists = apriori.getCondidatesList();


                for (String[] item : items) {
                        System.out.print("{");
                        for (String subItem: item) {
                                System.out.print(subItem + ", ");
                        }
                        System.out.print("}");
                        System.out.println("");
                }



                int i = 0;
                for (ArrayList<String> condidateList: condidatesLists) {
                        System.out.println("C"+ (i+1));
                        for (String item : condidateList) {
                                System.out.print(item + "; ");
                        }
                        System.out.println("");
                        i += 1;
                }



         */

        }
}

