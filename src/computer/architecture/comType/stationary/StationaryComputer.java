package computer.architecture.comType.stationary;

import com.fasterxml.jackson.annotation.JsonValue;
import computer.architecture.comType.PersonalComputer;
import sample.FieldGenerator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class StationaryComputer extends PersonalComputer implements Serializable {

    public StationaryComputer(String json) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        new FieldGenerator().initObject(this, json);
    }

    public StationaryComputer() {
        super("New Gadget");
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
