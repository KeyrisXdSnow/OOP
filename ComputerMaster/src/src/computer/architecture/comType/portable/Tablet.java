package computer.architecture.comType.portable;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import computer.specifications.Specifications;
import sample.FieldGenerator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Tablet extends PortableComputer implements Serializable {

    @JsonDeserialize(as = LinkedList.class)
    public LinkedList linkedList = new LinkedList() ;

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

    public Tablet() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        super();
    }
    public Tablet (String json) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        new FieldGenerator().initObject(this, json);
    }
    public Tablet (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super (name, specifications, cost, installedPrograms);
    }

}
