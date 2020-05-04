package computer.architecture.comType.portable;


import com.fasterxml.jackson.annotation.JsonValue;
import computer.specifications.Specifications;
import sample.FieldGenerator;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Smartphones extends PortableComputer implements Serializable {

    public int inte = 60;

    public Smartphones(String json) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        new FieldGenerator().initObject(this, json);
    }

    public Smartphones(String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super(name, specifications, cost, installedPrograms);
    }

    public Smartphones() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        super();
    }


    @Override
    @JsonValue
    public String toString() {
        try {
            return new FieldGenerator().toString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

