package computer.architecture.comType.portable;

import computer.Computer;
import computer.architecture.comType.PersonalComputer;
import computer.specifications.Specifications;

import java.util.ArrayList;

public class PortableComputer extends PersonalComputer {


    public PortableComputer(String name) {

        super(name);
    }

    public PortableComputer (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super(name,specifications,cost,installedPrograms);
    }

    void chargeOn () {}


}
