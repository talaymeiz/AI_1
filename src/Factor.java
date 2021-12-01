import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Factor {

    Object[][] metrix;
    Variable[] vars;


    String[][] cpt;
    double[] valeus;
    Variable[] perent;
    Variable query;


    public Factor(Object[][] metrix,Variable[] vars){
        this.vars= vars;
        this.metrix= metrix;
    }

    public Factor(Variable a, Variables variables){

        this.query = a;
        this.perent = new Variable[a.parents.size()];
        int rows = 1;

        for (int i=0; i<a.parents.size(); i++){
            String namePerent= a.parents.get(i);
            Variable v= variables.getvar(namePerent);
            this.perent[i]=v;
            rows = rows*v.outcome.size();
        }
        rows = rows*a.outcome.size();
        int flag=0;

        this.valeus= new double[rows];

        String[] num= a.cpt.split(" ");
        for (int i=0; i<num.length; i++){
            this.valeus[i]= Double.parseDouble(num[i]);
        }
        this.cpt= new String[rows][a.parents.size()+1];

        int f=1;
        int y=0;
        for (int i=0; i<rows; i++){
            if (i % f == 0)
                y =y+1;
            cpt[i][cpt[0].length-1]= a.outcome.get((y-1)%a.outcome.size());
        }
        f=f*a.outcome.size();

        for (int j=cpt[0].length-2; j>-1; j--){
            y=0;
            Variable v= this.perent[j];
            System.out.println(v.name);
            System.out.println("colmn:"+j);
            for (int i=0; i<rows; i++){
                if (i % f == 0)
                    y =y+1;
                cpt[i][j]= v.outcome.get((y-1)%v.outcome.size());
            }
            f=f*v.outcome.size();
        }
        this.metrix= new Object[this.cpt.length][this.cpt[0].length+1];
        for (int i=0; i<this.cpt.length; i++) {
            for (int j=0; j<this.cpt[0].length; j++) {
                this.metrix[i][j]= this.cpt[i][j];
            }
            this.metrix[i][this.metrix[0].length-1]= this.valeus[i];
        }
        this.vars = new Variable[this.perent.length+1];
        this.vars[this.vars.length-1]=this.query;
        for (int i=0; i<this.vars.length-1; i++) {
            this.vars[i]=this.perent[i];
        }

    }

    @Override
    public String toString(){
        String metrixv= "";
        for(int i=0; i<this.metrix.length; i++)
            metrixv= metrixv+Arrays.toString(this.metrix[i])+"\n";

        String metrixp= "";
        for(int i=0; i<this.vars.length; i++)
            metrixp= metrixp+this.vars[i].name+",";

        String name="";
        if (!(this.query == null))
            name= this.query.name;

        String str= name +":" +"\n"+"variabels:"+ metrixp+"\n"+ metrixv;
        return str;
    }

}


