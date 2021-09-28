import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Serializer<T extends Savable>{
    private final Class<?> cl;

    public Serializer (Class<?> cl) {
        this.cl = cl;
    }

    public void serialize(T t){
        String path = t.getPath();
        File file = new File(path);
        Field[] fields = cl.getDeclaredFields();
        try(PrintWriter pw = new PrintWriter(file)) {
            for(Field field : fields) {
                if(field.isAnnotationPresent(Save.class)) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                    }
                    pw.println(field.getName() + ": " + field.get(t));
                }
            }
        } catch (IOException e) {
            System.out.println("Input-output exception!");
        } catch (IllegalAccessException e) {
            System.out.println("Problems with access!");
        }
    }

    public T deserialize(String path) {
        T res = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            res = (T)cl.newInstance();
            String temp = "";

            while ((temp = br.readLine()) != null && !temp.equals("\n")) {
                fieldSetting(temp, res);
            }
        } catch (IOException e) {
            System.out.println("Input-output exception");
        } catch (IllegalAccessException e) {
            System.out.println("Illegal access");
        } catch (InstantiationException e) {
            System.out.println("No new instance");
        }
        return res;
    }

    private void fieldSetting(String temp, T res) throws IllegalAccessException {
        String[] splitting = temp.split(": ");
        String fieldName = splitting[0];
        String fieldValue = splitting[1];
        try {
            Field field = cl.getDeclaredField(fieldName);
            if(Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            casting(fieldValue, field, res);
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid field name.");
        }
    }

    private void casting(String fieldValue, Field field, T res) throws IllegalAccessException{
        Class<?> fieldType = field.getType();
        if (fieldType == boolean.class) field.setBoolean(res, Boolean.parseBoolean(fieldValue));
        else if (fieldType == char.class) field.setChar(res, fieldValue.toCharArray()[0]);
        else if (fieldType == byte.class) field.setByte(res, Byte.parseByte(fieldValue));
        else if (fieldType == short.class) field.setShort(res, Short.parseShort(fieldValue));
        else if (fieldType == int.class) field.setInt(res, Integer.parseInt(fieldValue));
        else if (fieldType == long.class) field.setLong(res, Long.parseLong(fieldValue));
        else if (fieldType == float.class) field.setFloat(res, Float.parseFloat(fieldValue));
        else if (fieldType == double.class) field.setDouble(res, Double.parseDouble(fieldValue));
        else if (fieldType == String.class) field.set(res, fieldValue);
    }
}

interface Savable {
    String getPath();
}