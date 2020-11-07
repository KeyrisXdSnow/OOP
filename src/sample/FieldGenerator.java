package sample;

import computer.architecture.comType.PersonalComputer;
import serilization.json.JsonSerializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldGenerator<T extends PersonalComputer> extends JsonSerializer<T> {

    public String toString(Object object) throws IllegalAccessException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        new Object() {
            {
            }

            void getFields(Class aClass, Object object) throws IllegalAccessException {
                if (!aClass.getSimpleName().equals(Object.class.getSimpleName()))
                    getFields(aClass.getSuperclass(), object);
                else return;

                Field[] fields = aClass.getDeclaredFields();
                try {
                    for (Field field : fields) {

                        field.setAccessible(true);

                        if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName())) ^ (field.getType().getName().equals(Character.class.getName())) ^ (field.getType().getName().equals(Integer.class.getName())) ^ (field.getType().getName().equals(Double.class.getName())) ^ (field.getType().getName().equals(Float.class.getName())) ^ (field.getType().getName().equals(Long.class.getName())) ^ (field.getType().getName().equals(Short.class.getName())) ^ (field.getType().getName().equals(Boolean.class.getName())) ^ (field.getType().getName().equals(Byte.class.getName()))) {
                            stringBuilder.append("").append(field.getName()).append("").append(":");
                            if (field.getType().getTypeName().equals(String.class.getTypeName()))
                                stringBuilder.append("\"").append(field.get(object)).append("\"");
                            else stringBuilder.append(field.get(object));

                        } else {
                            if (field.get(object) != null ) {
                                stringBuilder.append("").append(field.getName()).append("").append(":");
                                if (field.getType().toString().contains("util") || field.getType().toString().contains("lang"))
                                stringBuilder.append(field.get(object));
                                else {
                                    stringBuilder.append("{");
                                    getFields(field.get(object).getClass(), field.get(object));
                                    stringBuilder.append("}");
                                }
                            }
                        }
                        if (field.get(object) != null ) stringBuilder.append(",");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }.getFields(object.getClass(),object);

        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"}");
        String result = stringBuilder.toString().replaceAll(",\\}","\\}");
        return result;
    }


    public Object initObject(Object device, String json) throws NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Pattern pattern = Pattern.compile("(\\w+?:([[.][^[,\\{\\}]]])++)|(\\w+?:\\{\\w+?=.+?})");
        Matcher matcher = pattern.matcher(json);
        LinkedHashMap<String, ArrayList<String>> tokens = new LinkedHashMap<>();
        while (matcher.find()) {

            String token = json.substring(matcher.start(), matcher.end());

            long startAmount = token.chars().filter(ch -> ch == '{').count() - token.chars().filter(ch -> ch == '}').count(),
                    beginAmount = token.chars().filter(ch -> ch == '[').count() - token.chars().filter(ch -> ch == ']').count();

            if (startAmount != 0 || beginAmount != 0) {

                for (int i = matcher.end(); i < json.length(); i++) {
                    token = json.substring(matcher.start(), i);
                    switch (json.charAt(i)) {
                        case '{': {
                            ++startAmount;
                            break;
                        }
                        case '}': {
                            --startAmount;
                            break;
                        }
                        case ']': {
                            --beginAmount;
                            break;
                        }
                        case '[': {
                            ++beginAmount;
                            break;
                        }
                    }
                    if (startAmount == 0 && beginAmount == 0) {
                        token = json.substring(matcher.start(), i + 1);
                        break;
                    }
                }
            }
            String[] split = token.split(":");
            if (!tokens.containsKey(split[0])) tokens.put(split[0], new ArrayList<String>());
            tokens.get(split[0]).add(split[1]);
        }

        new Object() {
            {
            }

            void getFields(Class aClass, Object object) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
                if (!aClass.getSimpleName().equals(Object.class.getSimpleName()))
                    getFields(aClass.getSuperclass(), object);
                else return;

                for (Field field : aClass.getDeclaredFields()) {

                    field.setAccessible(true);

                    if (!tokens.keySet().contains(field.getName())) continue;;

                    if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName())) ^ (field.getType().getName().equals(Character.class.getName())) ^ (field.getType().getName().equals(Integer.class.getName())) ^ (field.getType().getName().equals(Double.class.getName())) ^ (field.getType().getName().equals(Float.class.getName())) ^ (field.getType().getName().equals(Long.class.getName())) ^ (field.getType().getName().equals(Short.class.getName())) ^ (field.getType().getName().equals(Boolean.class.getName())) ^ (field.getType().getName().equals(Byte.class.getName()))) {

                        switch (field.getType().getSimpleName()) {
                            case "Integer":
                            case "int": {
                                field.setInt(object, Integer.parseInt(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "String": {
                                field.set(object, tokens.get(field.getName()).get(0).replaceAll("\"", ""));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "double":
                            case "Double": {
                                field.setDouble(object, Double.parseDouble(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "boolean":
                            case "Boolean": {
                                field.setBoolean(object, Boolean.parseBoolean(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "byte":
                            case "Byte": {
                                field.setByte(object, Byte.parseByte(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "float":
                            case "Float": {
                                field.setFloat(object, Float.parseFloat(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "short":
                            case "Short": {
                                field.setShort(object, Short.parseShort(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            case "long":
                            case "Long": {
                                field.setLong(object, Long.parseLong(tokens.get(field.getName()).get(0)));
                                tokens.get(field.getName()).remove(0);
                                break;
                            }
                            default: {
                                field.set(object, (Object) tokens.get(field.getName()).get(0));
                                tokens.get(field.getName()).remove(0);
                            }
                        }
                    } else {

                        if (field.getType().toString().contains("util") || field.getType().toString().contains("lang")) {

                            String[] values = tokens.get(field.getName()).get(0).replaceAll("^(\\[|\\{)", "").
                                    replaceAll("(\\]|\\})$", "").trim().split(",");

                            String[] type = field.getGenericType().getTypeName().replaceAll("^.+<", "").replaceAll(">.*$", "").trim().split(",");

                            if (field.getType().getTypeName().replaceFirst("class", "").trim().equals(ArrayList.class.getTypeName())) {

                                Class clazz = Class.forName(type[0].trim());
                                Method valueOf;
                                try {
                                    valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                } catch (NoSuchMethodException e) {
                                    valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                }
                                valueOf.setAccessible(true);

                                if (values.length != 0) {

                                    ArrayList arrayList = new ArrayList();

                                    for (int index = 0; index < values.length; index++) {
                                        arrayList.add(index, valueOf.invoke(clazz, values[index].trim()));
                                    }

                                    field.set(object, arrayList);
                                }
                                continue;
                            }
                            if (field.getType().getTypeName().replaceFirst("class", "").trim().equals(LinkedList.class.getTypeName())) {
                                Class clazz = Class.forName(type[0].trim());
                                Method valueOf;
                                try {
                                    valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                } catch (NoSuchMethodException e) {
                                    valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                }
                                valueOf.setAccessible(true);
                                if (values.length != 0) {
                                    LinkedList arrayList = new LinkedList();
                                    for (int index = 0; index < values.length; index++) {
                                        arrayList.add(index, valueOf.invoke(clazz, values[index].trim()));
                                    }
                                    field.set(object, arrayList);
                                }
                                continue;
                            }
                            if (field.getType().getName().replaceFirst("class", "").trim().equals(HashMap.class.getTypeName())) {

                                Method valueOfKey, valueOfValue;
                                Class clazzKey = Class.forName(type[0].trim()), clazzValue = Class.forName(type[1].trim());
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

                                if (values.length != 0) {
                                    HashMap list = new HashMap();
                                    for (String value : values) {
                                        String[] v = value.split("=");
                                        list.put(valueOfKey.invoke(clazzKey, v[0].trim()), valueOfValue.invoke(clazzValue, v[1].trim()));
                                    }
                                    field.set(object, list);
                                }
                                continue;
                            }
                            if (field.getType().getName().replaceFirst("class", "").trim().equals(LinkedHashMap.class.getTypeName())) {

                                Method valueOfKey, valueOfValue;
                                Class clazzKey = Class.forName(type[0].trim()), clazzValue = Class.forName(type[1].trim());
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

                                if (values.length != 0) {
                                    LinkedHashMap list = new LinkedHashMap();
                                    for (String value : values) {
                                        String[] v = value.split("=");
                                        list.put(valueOfKey.invoke(clazzKey, v[0].trim()), valueOfValue.invoke(clazzValue, v[1].trim()));
                                    }
                                    field.set(object, list);
                                }
                            }

                        } else {
                            Class helpClass = Class.forName(field.getGenericType().toString().replaceFirst("class ",""));
                            Constructor<T> constructor = helpClass.getConstructor();
                            constructor.setAccessible(true);
                            field.set(object,constructor.newInstance());
                            getFields(field.get(object).getClass(), field.get(object));
                        }

                    }
                }
            }

        }.getFields(device.getClass(), device);

        return device;
    }
}
