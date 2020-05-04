package computer.specifications;

import com.fasterxml.jackson.annotation.JsonValue;
import computer.architecture.comType.portable.Laptop;
import sample.FieldGenerator;

import java.io.Serializable;
import java.lang.reflect.Field;

public class SomeInfo implements Serializable {

    public int buildYear = 2018  ;
    public String hello = "Some information about something";

    @Override
    @JsonValue
    public String toString(){
        try {
            return new FieldGenerator().toString(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(int buildYear) {
        this.buildYear = buildYear;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
