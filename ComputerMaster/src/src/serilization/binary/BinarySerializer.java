package serilization.binary;

import computer.architecture.comType.PersonalComputer;
import serilization.Serializer;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.zip.ZipOutputStream;

public class BinarySerializer <T extends PersonalComputer>  implements Serializable, Serializer<T> {

    @Override
    public void serialize(LinkedHashMap<String, LinkedHashMap<T, Integer>> list, File chosenFile) throws Exception {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(chosenFile))) {
            writer.writeObject(list);
        }
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<T, Integer>> deserialize(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
            Object data = reader.readObject();
            return new LinkedHashMap<>((LinkedHashMap<String, LinkedHashMap<T, Integer>>) data);
        }
    }

}
