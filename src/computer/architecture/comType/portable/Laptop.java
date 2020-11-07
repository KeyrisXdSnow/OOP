package computer.architecture.comType.portable;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import computer.specifications.Specifications;
import sample.FieldGenerator;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Laptop extends PortableComputer implements Serializable {

    public int sddAmount  = 3  ;
    boolean haveSSD = true ;
    public String  supportedLang = "Ru/English/..  " ;
    @JsonDeserialize(as = ArrayList.class)
    public ArrayList<Integer> arrayList = new ArrayList<>() ;
    @JsonDeserialize(as = HashMap.class)
    HashMap<String,Integer> ports = new HashMap<>();

    public Laptop(String json) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        new FieldGenerator().initObject(this,json);
    }

    public Laptop (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super (name, specifications, cost, installedPrograms);

        arrayList.add(1);
        arrayList.add(9);
        arrayList.add(2);
        arrayList.add(14);
        arrayList.add(12);
        arrayList.add(5);

        ports.put("USB",2);
        ports.put("Type-C",1);
        ports.put("AVG",1);
        ports.put("HDMI",1);

    }
    public Laptop () throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        super();

        ports.put("USB",2);
        ports.put("Type-C",1);
        ports.put("AVG",1);
        ports.put("HDMI",1);

        arrayList.add(1);
        arrayList.add(9);
        arrayList.add(2);
        arrayList.add(14);
        arrayList.add(12);
        arrayList.add(5);
    }

    @Override
    @JsonValue
    public String toString() {
        try {
            return new FieldGenerator().toString(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

}
