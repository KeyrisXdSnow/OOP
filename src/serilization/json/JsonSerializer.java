package serilization.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import computer.architecture.comType.PersonalComputer;
import computer.architecture.comType.portable.Laptop;
import computer.architecture.comType.portable.Smartphones;
import computer.architecture.comType.portable.Tablet;
import computer.architecture.comType.stationary.StationaryComputer;
import serilization.Serializer;
import java.io.*;
import java.util.*;

public class JsonSerializer <T extends PersonalComputer> implements Serializer<T> {

    private ObjectMapper mapper ;

    public JsonSerializer () { mapper = new ObjectMapper(); }

    @Override
    public void serialize(LinkedHashMap<String, LinkedHashMap<T, Integer>> data,File chosenFile) throws Exception  {
        //chosen file просто сделаьт зипом
        mapper.writeValue(chosenFile, data);
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<T, Integer>> deserialize(File chosenFile) throws Exception {

        LinkedHashMap<String, LinkedHashMap<String, Integer>> mapLinkedHashMap =
                mapper.readValue(chosenFile, new TypeReference<LinkedHashMap<String, LinkedHashMap<String, Integer>>>() {});
        LinkedHashMap<String, LinkedHashMap<T, Integer>> data = new LinkedHashMap<>();

        mapLinkedHashMap.forEach( (mapKey,mainValue) -> {
            LinkedHashMap<T,Integer> hashMap = new LinkedHashMap<>();
            mainValue.forEach( (key,value) -> {
                try {
                    key = key.replaceAll("\"","\\\\\"");
                    String json = "[" ;
                    if (mapKey.equals(Laptop.class.getName())) json += "\"laptop\"" ;
                    else if (mapKey.equals(Smartphones.class.getName())) json += "\"smartphones\"" ;
                    else if (mapKey.equals(Tablet.class.getName())) json += "\"tablet\"";
                    else if (mapKey.equals(StationaryComputer.class.getName())) json += "\"stationaryComputer\"";
                    json += ",\""+key+"\"]";
                    T bla = mapper.readValue(json, new TypeReference<T>() {}) ;
                    hashMap.put(bla, value);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            });
            data.put(mapKey,hashMap);
        } );
        return data ;
    }
}
