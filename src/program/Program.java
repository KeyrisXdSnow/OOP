package program;

public class Program  {
    protected String name = null ;
    protected double neededResourses = 0.0;
    private ProgramStatus programStatus = ProgramStatus.valueOf("none");

    public Program(String name, double neededResourses) {
         this.neededResourses = neededResourses ;
        this.name = name;
    }

    public Program(String name) {
       this.name  = name;
    }
    Program (double neededResourses) {
        this (null, neededResourses);
    }


    public void delete () {
      programStatus = ProgramStatus.none ;
    }
    public void run () {
         programStatus = ProgramStatus.run ;
    }
    public void stop () {
        programStatus = ProgramStatus.stop ;
    }
    public String getName () {
        return name ;
    }
    public double getNeededResourses () {
        return neededResourses ;
    }

    public ProgramStatus getProgramStatus() {
        return programStatus;
    }
}
