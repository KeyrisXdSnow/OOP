package computer.architecture.comType;

import computer.specifications.Specifications;
import program.Software;
import java.util.ArrayList;


public abstract class PersonalComputer  {

    protected String name ;
    protected Specifications specifications ;
    protected long cost ;

    protected  ArrayList<String> installedPrograms;
    protected boolean isWork = true ;

    public PersonalComputer (String name, Specifications specifications) {
        this (name, specifications, 300);
    }

    public PersonalComputer (String name, Specifications specifications, long cost) {
       this (name,specifications,cost,null);
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

//    @Override
//    public void installProgram (Program program) {
//        if (!programs.contains(program)) programs.add(program) ;
//        else System.out.println("Already install");
//    }
//
//    @Override
//    public void runProgram (Program program) {
//        if (programs.contains(program) && program.getProgramStatus() != ProgramStatus.run) program.run();
//        else if (program.getProgramStatus() == ProgramStatus.run) System.out.println("Already run");
//        else System.out.println("Not install");
//    }
//
//    @Override
//    public void stopProgram (Program program) {
//        if (programs.contains(program) && program.getProgramStatus() == ProgramStatus.run) program.stop();
//        else System.out.println("Not run");
//    }
//
//    @Override
//    public void deleteProgram (Program program) {
//        if (programs.contains(program) && program.getProgramStatus() != ProgramStatus.run ) {
//            programs.remove(program);
//        }
//        else System.out.println("Fist stop Program");
//    }

    public String getName () {
        return name;
    }

    // abstract void turnOn();
    //abstract void turnOFF ();

}
