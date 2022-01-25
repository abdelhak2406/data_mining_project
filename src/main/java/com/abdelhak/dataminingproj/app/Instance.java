package com.abdelhak.dataminingproj.app;

public class Instance {
        public int id;
        public String field1;
        public String field2;
        public String field3;
        public String field4;
        public String field5;
        public String field6;
        public String field7;
        public String classe;

        public Instance(int id, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String classe) {
                this.id = id;
                this.field1 = field1;
                this.field2 = field2;
                this.field3 = field3;
                this.field4 = field4;
                this.field5 = field5;
                this.field6 = field6;
                this.field7 = field7;
                this.classe = classe;
        }

        public int getId() {
                return id;
        }

        public String getField1() {
                return field1;
        }

        public String getField2() {
                return field2;
        }

        public String getField3() {
                return field3;
        }

        public String getField4() {
                return field4;
        }

        public String getField5() {
                return field5;
        }

        public String getField6() {
                return field6;
        }

        public String getField7() {
                return field7;
        }

        public String getClasse() {
                return classe;
        }
}
