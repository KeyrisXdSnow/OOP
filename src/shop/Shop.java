package shop;

import computer.architecture.comType.PersonalComputer;

import java.util.*;

public class Shop< T extends PersonalComputer> {

    private String name ;
    private long shopCash ;

    private LinkedHashMap< String, LinkedHashMap<T,Integer>> range ;

    private boolean open = false ;

    public Shop(String Name, long shopCash) {
        this.name = Name ;
        this.shopCash = shopCash;
        range = new LinkedHashMap<>();
    }

    public Shop(String Name) {
        this  (Name,10000);
    }
    public Shop(long shopCash) {
        this ("shop.Shop", shopCash);
    }

    public void buyComputer (T computer) {

        if (range.containsKey(computer.getClass().getName())) {
            for (Map.Entry<T,Integer> gadget : range.get(computer.getClass().getName()).entrySet()) {
               if ( gadget.getKey().getName().equals(computer.getName()) ) {
                   range.get(computer.getClass().getName()).replace(gadget.getKey(),gadget.getValue()+1);
                   return;
               }
            }
            range.get(computer.getClass().getName()).put(computer, 1);

        } else {
            LinkedHashMap<T, Integer> device = new LinkedHashMap();
            device.put(computer, 1);
            range.put(computer.getClass().getName(),device);
        }
    }

    public void buyComputer (T computer, String type) {

        if (range.containsKey(type)) {
            for (Map.Entry<T,Integer> gadget : range.get(type).entrySet()) {
                if ( gadget.getKey().getName().equals(computer.getName()) ) {
                    range.get(computer.getClass().getName()).replace(gadget.getKey(),gadget.getValue()+1);
                    return;
                }
            }
            range.get(type).put(computer, 1);

        } else {
            LinkedHashMap<T, Integer> device = new LinkedHashMap();
            device.put(computer, 1);
            range.put(computer.getClass().getName(),device);
        }
    }

    public void buyComputer (String type) {
        range.put(type, new LinkedHashMap<T,Integer>());
    }



    public void sellComputer (String type, String computerName) { // хз костыль это или нет нужно еще подумать

        if (range.containsKey(type) ) {
            HashMap<T,Integer> gadgets = range.get(type) ;
            boolean find = false;

            for ( int i = 0 ; i < gadgets.size(); i++) {
                PersonalComputer gadget = (PersonalComputer) gadgets.keySet().toArray()[i];

                if (gadget.getName().equals(computerName) ) {

                    Object key = (PersonalComputer) gadgets.keySet().toArray()[i];
                    int amount = gadgets.get(gadget);

                    if (amount > 1) range.get(type).replace((T) key, amount, amount-1);
                    else range.get(type).remove((T) key);
                    find = true ;
                    break;
                }

            }
            if (!find) System.out.println("Can't find this model in the system");

        } else System.out.println("Can't find this type in the system");
    }

    private T getGadget (String type, String gadgetName ) {

        for (T computer : range.get(type).keySet()) { // мы перебираем все гаджеты
            if (computer.getName().equals(gadgetName)) {
                return computer;
            }
        }
        return null ;
    }

    public HashMap<T, Integer> getTypedEquipment(String type) {

        for ( Map.Entry<String, LinkedHashMap<T, Integer>> list : range.entrySet()) {
            if (type.equals(list.getKey())) return list.getValue();
        }
        return null ;

    }

    public void sellComputer(String type) {

        range.remove(type) ;
    }

    public Integer getGadgetCount (String type, String computerName) {
        if (range.containsKey(type) ) {

            HashMap<T,Integer> gadgets = range.get(type) ;

            for ( int i = 0 ; i < gadgets.size(); i++) {
                PersonalComputer gadget = (PersonalComputer) gadgets.keySet().toArray()[i];
                if (gadget.getName().equals(computerName) ) return gadgets.get(gadget);
            }
            System.out.println("Can't find this model in the system");

        } else System.out.println("Can't find this type in the system");
        return  0 ;
    }

    public Integer setGadgetCount (String type, String computerName, int gadgetCount) {
        if (range.containsKey(type) ) {

            LinkedHashMap<T,Integer> gadgets = range.get(type) ;

            for ( int i = 0 ; i < gadgets.size(); i++) {
                PersonalComputer gadget = (PersonalComputer) gadgets.keySet().toArray()[i];
                if (gadget.getName().equals(computerName) ) {
                    gadgets.replace((T) gadget,gadgetCount);
                    return 0;
                }
            }
            System.out.println("Can't find this model in the system");

        } else System.out.println("Can't find this type in the system");
        return  0 ;
    }

    public Set<String> getTechniqueTypes () { return range.keySet(); }

    public LinkedHashMap< String, LinkedHashMap<T,Integer>> getRange() {
        return range;
    }

    public void setRange(LinkedHashMap< String, LinkedHashMap<T,Integer>> range) {
        this.range = range;
    }

}
