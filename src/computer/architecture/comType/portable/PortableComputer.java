package computer.architecture.comType.portable;

import com.fasterxml.jackson.annotation.JsonValue;
import computer.architecture.comType.PersonalComputer;
import computer.specifications.Specifications;
import sample.FieldGenerator;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class PortableComputer extends PersonalComputer implements Serializable {


    public PortableComputer(String json) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        new FieldGenerator().initObject(this,json);
    }
    public PortableComputer (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super(name,specifications,cost,installedPrograms);
    }
    public PortableComputer() {super();}

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
