import java.util.ArrayList;

public class Variables {

    ArrayList<Variable> variables= new ArrayList<Variable>();
    private int multip;
    private int addit;

    public int getMultip() {
        return multip;
    }

    public void setMultip() {
        this.multip++;
    }

    public int getAddit() {
        return addit;
    }

    public void setAddit() {
        this.addit++;
    }

    public Variables(){
        this.addit=0;
        this.multip=0;
    }
    public Variables(ArrayList<Variable> var){
        this.variables= new ArrayList<Variable>(var);
        this.addit=0;
        this.multip=0;
    }

    public void addvar(Variable v){
        this.variables.add(v);
    }

    public Variable getvar(String name){
        for (int i=0; i<this.variables.size(); i++){
            String str= this.variables.get(i).name;
            if (name.equals(str)){
                return this.variables.get(i);
            }
        }
        return null;
    }

    public void addchaild(Variable v){
        for(int j=0; j<v.parents.size(); j++) {
            String perent = (String) v.parents.get(j);
            if (this.getvar(perent) != null)
                this.getvar(perent).addchaild(v.name);
        }
    }

    public void addchaild(){
        for(int i=0; i<this.variables.size(); i++){
            Variable that= this.variables.get(i);
            for(int j=0; j<this.variables.get(i).parents.size(); j++) {
                String perent = (String) that.parents.get(j);
                if (this.getvar(perent) != null)
                    this.getvar(perent).addchaild(that.name);
            }
        }
    }

    public void setFrom(){
        for(int i=0; i<this.variables.size(); i++){
            Variable that= this.variables.get(i);
            that.fromP=0;
            that.fromC=0;
        }
    }

    @Override
    public String toString(){
        String str="";
        for (int i=0; i<this.variables.size(); i++){
            str= str+ this.variables.get(i).toString();
        }
        return str;
    }
}

