import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws IOException {
        // Given
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingStrategy() {}, new Excluder());
        TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;

        // When
        TypeAdapter<MyClass> adapter = gson.getDelegateAdapter(this.getClass(), typeAdapterFactory);

        // Then
        MyClass myClass = new MyClass();
        MyClass deserializedMyClass = gson.fromJson("{\"name\":\"John\", \"age\":30}", MyClass.class);
        Assert.assertNotNull(deserializedMyClass);

        // Test serialization
        String jsonString = gson.toJson(myClass);
        Assert.assertNotNull(jsonString);
        MyClass newMyClass = gson.fromJson(jsonString, MyClass.class);
        Assert.assertEquals(myClass, newMyClass);

        // Test deserialization with missing field
        String jsonStringMissingField = "{\"name\":\"John\"}";
        MyClass missingFieldMyClass = gson.fromJson(jsonStringMissingField, MyClass.class);
        Assert.assertNull(missingFieldMyClass.getAge());

        // Test deserialization with null value
        String jsonStringNullValue = "{\"name\":\"John\", \"age\":null}";
        MyClass nullValueMyClass = gson.fromJson(jsonStringNullValue, MyClass.class);
        Assert.assertNull(nullValueMyClass.getAge());

        // Test deserialization with empty string value
        String jsonStringEmptyValue = "{\"name\":\"John\", \"age\":\"\"}";
        MyClass emptyValueMyClass = gson.fromJson(jsonStringEmptyValue, MyClass.class);
        Assert.assertEquals(0, emptyValueMyClass.getAge());

        // Test deserialization with non-number value
        String jsonStringNonNumberValue = "{\"name\":\"John\", \"age\":\"string\"}";
        try {
            gson.fromJson(jsonStringNonNumberValue, MyClass.class);
            Assert.fail();
        } catch (JsonSyntaxException e) {
            // expected
        }

        // Test serialization with empty map
        MyClass emptyMapMyClass = new MyClass(new HashMap<>());
        String jsonStringEmptyMap = gson.toJson(emptyMapMyClass);
        MyClass deserializedEmptyMapMyClass = gson.fromJson(jsonStringEmptyMap, MyClass.class);
        Assert.assertEquals(emptyMapMyClass, deserializedEmptyMapMyClass);

        // Test serialization with non-empty map
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        MyClass mapMyClass = new MyClass(map);
        String jsonStringMap = gson.toJson(mapMyClass);
        MyClass deserializedMapMyClass = gson.fromJson(jsonStringMap, MyClass.class);
        Assert.assertEquals(map, deserializedMapMyClass.getMap());
    }

    static class MyClass {
        private String name;
        private Integer age;
        private Map<String, String> map;

        public MyClass() {
        }

        public MyClass(String name) {
            this.name = name;
        }

        public MyClass(Integer age) {
            this.age = age;
        }

        public MyClass(Map<String, String> map) {
            this.map = map;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}