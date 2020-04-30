package computer.specifications;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sample.FieldGenerator;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

enum specificationField {
    CPU, RAM, OS, weight, manufacturer, colour
    }

public class Specifications implements Serializable {


    public int max = 123;
    public int cost = 45 ; 
    SomeInfo someInfo = new SomeInfo();
    @JsonDeserialize(as = LinkedHashMap.class)
    protected LinkedHashMap<String, String> specifications;

    @Override
    @JsonValue
    public String toString () {
        try {
            return new FieldGenerator().toString(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }
    public Specifications(String cpu, String ram, String os,
                          String weight, String manufacturer, String colour) {
        specifications = new LinkedHashMap<>();
        specifications.put(specificationField.CPU.toString(), cpu);
        specifications.put(specificationField.RAM.toString(), ram);
        specifications.put(specificationField.OS.toString(), os);
        specifications.put(specificationField.weight.toString(), weight);
        specifications.put(specificationField.manufacturer.toString(), manufacturer);
        specifications.put(specificationField.colour.toString(), colour);
    }

    public Specifications(){}


    public void showInfo () {
        for (Map.Entry<String,String> sp : specifications.entrySet()) {
            System.out.println(sp.toString());
        }

    }

    public LinkedHashMap<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(LinkedHashMap<String, String> specifications) {
        this.specifications = specifications;
    }
}
