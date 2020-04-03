package computer.architecture.comType.portable;

import computer.Computer;
import computer.specifications.SomeInfo;
import computer.specifications.Specifications;
import program.Program;

import java.util.ArrayList;
import java.util.HashMap;

public class Laptop extends PortableComputer  {

    public int sddAmount  = 3  ;
    boolean haveSSD = true ;
    public String  supportedLang = "Ru/English/..  " ;


    public ArrayList<Integer> arrayList = new ArrayList<>() ;

    HashMap<String,Integer> ports = new HashMap<>();

    public Laptop(String name) {
        super(name);

        arrayList.add(1);
        arrayList.add(9);
        arrayList.add(2);
        arrayList.add(14);
        arrayList.add(12);
        arrayList.add(5);

        ports.put("USB",2);
        ports.put("Type-C",1);
        ports.put("AVG",1);
        ports.put("HDMI",1);
    }

    public Laptop (String name, Specifications specifications, long cost, ArrayList<String> installedPrograms) {
        super (name, specifications, cost, installedPrograms);

        arrayList.add(1);
        arrayList.add(9);
        arrayList.add(2);
        arrayList.add(14);
        arrayList.add(12);
        arrayList.add(5);

        ports.put("USB",2);
        ports.put("Type-C",1);
        ports.put("AVG",1);
        ports.put("HDMI",1);
    }
    public Laptop () {


        super("Default Name");


        ports.put("USB",2);
        ports.put("Type-C",1);
        ports.put("AVG",1);
        ports.put("HDMI",1);

        arrayList.add(1);
        arrayList.add(9);
        arrayList.add(2);
        arrayList.add(14);
        arrayList.add(12);
        arrayList.add(5);

    }

//    @Override
//    public void installProgram(Program program) {
//        super.installProgram(program);
//    }
//
//    public  void showIfo () {}
//
//    @Override
//    public void turnON() {
//
//    }
//
//    @Override
//    public void turnOFF() {
//
//    }
}
