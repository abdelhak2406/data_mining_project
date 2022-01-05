package app.functions;

import scala.Int;
import smile.neighbor.lsh.Hash;

import java.lang.reflect.Array;
import java.util.*;

public class Eclat {
        //TODO: try to print the actual list in every step of the process.
        public ArrayList<ArrayList<String>> itemset;
        public  int minSup;

        public Eclat (ArrayList<Double[]> data, int support){
                //TODO: -non priority- coud've done some inheritance here

                //Needs the discretized dataset Arraylist<String[ ] >
                // suport is supposed to be given as a %
                this.itemset = this.preprocessData(data);//itemset.get(i) gets you the column 'i' content (a table of 210 strings)
                this.minSup = itemset.get(0).size()*support/100;
        }

        public ArrayList<ArrayList<String>> preprocessData(ArrayList<Double[]> data){
                /**
                 * take the data and get the discretized one!
                 *
                 */
                ArrayList<ArrayList<String>> descritized_data = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < 7; i++) {
                        descritized_data.add(MainFct.discretisationEqual(this.getColumn(data, i), 4, i+1));
                }
                return descritized_data;
        }

        public Boolean isInDocument(String item , int document) {
                //document is just a fancy way of saying ligne
                for (int i = 0; i < 7; i++) {
                        if (this.itemset.get(i).get(document).equals(item)){
                                return true;
                        }
                }
                return  false;
        }

        public ArrayList<Double> getColumn(ArrayList<Double[]> data, int column){
                ArrayList<Double> columnArray = new ArrayList<>();
                for(int i=0 ;i<data.size();i++) {
                        columnArray.add(data.get(i)[column]);
                }
                return  columnArray;
        }

        public  ArrayList<Boolean> createArayZero(){
                ArrayList<Boolean> ar = new ArrayList<>();
                for (int i = 0; i < this.itemset.get(0).size(); i++) {
                        ar.add(false) ;
                }
                return  ar;
        }

        public  HashMap<String,ArrayList<Boolean> > getVerticalMat(){
                /** Create the verticale representation matrix!!
                 * <item :  <list of Booleans> > index rep num de document (num de colonne)
                 * <list of booleans > is of size (210) aka the num of lines/documents:
                 */
                HashMap<String, ArrayList<Boolean> > listItems = new HashMap();

                // we just need to loop through the whole dataset here
                //get(0) because we dont care and we know all the column have 210 lines
                for (int i = 0; i < this.itemset.size(); i++) {//itemset.size()= 7
                        // add all the itemset of the ligne if they dont exist!!
                        for (int j = 0; j < this.itemset.get(i).size(); j++) {
                                // check it the item exists! otherwise create an entry for it.
                                if (listItems.get(this.itemset.get(i).get(j)) == null) {
                                        ArrayList<Boolean> tmp = createArayZero();
                                        listItems.put(this.itemset.get(i).get(j),tmp);
                                }
                        }
                }


                int docCounter = 0;
                // we loop through the matrix and put true(1) if the item apears in the i'th document
                for (Map.Entry<String, ArrayList<Boolean>> item : listItems.entrySet()) {

                        for (int i = 0; i < this.itemset.get(0).size(); i++) {//for every doc, donc ligne
                                // check si li'tem existe
                                if (isInDocument(item.getKey(), i)) {
                                        //mettre 1 a l'index doucment
                                        ArrayList<Boolean> tmp = item.getValue();
                                        tmp.set(i,true);
                                        listItems.put(item.getKey(), tmp);
                                }
                        }

                        //item.getKey();
                        //item.getValue();
                        // Printing all elements of a Map

                }

                return  listItems;


        }

        public void createList(){
                // Hashmap<String, Arraylist<> 210 taille>
                //
        }

        public int calculateSupport(ArrayList<Boolean> listDoc){
                /**
                 *
                 */
                int res =0;
                for (Boolean elem:listDoc) {
                        if (elem){
                                res = res + 1;
                        }
                }
                return res;
        }

        public String[] getAllKeys(HashMap< String, ArrayList<Boolean>> mat){

                String[] listKeys = new String[mat.size()];
                int i =0;
                for (Map.Entry<String, ArrayList<Boolean>> item : mat.entrySet()) {
                        listKeys[i] = item.getKey();
                        i++;
                }
                return listKeys;
        }

        public int calculSupportNCandidats( HashMap< String, ArrayList<Boolean>> mat, String[] candidat ){
                /**
                 * Calculer support pour un  candidats (quand c'est plusieurs truc=
                 * @param candidat une liste d'item
                 */
                int res= 0;

                for (int i = 0; i < this.itemset.get(0).size(); i++) {//on sais qu'il ont tous la meem taille d'ou le get(0)
                        int k ;
                        for ( k = 0; k < candidat.length; k++) {
                                //si pour tout les candidats c'est true alors on incremente
                                // sinon non!
                                //mat.get(candidat[k]).get(i);//true or false
                               if( mat.get(candidat[k]).get(i) == false) { //si cest faux
                                     break;
                               }
                        }
                        //si il a traverser tt la liste sa veut dire que tous sont correct
                        if ( k >= candidat.length){
                            res ++;
                        }
                }

                return res;
        }
        public  Boolean isInListCandidates(String item , List<String []> listCand){
                /**
                 * you give the list of candidates and an item
                 * and it tells you if the item is one of them or not.
                 */
                for (int i = 0; i < listCand.size(); i++) {
                        for (int j = 0; j < listCand.get(i).length ; j++) {
                                if ( listCand.get(i)[j].equals(item)){
                                        return true;
                                }
                        }
                }
                return false;
        }
        public   HashMap< String, ArrayList<Boolean>> deleteUselessItems( HashMap< String, ArrayList<Boolean>> curentMat,
                                                               List<String[]> items){

                ArrayList<String> deleteInd = new ArrayList<>();

                for (Map.Entry<String, ArrayList<Boolean>> item : curentMat.entrySet()) {
                        if ( ! isInListCandidates(item.getKey(), items ) ){
                                deleteInd.add(item.getKey());
                        }
                }
                for (int i = 0; i < deleteInd.size(); i++) {
                        curentMat.remove(deleteInd.get(i));
                }
                return curentMat;

        }

        public void executEclat(){

                HashMap< String, ArrayList<Boolean>> verticalMat = getVerticalMat();

                ArrayList<String> toDelete = new ArrayList<>();
                // supprimer les item ayant support < minSupport afin de genere L1
                for (Map.Entry<String, ArrayList<Boolean>> item : verticalMat.entrySet()) {
                        ArrayList<Boolean> listDoc = item.getValue();
                        // calculer support
                        int support = calculateSupport(listDoc);
                        if ( support < this.minSup ){
                                // supprimer l'item
                                toDelete.add(item.getKey()) ;
                                //verticalMat.remove(item.getKey());
                        }
                        //item.getKey();
                        //item.getValue();
                }
                // delete les truc non candidats
                for (int i = 0; i < toDelete.size(); i++) {
                        verticalMat.remove(toDelete.get(i));
                }

                // verticalMat now contains L1
                System.out.println("LISTE L1");
                for (Map.Entry<String, ArrayList<Boolean>> item : verticalMat.entrySet()) {
                        System.out.print(item.getKey()+", ");
                }
                System.out.println("\n------------FIN L1--------------------\n");



                // TODO: now we need to understand the next steps!!
                // okay now we need to get all the Lks

                int k =2;
                while (true){
                        // dbred les combinaisoin possible de k
                        // get all the items (donc keys sous forme de String[]!
                        String[] allKeys = getAllKeys(verticalMat);
                        List<String[]> listCandidates = this.generateItemCandidates(allKeys,k); //
                        List<String[]> listItemFreq = new ArrayList<>();

                        // pour chaque candidats calculer le support!
                        ArrayList<Integer> toDeleteIndex = new ArrayList<>() ;
                        for (int i = 0; i < listCandidates.size(); i++) {
                               //calculer le support!
                                int res = calculSupportNCandidats(verticalMat, listCandidates.get(i) );
                                if (res >= this.minSup){
                                        listItemFreq.add(listCandidates.get(i)) ;

                                        //supprimer de la liste listCandidates
                                        // on vas l'ajouter a la liste puis supp en bas
                                        //toDeleteIndex.add(i);
                                }

                        }

                        // suprimer!
                        /*
                        for (int i = 0; i < toDeleteIndex.size(); i++) {
                                int delInd = toDeleteIndex.get(i);
                                listCandidates.remove( delInd );
                        }
                         */

                        // at this point nous avons la liste des item frequent pour k=2
                        //afficher contenu!
                        System.out.println("liste L"+k+"----------------");
                        for (int i = 0; i < listItemFreq.size(); i++) {
                                for (int j = 0; j < listItemFreq.get(i).length; j++) {
                                        System.out.print(listItemFreq.get(i)[j]+", ");
                                }
                                System.out.println("");
                        }
                        System.out.println("----------Fin L"+k+"---------------------------");
                        if (listItemFreq.size() == 0 ){
                               break;
                        }
                        verticalMat = deleteUselessItems(verticalMat,listItemFreq);
                        toDeleteIndex.clear();
                        listItemFreq.clear();
                        k++;
                }

                //check si c'est sup ou égale sinon nahi


                // check si le support est >= a minSup
                // si c'est le cas laisser, sinon supprimer


        }
        String[] getSubset(String[] input, int[] subset) {
                // generate actual subset by index sequence
                String[] result = new String[subset.length];
                for (int i = 0; i < subset.length; i++)
                        result[i] = input[subset[i]];
                return result;
        }

        //----------------------------------
        //----------------------------------
        //----------------------------------



        //----------------------------------
        //----------------------------------
        //----------------------------------
        //----------------------------------




        public List<String[]>  generateItemCandidates(String[] listItems, int k){
                /**
                 * you give a list of items and k
                 * and it gives you the list of possibilities
                 */
                // listItems = {10, 20, 30};    // input array
                //int k = 2;                             // sequence length

                List<String[]> subsets = new ArrayList<>();

                int[] s = new int[k];                  // here we'll keep indices
                // pointing to elements in input array

                if (k <= listItems.length) {
                        // first index sequence: 0, 1, 2, ...
                        for (int i = 0; (s[i] = i) < k - 1; i++);
                        subsets.add(this.getSubset(listItems, s));
                        for(;;) {
                                int i;
                                // find position of item that can be incremented
                                for (i = k - 1; i >= 0 && s[i] == listItems.length - k + i; i--);
                                if (i < 0) {
                                        break;
                                }
                                s[i]++;                    // increment this item
                                for (++i; i < k; i++) {    // fill up remaining items
                                        s[i] = s[i - 1] + 1;
                                }
                                subsets.add(this.getSubset(listItems, s));
                        }
                }
                return  subsets;
        }

        public static void main(String[] args) throws Exception {
                //TODO: maybe add the 8th column if needed
                ArrayList<Double[]> data = MainFct.readFile("datasets/seeds_dataset.txt");
                Eclat eclat = new Eclat(data, 20);
                eclat.executEclat();
                System.out.println(eclat.itemset.size());
                /*
                        //test that method
                String [] listItems = {"a", "b", "c"};    // input array
                List<String[]> a = eclat.generateItemCandidates(listItems,2);
                for (int i = 0; i < a.size(); i++) {
                        for (int j = 0; j < a.get(i).length; j++) {
                                System.out.print(" "+a.get(i)[j]);
                        }
                        System.out.println("");
                }
                */
                //-----------------------

        }

}
