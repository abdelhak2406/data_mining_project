package app.functions;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Extraction_regles {
        int minconf;
        ArrayList<String[]> itemsets;
        ArrayList<ArrayList<String>> dataset;

        public Extraction_regles(ArrayList<Double[]> data,int minconf, ArrayList<String[]> itemsets) {
                this.minconf = minconf;
                this.itemsets = itemsets;
                this.dataset = this.preprocessData(data);
        }

        public ArrayList<Rule> rule_extraction(){
                ArrayList<Rule> final_rules= new ArrayList<>();
                for (int i = 0; i < itemsets.size(); i++) {
                        if (itemsets.get(i).length>=2){
                                ArrayList<ArrayList<String>> arrays= subArray(itemsets.get(i));
                                ArrayList<Rule> initial_rules=get_initial_rules(arrays);
                                for (Rule rule : initial_rules){
                                        Double down=Double.valueOf(get_support_antecedent(rule));
                                        Double up=Double.valueOf(get_support_antecedent_consequent(rule));
                                        rule.confiance= up/down;
                                        if (rule.confiance*100>=minconf){
                                                final_rules.add(rule);
                                        }
                                }
                        }
                }

                return final_rules;

        }

        public int get_support_antecedent(Rule rule){
                int count=0;
                Boolean truth= false;
                for (int i = 0; i < dataset.get(0).size(); i++) {
                        for (int k = 0; k < rule.antecedent.size(); k++) {
                                truth=false;
                                for (int j = 0; j < dataset.size(); j++) {
                                        if (dataset.get(j).get(i).equals(rule.antecedent.get(k))){
                                                truth=true;
                                        }
                                }
                                if(!truth){
                                        break;
                                }
                                else if(truth && k==rule.antecedent.size()-1){
                                        count++;
                                }
                        }

                }
                return count;
        }

        public int get_support_antecedent_consequent(Rule rule){
                int count=0;
                Boolean[] b1=new Boolean[rule.antecedent.size()], b2= new Boolean[rule.consequent.size()];
                for (int i = 0; i < dataset.get(0).size(); i++) {
                        b1=make_array_false(b1);b2=make_array_false(b2);
                        for (int j = 0; j < dataset.size(); j++) {
                                for (int k = 0; k < rule.antecedent.size(); k++) {
                                        if (dataset.get(j).get(i).equals(rule.antecedent.get(k))){
                                                b1[k]=true;
                                        }
                                }

                                for (int k = 0; k < rule.consequent.size(); k++) {
                                        if(dataset.get(j).get(i).equals(rule.consequent.get(k))){
                                                b2[k]=true;
                                        }
                                }

                        }
                        if(verify_everything_true(b1) && verify_everything_true(b2)){
                                count++;
                        }
                }
                return count;
        }

        public Boolean verify_everything_true(Boolean[] b1){
                for (int i = 0; i < b1.length; i++) {
                        if(!b1[i]){
                                return false;
                        }
                }
                return true;
        }

        public Boolean[] make_array_false(Boolean[] bool){
                for (int i = 0; i < bool.length; i++) {
                        bool[i]=false;
                }
                return  bool;
        }

        public ArrayList<ArrayList<String>> preprocessData(ArrayList<Double[]> data){
                ArrayList<ArrayList<String>> descritized_data = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < 7; i++) {
                        descritized_data.add(MainFct.discretisationEqual(this.getColumn(data, i), 4, i+1));
                }
                return descritized_data;
        }

        public ArrayList<Double> getColumn(ArrayList<Double[]> data, int column){
                ArrayList<Double> columnArray = new ArrayList<>();
                for(int i=0 ;i<data.size();i++) {
                        columnArray.add(data.get(i)[column]);
                }
                return  columnArray;
        }

        public ArrayList<Rule> get_initial_rules(ArrayList<ArrayList<String>> arrays){
                ArrayList<Rule> initial_rules= new ArrayList<>();
                for (int j = 0; j < arrays.size(); j++) {
                        for (int k = 0; k < arrays.size(); k++) {
                                ArrayList<String> intersect= intersection(arrays.get(j),arrays.get(k));
                                if (intersect.size()==0){
                                        Rule rule= new Rule(arrays.get(j),arrays.get(k));
                                        initial_rules.add(rule);
                                }
                        }

                }
                return initial_rules;
        }

        public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
                ArrayList<String> list = new ArrayList<String>();

                for (String t : list1) {
                        if(list2.contains(t)) {
                                list.add(t);
                        }
                }

                return list;
        }

        private static ArrayList<ArrayList<String>> subArray(String[] array) {
                ArrayList<ArrayList<String>> items = new ArrayList<>(  );
                int opsize = (int)Math.pow(2, array.length);

                for (int counter = 1; counter < opsize; counter++) {
                        ArrayList<String> terms = new ArrayList<>(  );
                        for (int j = 0; j < array.length; j++) {

                                if (BigInteger.valueOf(counter).testBit(j))
                                        terms.add(array[j]);
                        }
                        if (terms.size() < array.length){
                                items.add(terms);
                        }
                }
                return items;
        }


        public static void main(String[] args) throws Exception{
                ArrayList<Double[]> data= MainFct.readFile("datasets/seeds.txt");
                Apriori apriori = new Apriori(data, 25,4,1);
                ArrayList<String[]> items = apriori.calculateFrequentItems();

                for (String[] item : items ){
                        for ( String str : item){
                                System.out.print(str+" ,");
                        }
                        System.out.println();
                }

                Extraction_regles ex_instance= new Extraction_regles(data,90,items);
                System.out.println(ex_instance.rule_extraction());
        }
}
