package serilization.text;

import computer.architecture.comType.PersonalComputer;
import computer.architecture.comType.portable.Smartphones;
import computer.specifications.Specifications;
import serilization.Serializer;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public class TextSerializer <T extends PersonalComputer> implements Serializer<T> {

    private String newLineDelimiter = ";\n" ;

    @Override
    public void serialize(LinkedHashMap<String, LinkedHashMap<T, Integer>> list, File chosenFile) throws Exception {

        try (FileWriter writer = new FileWriter(chosenFile)) {

            for (Map.Entry<String, LinkedHashMap<T, Integer>> device : list.entrySet()) {

                writer.write("key->"+device.getKey()+newLineDelimiter+"value->{\n");
                for (Map.Entry<T, Integer> d : device.getValue().entrySet()) {
                    writer.write("key{\n"+object(d.getKey())+"}\n"+"value->"+d.getValue()+newLineDelimiter+"\n");
                }
                writer.write("}"+"\n");
            }
            writer.flush();

        }
    }

    private String object (T device) throws IllegalAccessException {

        StringBuilder strDevice = new StringBuilder() ;
        String delimiter = "->" ;

        new Object(){ {}

                void getSuperClass (Object object, Class aClass) throws IllegalAccessException {

                    if (aClass.getSimpleName().equals(Object.class.getSimpleName())) return;
                    else getSuperClass(object,aClass.getSuperclass());

                    try {
                        for (Field field : aClass.getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName())) ^ (field.getType().getName().equals(Character.class.getName())) ^ (field.getType().getName().equals(Integer.class.getName())) ^ (field.getType().getName().equals(Double.class.getName())) ^ (field.getType().getName().equals(Float.class.getName())) ^ (field.getType().getName().equals(Long.class.getName())) ^ (field.getType().getName().equals(Short.class.getName())) ^ (field.getType().getName().equals(Boolean.class.getName())) ^ (field.getType().getName().equals(Byte.class.getName()))) {
                                strDevice.append(field.getName()).append(delimiter).append(field.get(object).toString()).append(newLineDelimiter);
                            } else {
                                if (field.getType().isArray()) strDevice.append(field.getName()).append(delimiter).append(field.get(object).toString()).append(newLineDelimiter);
                                else if (field.get(object) != null) {
                                    if (field.getType().getName().contains("util") ^ field.getName().contains("lang")) {
                                        strDevice.append(field.getName()).append("(").append(field.getGenericType().toString().replaceAll(".+?<", "").replaceAll(">", "")).append(")").append("->")
                                                .append(field.get(object).toString()).append(";\n");
                                    } else {
                                        strDevice.append(field.getName()).append(" { \n");
                                        getSuperClass(field.get(object), field.get(object).getClass());
                                        strDevice.append("}\n");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }.getSuperClass(device,device.getClass());

        return strDevice.toString();
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<T, Integer>> deserialize(File chosenFile) throws Exception {

        StringBuilder file;
        try (BufferedReader reader = new BufferedReader(new FileReader(chosenFile))) {

            file = new StringBuilder();
            String r;
            while ((r = reader.readLine()) != null) {
                file.append(r);
            }
        }

        String[] linkedHashMap = file.toString().split("key->");

        LinkedHashMap<String, LinkedHashMap<T, Integer>> result = new LinkedHashMap<>();

        for (String entrySet : linkedHashMap) {

            if (entrySet.equals("")) continue;
            int c = entrySet.indexOf(";");
            String key = entrySet.substring(0, c);
            LinkedHashMap<T, Integer> devices = new LinkedHashMap<>();
            ArrayList<String> strDevices = calculateStr(entrySet, "key\\{");
            for (String dev : strDevices) {
                final String[] str = {dev};
                String value = entrySet.substring(entrySet.indexOf(str[0])).trim();

                Pattern p = Pattern.compile("value\\s*->\\s*([0-9]+?);?\n*$" +
                        "*");
                Matcher matcher = p.matcher(value);
                if (matcher.find())
                    value = value.substring(matcher.start(), matcher.end()).replaceFirst("^value->", "").replaceAll(";\n*$", "");


                Class aClass = Class.forName(key);
                Constructor<T> constructor = aClass.getConstructor();
                constructor.setAccessible(true);
                Object object = constructor.newInstance();;

                new Object() {
                    {
                    }

                    void getSuperClass(Object object, Class aClass) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {

                        if (aClass.getSimpleName().equals(Object.class.getSimpleName())) return;
                        else getSuperClass(object, aClass.getSuperclass());

                        for (Field field : aClass.getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName())) ^ (field.getType().getName().equals(Character.class.getName())) ^ (field.getType().getName().equals(Integer.class.getName())) ^ (field.getType().getName().equals(Double.class.getName())) ^ (field.getType().getName().equals(Float.class.getName())) ^ (field.getType().getName().equals(Long.class.getName())) ^ (field.getType().getName().equals(Short.class.getName())) ^ (field.getType().getName().equals(Boolean.class.getName())) ^ (field.getType().getName().equals(Byte.class.getName()))) {

                                Pattern p = Pattern.compile(field.getName() + "->.+?;");
                                Matcher m = p.matcher(str[0]);
                                String value = "";
                                while (m.find()) {
                                    if (str[0].substring(m.start()).chars().filter(ch -> ch == '{').count()
                                            == str[0].substring(m.start()).chars().filter(ch -> ch == '}').count()) {
                                        value = str[0].substring(m.start(), m.end());
                                        value = value.replaceFirst("^" + field.getName() + "->", "").trim();
                                        value = value.replaceAll(";?\n*?$", "");
                                        value = value.replaceFirst(";","");
                                    }
                                }
                                if (value.equals("")) continue;
                                switch (field.getType().getSimpleName()) {
                                    case "Integer":
                                    case "int": {
                                        field.setInt(object, Integer.parseInt(value));
                                        break;
                                    }
                                    case "String": {
                                        field.set(object, value);
                                        break;
                                    }
                                    case "double":
                                    case "Double": {
                                        field.setDouble(object, Double.parseDouble(value));
                                        break;
                                    }
                                    case "boolean":
                                    case "Boolean": {
                                        field.setBoolean(object, Boolean.parseBoolean(value));
                                        break;
                                    }
                                    case "byte":
                                    case "Byte": {
                                        field.setByte(object, Byte.parseByte(value));
                                        break;
                                    }
                                    case "float":
                                    case "Float": {
                                        field.setFloat(object, Float.parseFloat(value));
                                        break;
                                    }
                                    case "short":
                                    case "Short": {
                                        field.setShort(object, Short.parseShort(value));
                                        break;
                                    }
                                    case "long":
                                    case "Long": {
                                        field.setLong(object, Long.parseLong(value));
                                        break;
                                    }
                                    default: {
                                        field.set(object, value);
                                    }
                                }
                            } else {
                                if (field.getType().isArray()) {
                                } else {
                                    if (field.getType().getName().contains("util") ^ field.getName().contains("lang")) {

                                        Pattern p = Pattern.compile(field.getName() + "\\(.+?\\)->.+?;\n?");
                                        Matcher m = p.matcher(str[0]);
                                        String value = "";
                                        while (m.find()) {
                                            if (str[0].substring(m.start()).chars().filter(ch -> ch == '{').count()
                                                    == str[0].substring(m.start()).chars().filter(ch -> ch == '}').count()) {
                                                value = str[0].substring(m.start(), m.end());
                                                value = value.replaceFirst("^" + field.getName() + "\\(.+?\\)->", "").trim();
                                                value = value.replaceAll(";\n*?$", "");
                                                value = value.replaceAll("((])|(}))$", "");
                                                value = value.replaceAll("^((\\{)|(\\[))", "");
                                                break;
                                            }
                                        }
                                        if (value.equals("")) continue;
                                        String valueType = "";
                                        try {
                                            valueType = str[0].substring(m.start(), m.end()).
                                                    replaceAll("\\s*\\)\\s*->\\s*.+$", "").replaceFirst("^.+?\\(", "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String[] valuesType = valueType.trim().replaceAll(";?\\n?", "").replaceAll("^(\\[|\\{)", "").
                                                replaceAll("(\\]|\\})$", "").trim().split(",");
                                        for (int i = 0; i < valuesType.length; i++)
                                            valuesType[i] = valuesType[i].trim();

                                        if (field.getType().getTypeName().replaceFirst("class", "").
                                                trim().equals(ArrayList.class.getTypeName())) {

                                            ArrayList arrayList = new ArrayList();
                                            if (field.get(object) != null) continue;

                                            Method valueOf;
                                            Class clazz = Class.forName(valuesType[0]);
                                            try {
                                                valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                            }

                                            valueOf.setAccessible(true);
                                            String[] values = value.split(",");

                                            for (int index = 0; index < values.length; index++) {
                                                arrayList.add(index, valueOf.invoke(clazz, values[index]));
                                            }

                                            field.set(object, arrayList);
                                            continue;
                                        }

                                        if (field.getType().getName().replaceFirst("class", "").
                                                trim().equals(LinkedList.class.getTypeName())) {

                                            LinkedList arrayList = (LinkedList) field.get(object);
                                            if (field.get(object) != null) continue;

                                            Method valueOf;
                                            Class clazz = Class.forName(valuesType[0]);
                                            try {
                                                valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                            }

                                            valueOf.setAccessible(true);
                                            String[] values = value.split(",");

                                            for (int index = 0; index < values.length; index++) {
                                                arrayList.add(index, valueOf.invoke(clazz, values[index]));
                                            }

                                            field.set(object, arrayList);
                                            continue;
                                        }

                                        if (field.getType().getName().replaceFirst("class", "").
                                                trim().equals(HashMap.class.getTypeName())) {

                                            HashMap list = new HashMap();
                                            if (field.get(object) != null) continue;

                                            Method valueOfKey, valueOfValue;
                                            Class clazzKey = Class.forName(valuesType[0]), clazzValue = Class.forName(valuesType[1]);
                                            try {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", Object.class);
                                            }
                                            try {
                                                valueOfValue = clazzValue.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfValue = clazzValue.getDeclaredMethod("valueOf", Object.class);
                                            }
                                            valueOfValue.setAccessible(true);

                                            String[] values = value.split(",");

                                            for (int index = 0; index < values.length; index++) {
                                                String[] v = values[index].split("=");
                                                list.put(valueOfKey.invoke(clazzKey, v[0].trim()), valueOfValue.invoke(clazzValue, v[1].trim()));
                                            }
                                            field.set(object, list);
                                            continue;
                                        }

                                        if (field.getType().getName().replaceFirst("class", "").
                                                trim().equals(LinkedHashMap.class.getTypeName())) {

                                            LinkedHashMap list = new LinkedHashMap();
                                            if (field.get(object) != null) continue;

                                            Method valueOfKey, valueOfValue;
                                            Class clazzKey = Class.forName(valuesType[0]), clazzValue = Class.forName(valuesType[1]);
                                            try {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", Object.class);
                                            }
                                            try {
                                                valueOfValue = clazzValue.getDeclaredMethod("valueOf", String.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfValue = clazzValue.getDeclaredMethod("valueOf", Object.class);
                                            }
                                            valueOfValue.setAccessible(true);

                                            String[] values = value.split(",");

                                            for (String s : values) {
                                                String[] v = s.split("=");
                                                list.put(valueOfKey.invoke(clazzKey, v[0].trim()), valueOfValue.invoke(clazzValue, v[1].trim()));
                                            }
                                            field.set(object, list);
                                        }

                                    } else {
                                        String oldStr = str[0];
                                        str[0] = calculateStr(oldStr, field.getName() + "\\s*\\{").get(0);
                                        if (str[0].equals(""))  {
                                            str[0] = oldStr;
                                            continue;
                                        }
                                        Class helpClass = Class.forName(field.getGenericType().toString().replaceFirst("class ", ""));
                                        Constructor<T> constructor = helpClass.getConstructor();
                                        constructor.setAccessible(true);
                                        field.set(object, constructor.newInstance());
                                        getSuperClass(field.get(object), field.get(object).getClass());
                                        str[0] = oldStr;

                                    }
                                }
                            }
                        }
                    }
                }.getSuperClass(object, object.getClass());

                devices.put((T) object, Integer.parseInt(value));
            }
            result.put(key, devices);
        }
        return result;

    }

    private ArrayList<String> calculateStr (String str, String regEx) {
        ArrayList<String> result = new ArrayList<>();

            ArrayList<Integer> parStart = new ArrayList<>();
            ArrayList<Integer> parEnd = new ArrayList<>();

            Pattern pattern = Pattern.compile("\\{");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) parStart.add(matcher.start());

            pattern = Pattern.compile("}");
            matcher = pattern.matcher(str);
            while (matcher.find()) parEnd.add(matcher.start());

            pattern = Pattern.compile(regEx);
            matcher = pattern.matcher(str);
            String strResult;

            while (matcher.find()) {
                for (int index = 0; index < parStart.size(); index++) {
                    if (parEnd.get(index) < matcher.end()) continue;
                    strResult = str.substring(matcher.end(), parEnd.get(index));
                    if (strResult.chars().filter(ch -> ch == '{').count() == strResult.chars().filter(ch -> ch == '}').count()) {
                        result.add(strResult);
                        break;
                    }
                }
            }
            if (result.isEmpty()) result.add("");
            return result;

    }
}
