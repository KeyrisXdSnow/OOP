package serilization;

import computer.architecture.comType.PersonalComputer;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.zip.GZIPOutputStream;

public interface Serializer < T extends PersonalComputer> {
    public void serialize (LinkedHashMap< String, LinkedHashMap<T,Integer>> list,File chosenFile) throws Exception;
    public LinkedHashMap<String, LinkedHashMap<T,Integer>> deserialize (File chosenFile) throws Exception;


}
