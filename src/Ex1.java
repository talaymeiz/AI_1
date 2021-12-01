import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Ex1 {

    static int multip=0;
    static int addit=0;

    public static String[] readfile(){
        String[] xml= new String[1];
        ArrayList<String> datas= new ArrayList();
//        ArrayList<String> elimination= new ArrayList();
        try {
            File file = new File("input.txt");
            Scanner myReader = new Scanner(file);
            xml[0]= myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                datas.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String[] data = datas.toArray(String[]::new);
        return data;
    }

    public static void write(String s) {
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(s);
            myWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Variable> basebollS(String str, Variables variables){

        ArrayList<Variable> var= new ArrayList();

        String[] part1 = str.split("-");
        var.add(variables.getvar(part1[0]));
        String[] part2 = part1[1].split("\\|");
        var.add(variables.getvar(part2[0]));
        if (part2.length>1) {
            String[] part3 = part2[1].split(",");

            for (int i = 0; i < part3.length; i++) {
                String[] part4 = part3[i].split("=");
                var.add(variables.getvar(part4[0]));
            }
        }
        return var;
    }

    public static String baseboll(Variable a, Variable b, Variables variables, String s){

        String[] evs= s.split(",");
        for (int i=0; i<evs.length-1; i++){
            Variable var= variables.getvar(evs[i]);
            var.isgiven();
        }

        boolean chaild=findway(a, b, variables, 1);
        boolean perent=findway(a, b, variables, 0);

        for (int i=0; i<evs.length-1; i++){
            Variable var= variables.getvar(evs[i]);
            var.bakegiven();
        }

        variables.setFrom();

        if (chaild || perent)
            return "no";
        else
            return "yes";
    }

    public static String baseboll(String str, Variables variables){

        ArrayList<Variable> var= basebollS(str,variables);
        Variable a = var.get(0);
        Variable b = var.get(1);

        for (int i=2; i<var.size(); i++){
            var.get(i).isgiven();
        }

        boolean chaild=findway(a, b, variables, 1);
        boolean perent=findway(a, b, variables, 0);

        for (int i=2; i<var.size(); i++){
            var.get(i).bakegiven();
        }

        variables.setFrom();

        if (chaild || perent)
            return "no";
        else
            return "yes";
    }

    public static boolean findway(Variable a, Variable b, Variables variables, int where) {
        boolean flag= false;
        if (a.name.equals(b.name))
            return true;
        if (where==1){
          if (a.whatgiven()==0){
            for (int i=0; i<a.children.size(); i++){
                String achaild= (String) a.children.get(i);
                if (variables.getvar(achaild).fromP==0) {
                    variables.getvar(achaild).fromP=1;
                    flag = findway(variables.getvar(achaild), b, variables, 1);
                    if (flag == true) return true;
                }
            }
          }
          else{
              for (int i=0; i<a.parents.size(); i++){
                  String parent= (String) a.parents.get(i);
                  if (variables.getvar(parent).fromC==0) {
                      variables.getvar(parent).fromC=1;
                      flag = findway(variables.getvar(parent), b, variables, 0);
                      if (flag == true) return true;
                  }
              }
          }
        }
        else if (where==0){
            if (a.whatgiven()==0){
                for (int i=0; i<a.parents.size(); i++){
                    String perent= (String) a.parents.get(i);
                    if(variables.getvar(perent).fromC==0 ) {
                        variables.getvar(perent).fromC=1;
                        flag = findway(variables.getvar(perent), b, variables, 0);
                        if (flag == true) return true;
                    }
                }
                for (int i=0; i<a.children.size(); i++){
                    String achaild= (String) a.children.get(i);
                    if(variables.getvar(achaild).fromP==0 ) {
                        variables.getvar(achaild).fromP=1;
                        flag= findway(variables.getvar(achaild), b, variables, 1);
                        if (flag == true) return true;
                    }
                }

            }
        }
        return flag;
    }

    public static Object[] eliminateS(String str, Variables variables){

        Object[] a=new Object[2]; //query, ev
        ArrayList<Object[]> b=new ArrayList<Object[]>(); //evidance, ev
        ArrayList<Variable> c=new ArrayList<Variable>(); //hidens
        Object[] el =new Object[3];


        str= str.replace("P", ",");
        str= str.replace("(", ",");
        str= str.replace(")", ",");
        str= str.replace("|", ",");
        str= str.replace(" ", ",");

        String[] part= str.split(",");
        String[] parts= new String[part.length-3];
        int k=0;

        for (int i=0; i<part.length; i++){
            if (!(part[i].equals("")) && k<parts.length) {
                parts[k]= part[i];
                k++;
            }
        }

        String[] quiry= parts[0].split("=");
        Variable q= variables.getvar(quiry[0]);
        a[0]=q;
        a[1]=quiry[1];

        if (!(str.charAt(str.indexOf('|')+1)==')')){
            for(int i=1;i<parts.length-1;i++){
                String[] evidance= parts[i].split("=");
                Variable e= variables.getvar(evidance[0]);
                Object[] o=new Object[2];
                o[0]=e;
                o[1]=evidance[1];
                b.add(o);
            }
        }
        else
            b=null;


        String[] hidens= parts[parts.length-1].split("-");
        for (int i=0; i<hidens.length; i++){
            Variable v= variables.getvar(hidens[i]);
            c.add(v);
        }

        el[0]= a;
        el[1]= b;
        el[2]= c;
        return el;
    }

    public static int as(Factor f){
        int num=0;
        for( int i=0; i<f.vars.length; i++){
            for(int j=0; j<f.vars[i].name.length(); j++){
                int as= f.vars[i].name.charAt(j);
                num= num+ as;
            }
        }
        return num;
    }

    public static Factor[] sortF(ArrayList<Factor> factors){

        Factor[] sort= new Factor[factors.size()];
        for (int i=0;i<factors.size(); i++){
            int index=0;
            for (int j=0;j<factors.size(); j++){
                if(factors.get(i).metrix.length>factors.get(j).metrix.length)
                    index++;
            }
            if (sort[index]== null)
                sort[index]= factors.get(i);
            else{
                if (as(sort[index])>as(factors.get(i))){
                    sort[index+1]= sort[index];
                    sort[index]= factors.get(i);
                }
                else
                    sort[index+1]= factors.get(i);
            }
        }
        return sort;
    }

    public static ArrayList<Factor> elimVar(Variable hiden, ArrayList<Factor> factors){

        if (factors.size()==1)
            return  factors;

        Factor[] arrFactors= sortF(factors);
        Factor a= arrFactors[0];
        Factor b= arrFactors[1];
        Factor c= joyn(a,b);
        c= eliminate(c,hiden);
        factors.remove(0);
        factors.remove(1);
        factors.add(c);

        return elimVar(hiden,factors);

    }

    public static boolean isAncestor(Variable a,Variable b, Variables variables){

        boolean flag=false;

        if (a.name.equals(b.name))
            return flag=true;
        else if (a.parents.size()<1)
            flag= false;
        else {
            for (int i = 0; i < a.parents.size(); i++) {
                Variable c = variables.getvar(a.parents.get(i));
                flag = isAncestor(c, b, variables);
                if (flag == true)
                    return flag;
            }
        }
        return flag;
    }

    public static boolean isAncestor(String[] evs,Variable b, Variables variables){

        if (evs[0].equals("")){
            return false;
        }
        for (int i=0; i<evs.length; i++){
            Variable a= variables.getvar(evs[i]);
            if (isAncestor(a,b,variables)){
                return true;
            }
        }
        return false;
    }

    public static String algoEliminate(Variables variables, String str){

        String[] ans= new String[3];
        ArrayList<Factor> factorsList=new ArrayList<>();

        ////////////////////////////////////////
        Object[] a=new Object[2]; //query, ev
        ArrayList<Object[]> b=new ArrayList<Object[]>(); //evidance, ev
        ArrayList<Variable> hiden=new ArrayList<Variable>(); //hidens

        str= str.replace("P", ",");
        str= str.replace("(", ",");
        str= str.replace(")", ",");
        str= str.replace("|", ",");
        str= str.replace(" ", ",");

        String[] part= str.split(",");
        String[] parts= new String[part.length-3];
        int k=0;

        for (int i=0; i<part.length; i++){
            if (!(part[i].equals("")) && k<parts.length) {
                parts[k]= part[i];
                k++;
            }
        }

        String[] quiry= parts[0].split("=");
        Variable q= variables.getvar(quiry[0]);

        a[0]=q;
        a[1]=quiry[1];

        ArrayList<String> evOutcome= new ArrayList<>();
        String evidanceName="";
        if (parts[2]!=null){
            for(int i=1;i<parts.length-1;i++){
                String[] evidance= parts[i].split("=");
                Variable e= variables.getvar(evidance[0]);
                evidanceName= evidanceName+e.name+",";
                Object[] o=new Object[2];
                o[0]=e;
                o[1]=evidance[1];
                evOutcome.add(evidance[1]);
                b.add(o);
            }
        }
        else
            b=null;

        if(parts[2]!=null) {
            String[] hidens = parts[parts.length - 1].split("-");
            for (int i = 0; i < hidens.length; i++) {
                Variable v = variables.getvar(hidens[i]);
                hiden.add(v);
            }
        }
        else{
            String[] hidens = parts[parts.length - 2].split("-");
            for (int i = 0; i < hidens.length; i++) {
                Variable v = variables.getvar(hidens[i]);
                hiden.add(v);
            }
        }
        ////////////////////////////////////////////////////////////////////

        String[] evs= evidanceName.split(","); //arr name of evidance
        ArrayList<Variable> Irrelevant= new ArrayList<>();

        for (int i=0; i<variables.variables.size(); i++){ //faind if the ansuer in a cpt
            Factor f = new Factor(variables.variables.get(i), variables);

            if(ifansinCPT(f,q,evs,variables)){
                return ansinCPT(f,str,variables);
            }
        }

        for (int i=0; i<variables.variables.size(); i++){  //what cpt we need to the query
            boolean AncestorQuery= isAncestor(q,variables.variables.get(i),variables); //Ancestor of query
            boolean AncestorEvs= isAncestor(evs,variables.variables.get(i),variables);  //Ancestor of evidance
            boolean Ancestor= (AncestorQuery||AncestorEvs);
            boolean dependent= "no".equals(baseboll(q,variables.variables.get(i),variables,evidanceName)); // dependent in query with evs
            boolean e= (evidanceName.indexOf(variables.variables.get(i).name)>-1); //if the var is evidance

            if ((Ancestor && dependent) || e) { //if f is Ancestor and baseball
                Factor f = new Factor(variables.variables.get(i), variables);
                for (int j=0; j<evs.length; j++){
                    String s= evs[j];
                    if (isin(f,variables.getvar(s))) {
                        f = remuv(f, variables.getvar(s), evOutcome.get(j)); //remuv evidance from factor
                    }
                }
                factorsList.add(f); //add the factor to the list
            }
            else{
                Variable v = variables.variables.get(i);
                Irrelevant.add(v);
            }
        }
        ArrayList<Factor> fctorsList2= new ArrayList<>();
        for (int j = 0; j < factorsList.size(); j++) {
            fctorsList2.add(factorsList.get(j));
        }
        for (int i = 0; i < fctorsList2.size(); i++) {     //remove the factors who include irreletiv variabel
            for (int j = 0; j < Irrelevant.size(); j++) {
                if(isin(fctorsList2.get(i),Irrelevant.get(j))){
                    factorsList.remove(fctorsList2.get(i));
                }
            }
        }

        for(int i=0; i<hiden.size(); i++){

            ArrayList<Factor> fctorsList1= new ArrayList<>();
            for (int j = 0; j < factorsList.size(); j++) {
                fctorsList1.add(factorsList.get(j));
            }

            ArrayList<Factor> Fhiden= new ArrayList<>();
            int j = 0;
            for (j = 0; j < fctorsList1.size(); j++){
                if (isin(fctorsList1.get(j),hiden.get(i))){
                    Fhiden.add(fctorsList1.get(j));
                    factorsList.remove(fctorsList1.get(j));
                }
            }
            if (Fhiden.size() > 0) {
                while (Fhiden.size() > 1) {  //run on factor of hidens, until there is only one
                    Factor[] fList = sortF(Fhiden);  //sort factor of hidens
                    Factor factor = joyn(fList[0], fList[1]); //join on the first
                    Fhiden.remove(fList[0]);
                    Fhiden.remove(fList[1]);
                    Fhiden.add(factor); //add the joyn one to factor of hidens
                    int y=1;
                }
                Factor f = eliminate(Fhiden.get(0), hiden.get(i)); //eliminate the one how left
                factorsList.add(f); //add him bake to factors List
                }
        }

        while (factorsList.size() > 1) {  //run on factor of hidens, until there is only one
            Factor[] fList = sortF(factorsList);  //sort factor of hidens
            Factor factor = joyn(fList[0], fList[1]); //join on the first
            factorsList.remove(fList[0]);
            factorsList.remove(fList[1]);
            factorsList.add(factor); //add the joyn one to factor of hidens
        }

        ////////////////////
        Factor ff= factorsList.get(0);
        double sum=0;
        double an=0;
        for (int i=0;i<ff.metrix.length; i++){
            sum=sum+ (double) ff.metrix[i][1];
            addit++;
            if (ff.metrix[i][0].equals(quiry[1])){
                an =(double) ff.metrix[i][1];
            }
        }
        ans[0]= String.format("%.5f", an/sum);
        ans[1]= String.valueOf(addit-1); //because he take one more in the normalization, 6 rows above
        ans[2]= String.valueOf(multip);

        addit=0;
        multip=0;
        String answer=ans[0]+","+ans[1]+","+ans[2];
        return answer;
    }

    public static String ansinCPT(Factor cpt, String str, Variables variables){

        Object[] a=new Object[2]; //query, ev
        ArrayList<Variable> b1=new ArrayList<>(); //evidance
        ArrayList<String> b2=new ArrayList<>(); //outcome

        str= str.replace("P", ",");
        str= str.replace("(", ",");
        str= str.replace(")", ",");
        str= str.replace("|", ",");
        str= str.replace(" ", ",");

        String[] part= str.split(",");
        String[] parts= new String[part.length-3];
        int k=0;
        for (int i=0; i<part.length; i++){
            if (!(part[i].equals("")) && k<parts.length) {
                parts[k]= part[i];
                k++;
            }
        }

        String[] quiry= parts[0].split("=");
        Variable q= variables.getvar(quiry[0]);
        a[0]=q;
        a[1]=quiry[1];

        ArrayList<String> evOutcome= new ArrayList<>();
        String evidanceName="";
        if (parts[2]!=null){
            for(int i=1;i<parts.length-1;i++){
                String[] evidance= parts[i].split("=");
                Variable e= variables.getvar(evidance[0]);
                b1.add(e);
                b2.add(evidance[1]);

            }
        }
        else {
            b1 = null;
            b2 = null;
        }
        String s="";

        for(int i=0; i<cpt.vars.length; i++){
            String e=cpt.vars[i].name;
            int flagflag=0;
            for (int j=0; j<b1.size(); j++){
                if(e.equals(b1.get(j).name)){
                    s=s+b2.get(j);
                }
                if(q.name.equals(e)&& flagflag==0){
                    s=s+quiry[1];
                    flagflag++;
                }
            }
        }

        double ans=-1;
        for(int i=0; i<cpt.metrix.length; i++){

            String row= intoStr(cpt.metrix[i]);
            if (s.equals(row)){
                ans= (double) cpt.metrix[i][cpt.metrix[0].length-1];
            }
        }
        String[] an= new String[3];
        an[0]= String.format("%.5f", ans);
        an[1]= String.valueOf(0.0); //because he take one more in the normalization, 6 rows above
        an[2]= String.valueOf(0.0);

        addit=0;
        multip=0;
        String answer=an[0]+","+an[1]+","+an[2];
        return answer;
    }

    public static boolean ifansinCPT(Factor cpt, Variable query, String[] ev, Variables variables){

        Variable[] evs=new Variable[ev.length];

        for(int i=0; i<evs.length;i++){
            evs[i]=variables.getvar(ev[i]);
        }
        int numcpt= cpt.vars.length;
        int numsh= evs.length+1;
        if(numcpt!=numsh){
            return false;
        }
        if(evs[0]==null){
            if(query.name.equals(cpt.vars[0].name) && numcpt==1){
                return true;
            }
            else{
                return false;
            }
        }

        int flag=0;
        for (int i=0; i<cpt.vars.length; i++){
            String namecpt= cpt.vars[i].name;
            for (int j=0; j<evs.length; j++){
                String namesh= evs[j].name;
                if(namesh.equals(namecpt)){
                    flag++;
                }
            }
            if(query.name.equals(namecpt))
                flag++;
        }
        if (flag==numcpt){
            return true;
        }
        return false;
    }

    public static boolean isin(Factor factor, Variable a){

        if(a==null){
            return false;
        }
        for (int i=0; i<factor.vars.length; i++){
            if (a.name.equals(factor.vars[i].name))
                return true;
        }
        return false;
    }

     public static Factor remuv(Factor factor, Variable a, String ev){

        Object[][] metrix= new Object[factor.metrix.length/a.outcome.size()][factor.metrix[0].length-1];
        Variable[] vars= new Variable[factor.vars.length-1];

        int place=-1;
         int l=0;
         for (int i=0; i<factor.vars.length; i++){
             if (factor.vars[i].name.equals(a.name))
                 place=i;
             else{
                 Variable f= factor.vars[i];
                 vars[l]= f;
                 l++;
             }
         }
         l=0;
         for (int i=0; i<factor.metrix.length; i++){
            if (factor.metrix[i][place].equals(ev)){
                int k=0;
                for (int j=0; j<factor.metrix[0].length; j++){
                    if(j!=place && l<metrix.length && k<metrix[0].length){
                        metrix[l][k]=factor.metrix[i][j];
                        k++;
                    }
                }
                l++;
            }
        }

        Factor f= new Factor(metrix,vars);
        return f;
     }

    public static Factor joyn(Factor a, Factor b){//Factor

        ArrayList<Variable> vars= new ArrayList();
        HashMap<String, int[]> where = new HashMap<String, int[]>(); // HashMap hold the factor and the index in vars of the factor for every variabel
        HashMap<String, Integer> whereMet = new HashMap<String, Integer>();  // HashMap hold the colomn of variable in the new matrix

        for (int i=0; i<a.vars.length; i++) { //creat the list of variabels of the new factor
            int flag = 0;
            for (int j = 0; j < b.vars.length; j++) {
                if (a.vars[i].name.equals(b.vars[j].name))
                    flag++;
            }
            if (flag == 1) {
                int[] h = {0, i};
                where.put(a.vars[i].name, h);
            } else {
                int[] h = {1, i};
                where.put(a.vars[i].name, h);
            }
            vars.add(a.vars[i]);
        }
        for (int i=0; i<b.vars.length; i++) {
            int flag = 0;
            for (int j = 0; j < a.vars.length; j++) {
                if (b.vars[i].name.equals(a.vars[j].name)) {
                    flag++;
                }
            }
            if (flag==0){
                int[] h = {2, i};
                where.put(b.vars[i].name, h);
                vars.add(b.vars[i]);
            }
        }

        int rows = 1;
        for (int i=0; i<vars.size(); i++){  //num of rows of new factor
            rows = rows*vars.get(i).outcome.size();
        }
        Object[][] metrix= new Object[rows][vars.size()+1];

        int f=1;
        for (int i=0; i<vars.size(); i++){  //feel the new joyn with the outcoms of the vers
            int x=0;
            Variable v= vars.get(i);
            for (int j=0; j<rows; j++){
                if (j % f == 0)
                    x =x+1;
                metrix[j][i]= v.outcome.get((x-1)%v.outcome.size());
            }
            f=f*v.outcome.size();
        }

        for (int i=0; i<vars.size(); i++)
            whereMet.put(vars.get(i).name, i);

        for (int i=0; i<a.metrix.length; i++){ // fell the new valeus in the new matrix
            for(int i1=0; i1<metrix.length; i1++) {
                int flag=0;
                for (int j = 0; j < a.metrix[0].length - 1; j++) {
                    String column= a.vars[j].name;
                    String val= (String) a.metrix[i][j];
                    int index= whereMet.get(column);
                    String valMet= (String) metrix[i1][index];
                    if(!val.equals(valMet))
                        flag++;
                }
                if (flag ==0)
                    metrix[i1][metrix[0].length-1]= a.metrix[i][a.metrix[0].length-1];
            }
        }
        for (int i=0; i<b.metrix.length; i++){
            for(int i1=0; i1<metrix.length; i1++) {
                int flag=0;
                for (int j = 0; j < b.metrix[0].length - 1; j++) {
                    String column= b.vars[j].name;
                    String val= (String) b.metrix[i][j];
                    int index= whereMet.get(column);
                    String valMet= (String) metrix[i1][index];
                    if(!val.equals(valMet))
                        flag++;
                }
                if (flag ==0) {
                    double a1= (double) metrix[i1][metrix[0].length - 1];
                    double a2= (double) b.metrix[i][b.metrix[0].length - 1];
                    metrix[i1][metrix[0].length - 1] = a1*a2;
                    multip++;
                }
            }
        }

        Variable[] var=new Variable[vars.size()];
        for (int i=0; i<vars.size(); i++){
            var[i]=vars.get(i);
        }

        Factor factor= new Factor(metrix,var);
        return factor;
        }

    public static String intoStr(Object[] arr){
        String str="";
        for (int i=0; i< arr.length-1; i++){
            str= str+arr[i];
        }
        return str;
    }

    public static Factor eliminate(Factor factor, Variable a) {//Factor
        Object[][] metrix1 = new Object[factor.metrix.length][factor.metrix[0].length - 1];  //remov from the metrix the colomn of the var we eliminate
        int placeA = -1;
        for (int i = 0; i < factor.vars.length; i++) { //place of a in vers
            if (a.name.equals(factor.vars[i].name))
                placeA = i;
        }
        for (int i = 0; i < factor.metrix.length; i++) {  // build the vers of the new factor without a
            for (int j = 0; j < factor.metrix[0].length; j++) {
                if (j < placeA)
                    metrix1[i][j] = factor.metrix[i][j];
                if (j > placeA)
                    metrix1[i][j-1] = factor.metrix[i][j];
            }
        }

        int numOutcome= a.outcome.size(); // how many outcoms in a
        Object[][] metrix2= new Object[metrix1.length/numOutcome][metrix1[0].length]; //the matrix of the new factor(after the elimination)
        for (int i = 0; i < metrix1.length; i++) {  //enter the valeus to metrix2
            String name= intoStr(metrix1[i]);
            double sum=0;
            for (int j = 0; j < metrix1.length; j++) {
                String namej = intoStr(metrix1[j]);
                if (name.equals(namej)) {
                    double n = (double) metrix1[j][metrix1[0].length - 1];
                    sum = sum + n;
                }
            }
            metrix1[i][metrix1[0].length - 1]=sum;
            addit++;
        }

        int l=0;
        for (int i = 0; i < metrix1.length; i++) {
            int h=0;
            String name1= intoStr(metrix1[i]);
            for (int k = 0; k < l; k++) {
                String name2= intoStr(metrix2[k]);
                if(name1.equals(name2)){
                    h++;
                }
            }
            if(h==0) {
                for (int j = 0; j < metrix1[0].length; j++) {
                    metrix2[l][j] = metrix1[i][j];
                }
                l++;
            }
        }
        int k=0;
        Variable[] vars= new Variable[factor.vars.length-1]; //the vars of the new factor to array
        for (int i=0; i<factor.vars.length; i++){
            if(! factor.vars[i].name.equals(a.name)){
                vars[k]=factor.vars[i];
                k++;
            }
        }

        Factor f=new Factor(metrix2, vars);
        return f;
    }

    private static final String FILENAME = "C:\\Users\\talia\\year2\\BM/big_net.xml";

    public static void main(String[] args) {

            String[] data= readfile();//C:\Users\talia\IdeaProjects\BM\src\

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(FILENAME));
                //Document doc = db.parse(new File(data[0][0]));
                doc.getDocumentElement().normalize();

                NodeList list_var = doc.getElementsByTagName("VARIABLE");
                NodeList list_def = doc.getElementsByTagName("DEFINITION");
                Variables variables= new Variables();

                for (int temp = 0; temp < list_var.getLength(); temp++) {

                    Node node1 = list_var.item(temp);
                    Node node2 = list_def.item(temp);

                    Variable variable = new Variable(node1, node2);
                    for (int i=0; i<variable.parents.size(); i++){

                    }

                    variables.addvar(variable);
                }
                variables.addchaild();

                String ans="";
                for (int i=0; i<data.length; i++){
                    String s=data[i];
                    if(s.charAt(0)=='P'){
                        String eliminate= algoEliminate(variables,s);
                        ans= ans+ eliminate+ "\n";
                    }
                    else {
                        String baseboll= baseboll(s, variables);
                        ans= ans+ baseboll+ "\n";
                    }
                }
                write(ans);

//                Factor f1= new Factor(variables.getvar("C3"),variables);
//                System.out.println(f1);
//                Factor f2= new Factor(variables.getvar("A2"),variables);
//                System.out.println(f2);
//                Factor f3= new Factor(variables.getvar("B1"),variables);
//                System.out.println(f3);
//
//                ArrayList<Factor> f= new ArrayList<>();
//                f.add(f1);
//                f.add(f2);
//                f.add(f3);
//
//                Factor[] arr= sortF(f);
//                for(int i=0; i<arr.length; i++){
//                    System.out.println(arr[i].query.name);
//                }

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }

        }

    }