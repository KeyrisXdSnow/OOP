package computer.architecture.comType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import computer.architecture.comType.portable.Laptop;
import computer.architecture.comType.portable.Smartphones;
import computer.architecture.comType.portable.Tablet;
import computer.architecture.comType.stationary.StationaryComputer;
import computer.specifications.Specifications;
import sample.FieldGenerator;
import java.io.Serializable;
import java.util.ArrayList;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StationaryComputer.class, name="stationaryComputer"),
        @JsonSubTypes.Type(value= Laptop.class, name="laptop"),
        @JsonSubTypes.Type(value= Smartphones.class, name="smartphones"),
        @JsonSubTypes.Type(value= Tablet.class, name="tablet"),
})
public abstract class PersonalComputer implements Serializable {

    protected String name ;
    protected Specifications specifications ;
    protected long cost ;
    @JsonDeserialize(as = ArrayList.class)
    protected  ArrayList<String> installedPrograms;
    protected boolean isWork = true ;

    @Override
    @JsonValue
    public String toString() {
        try {
            return new FieldGenerator().toString(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PersonalComputer (String name, Specifications specifications) {
        this (name, specifications, 300);
    }

    public PersonalComputer (String name, Specifications specifications, long cost) {
       this (name,specifications,cost,null);
    }
    public PersonalComputer () {
        name = "New Gadget";
    }

    public PersonalComputer (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        this.name = name ;
        this.cost = cost;
        this.specifications = specifications;
        this.installedPrograms = installedPrograms;
    }

    public PersonalComputer(String name) {
        this(name, new Specifications("-","-","-","-","-","-") );
    }


    public String getName () {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Specifications getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Specifications specifications) {
        this.specifications = specifications;
    }

}
