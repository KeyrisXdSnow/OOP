package computer.specifications;

import java.util.LinkedHashMap;
import java.util.Map;

enum specificationField {
    CPU, RAM, OS, weight, manufacturer, colour
    }

public class Specifications {

    public int max = 123;
    public int cost = 45 ; 
    SomeInfo someInfo = new SomeInfo();
    protected LinkedHashMap<String, String> specifications;

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
    
    public String getCPU () {
        return specifications.get(specificationField.CPU.toString());
    }
    
    public String getRAM () {
        return specifications.get(specificationField.RAM.toString());
    }
    
    public String getOS () {
        return specifications.get(specificationField.OS.toString());
    }

    public String getWeight () {
        return specifications.get(specificationField.weight.toString());
    }

    public String getManufacturer () {
        return specifications.get(specificationField.manufacturer.toString());
    }

    public String getColour () {
        return specifications.get(specificationField.colour.toString());
    }

    public void showInfo () {
        for (Map.Entry<String,String> sp : specifications.entrySet()) {
            System.out.println(sp.toString());
        }

    }
}
