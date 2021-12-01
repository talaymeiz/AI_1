//import jdk.internal.icu.text.UnicodeSet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Variable {

    String name;
    ArrayList<String> outcome= new ArrayList();
    ArrayList<String> parents= new ArrayList();
    ArrayList<String> children= new ArrayList();
    //Factor cpt;
    String cpt;
    private int given;
    int fromP;
    int fromC;

    public Variable(Node a, Node b){
        Element elementa = (Element) a;
        Element elementb = (Element) b;

        this.name= elementa.getElementsByTagName("NAME").item(0).getTextContent();

        for(int i=0; i<elementa.getElementsByTagName("OUTCOME").getLength(); i++){
            this.outcome.add(elementa.getElementsByTagName("OUTCOME").item(i).getTextContent());
        }

        for(int i=0; i<elementb.getElementsByTagName("GIVEN").getLength(); i++){
            this.parents.add(elementb.getElementsByTagName("GIVEN").item(i).getTextContent());
        }

        this.given= 0;

        this.cpt= elementb.getElementsByTagName("TABLE").item(0).getTextContent();
        //this.cpt= new Factor(elementb.getElementsByTagName("TABLE").item(0).getTextContent(), this.parents, this.outcome);

        this.fromC=0;
        this.fromP=0;
    }

    public void addchaild(String chaild){
        this.children.add(chaild);
    }

    public void isgiven(){
        this.given=1;
    }
    public void bakegiven(){
        this.given=0;
    }
    public int whatgiven(){
        return this.given;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", outcome=" + outcome +
                ", parents=" + parents +
                ", children=" + children +
                ", cpt=" + cpt +
                //", cpt=" + cpt.toString() +
                ", given=" + given +
                '}';
    }
}
