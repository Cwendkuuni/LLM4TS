import com.google.gson.*;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectionTypeAdapterFactory() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Test creation of ReflectiveTypeAdapterFactory
        Gson gson = new GsonBuilder().setExcluder(new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }
        }).create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                gson.constructorConstructor,
                gson.fieldNamingPolicy,
                gson.excluder);

        // Test serialization and deserialization of simple JavaBeans
        TestBean javaBean = new TestBean("Hello", 1);
        JsonWriter writer = new JsonWriter(System.out);
        gson.toJson(javaBean, writer);
        writer.flush();
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(gson.toJson(javaBean)));
        TestBean deserializedBean = gson.fromJson(reader, TestBean.class);
        Assert.assertEquals(javaBean, deserializedBean);

        // Test serialization and deserialization of JavaBeans with custom field names
        TestBeanWithCustomNames customBean = new TestBeanWithCustomNames("World", 2);
        writer = new JsonWriter(System.out);
        gson.toJson(customBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(customBean)));
        TestBeanWithCustomNames deserializedCustomBean = gson.fromJson(reader, TestBeanWithCustomNames.class);
        Assert.assertEquals(customBean, deserializedCustomBean);

        // Test handling of excluded fields
        TestBeanExcluded excludedBean = new TestBeanExcluded("Excluded", null);
        writer = new JsonWriter(System.out);
        gson.toJson(excludedBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(excludedBean)));
        TestBeanExcluded deserializedExcludedBean = gson.fromJson(reader, TestBeanExcluded.class);
        Assert.assertNull(deserializedExcludedBean.stringField);

        // Test handling of JSON fields with the same name as JavaBean fields but different case
        TestBeanCaseSensitive caseSensitiveBean = new TestBeanCaseSensitive("Upper", 3);
        writer = new JsonWriter(System.out);
        gson.toJson(caseSensitiveBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(caseSensitiveBean)));
        TestBeanCaseSensitive deserializedCaseSensitiveBean = gson.fromJson(reader, TestBeanCaseSensitive.class);
        Assert.assertEquals("Upper", deserializedCaseSensitiveBean.stringField);

        // Test handling of JSON fields with different names than JavaBean fields
        TestBeanWithCustomNamesJsonFields customJsonFieldsBean = new TestBeanWithCustomNamesJsonFields("CustomName", 4);
        writer = new JsonWriter(System.out);
        gson.toJson(customJsonFieldsBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(customJsonFieldsBean)));
        TestBeanWithCustomNamesJsonFields deserializedCustomJsonFieldsBean = gson.fromJson(reader, TestBeanWithCustomNamesJsonFields.class);
        Assert.assertEquals("CustomName", deserializedCustomJsonFieldsBean.customName);
        Assert.assertEquals(4, deserializedCustomJsonFieldsBean.integerField);

        // Test handling of JSON null values
        TestBeanWithNullFields nullFieldsBean = new TestBeanWithNullFields(null, null);
        writer = new JsonWriter(System.out);
        gson.toJson(nullFieldsBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(nullFieldsBean)));
        TestBeanWithNullFields deserializedNullFieldsBean = gson.fromJson(reader, TestBeanWithNullFields.class);
        Assert.assertNull(deserializedNullFieldsBean.stringField);
        Assert.assertNull(deserializedNullFieldsBean.integerField);

        // Test handling of JSON null values in excluded fields
        TestBeanExcludedWithNullFields nullExcludedBean = new TestBeanExcludedWithNullFields(null, null);
        writer = new JsonWriter(System.out);
        gson.toJson(nullExcludedBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(nullExcludedBean)));
        TestBeanExcludedWithNullFields deserializedNullExcludedBean = gson.fromJson(reader, TestBeanExcludedWithNullFields.class);
        Assert.assertNull(deserializedNullExcludedBean.stringField);

        // Test handling of JSON arrays
        TestBeanArray arrayBean = new TestBeanArray(new TestBean[] {new TestBean("Element1"), new TestBean("Element2")});
        writer = new JsonWriter(System.out);
        gson.toJson(arrayBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(arrayBean)));
        TestBeanArray deserializedArrayBean = gson.fromJson(reader, TestBeanArray.class);
        Assert.assertArrayEquals(new TestBean[] {new TestBean("Element1"), new TestBean("Element2")}, deserializedArrayBean.beans);

        // Test handling of JSON objects
        TestBeanObject objectBean = new TestBeanObject(new HashMap<String, String>() {{
            put("Key1", "Value1");
            put("Key2", "Value2");
        }});
        writer = new JsonWriter(System.out);
        gson.toJson(objectBean, writer);
        writer.flush();
        writer.close();

        reader = new JsonReader(new StringReader(gson.toJson(objectBean)));
        TestBeanObject deserializedObjectBean = gson.fromJson(reader, TestBeanObject.class);
        Assert.assertEquals(new HashMap<String, String>() {{
            put("Key1", "Value1");
            put("Key2", "Value2");
        }}, deserializedObjectBean.map);
    }

    // Test JavaBeans
    static class TestBean {
        private String stringField;
        private int integerField;

        public TestBean(String stringField, int integerField) {
            this.stringField = stringField;
            this.integerField = integerField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntegerField() {
            return integerField;
        }

        public void setIntegerField(int integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with custom names
    static class TestBeanWithCustomNames {
        private String customName;
        private int integerField;

        public TestBeanWithCustomNames(String customName, int integerField) {
            this.customName = customName;
            this.integerField = integerField;
        }

        public String getCustomName() {
            return customName;
        }

        public void setCustomName(String customName) {
            this.customName = customName;
        }

        public int getIntegerField() {
            return integerField;
        }

        public void setIntegerField(int integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with excluded fields
    static class TestBeanExcluded {
        private String stringField;
        private Integer integerField;

        public TestBeanExcluded(String stringField, Integer integerField) {
            this.stringField = stringField;
            this.integerField = integerField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public Integer getIntegerField() {
            return integerField;
        }

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with excluded fields and null values
    static class TestBeanExcludedWithNullFields {
        private String stringField;
        private Integer integerField;

        public TestBeanExcludedWithNullFields(String stringField, Integer integerField) {
            this.stringField = stringField;
            this.integerField = integerField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public Integer getIntegerField() {
            return integerField;
        }

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with case-sensitive JSON field names
    static class TestBeanCaseSensitive {
        private String stringField;
        private int integerField;

        public TestBeanCaseSensitive(String stringField, int integerField) {
            this.stringField = stringField;
            this.integerField = integerField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntegerField() {
            return integerField;
        }

        public void setIntegerField(int integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with custom JSON field names
    static class TestBeanWithCustomNamesJsonFields {
        private String customName;
        private int integerField;

        public TestBeanWithCustomNamesJsonFields(String customName, int integerField) {
            this.customName = customName;
            this.integerField = integerField;
        }

        public String getCustomName() {
            return customName;
        }

        public void setCustomName(String customName) {
            this.customName = customName;
        }

        public int getIntegerField() {
            return integerField;
        }

        public void setIntegerField(int integerField) {
            this.integerField = integerField;
        }
    }

    // Test JavaBeans with arrays
    static class TestBeanArray {
        private TestBean[] beans;

        public TestBeanArray(TestBean[] beans) {
            this.beans = beans;
        }

        public TestBean[] getBeans() {
            return beans;
        }

        public void setBeans(TestBean[] beans) {
            this.beans = beans;
        }
    }

    // Test JavaBeans with maps
    static class TestBeanObject {
        private Map<String, String> map;

        public TestBeanObject(Map<String, String> map) {
            this.map = map;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}