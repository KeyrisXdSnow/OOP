package computer.architecture.comType.portable;

import computer.Computer;
import computer.architecture.comType.PersonalComputer;
import computer.specifications.Specifications;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tablet extends PortableComputer implements Computer {

    public LinkedList linkedList = new LinkedList() ;
    public Tablet(String name) {
        super(name);
    }
    public Tablet (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super (name, specifications, cost, installedPrograms);
    }

    @Override
    public void turnON() {

    }

    @Override
    public void turnOFF() {

    }
}
