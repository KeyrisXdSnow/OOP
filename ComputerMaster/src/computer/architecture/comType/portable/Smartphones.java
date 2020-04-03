package computer.architecture.comType.portable;

import computer.Computer;
import computer.architecture.comType.PersonalComputer;
import computer.specifications.Specifications;

import java.util.ArrayList;

public class Smartphones extends PortableComputer implements Computer {

    public int inte = 60 ;

    public Smartphones(String name) {
        super(name);
    }

    public Smartphones (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super (name, specifications, cost, installedPrograms);
    }

    @Override
    public void turnON() {

    }

    @Override
    public void turnOFF() {

    }
}
