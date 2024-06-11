import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);

        AnnotatedMember accessor = new AnnotatedMember(MyTestClass.class.getMethod("getMapProperty"));
        BeanProperty property = new BeanProperty(MyTestClass.class, "mapProperty", accessor);
        MapSerializer mapSerializer = new StdSerializer<>((Map<?, ?>) null) {};
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws IOException {
        MyTestClass testClass = new MyTestClass();
        testClass.mapProperty = new HashMap<>();
        testClass.mapProperty.put("key1", "value1");

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(anyGetterWriter._property, anyGetterWriter._accessor, anyGetterWriter._mapSerializer);

        byte[] actualBytes = objectMapper.writeValueAsBytes(testClass);
        String expectedJson = "{\"key1\":\"value1\"}";
        String actualJson = new String(actualBytes, "UTF-8");

        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNullValue() throws Exception {
        MyTestClass testClass = new MyTestClass();
        anyGetterWriter.getAndSerialize(testClass, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        MyTestClass testClass = new MyTestClass();
        testClass.mapProperty = 42;

        anyGetterWriter.getAndSerialize(testClass, null, null);
    }

    @Test
    public void testGetAndFilter() throws IOException {
        MyTestClass testClass = new MyTestClass();
        testClass.mapProperty = new HashMap<>();
        testClass.mapProperty.put("key1", "value1");
        testClass.mapProperty.put("key2", "value2");

        PropertyFilter filter = new Filter() {
            @Override
            public boolean includes(String name) {
                return !name.equals("key2");
            }
        };

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(anyGetterWriter._property, anyGetterWriter._accessor, anyGetterWriter._mapSerializer);

        byte[] actualBytes = objectMapper.writeValueAsBytes(testClass);
        String expectedJson = "{\"key1\":\"value1\"}";
        String actualJson = new String(actualBytes, "UTF-8");

        assertEquals(expectedJson, actualJson);

        Map<String, Object> deserializedMap = objectMapper.readValue(new ByteArrayInputStream(actualBytes), Map.class);
        assertEquals("value1", deserializedMap.get("key1"));
        assertNull(deserializedMap.get("key2"));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNullValue() throws Exception {
        MyTestClass testClass = new MyTestClass();
        AnnotatedMember accessor = new AnnotatedMember(MyTestClass.class.getMethod("getMapProperty"));
        BeanProperty property = new BeanProperty(MyTestClass.class, "mapProperty", accessor);
        MapSerializer mapSerializer = new StdSerializer<>((Map<?, ?>) null) {};
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
        anyGetterWriter.getAndFilter(testClass, null, null, null);
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider serializerProvider = new SerializerProvider() {};
        anyGetterWriter.resolve(serializerProvider);
        assertNotNull(anyGetterWriter._mapSerializer);
    }

    static class Filter {
        public boolean includes(String name) {
            return true;
        }
    }

    static class MyTestClass {
        private Map<String, String> mapProperty;

        public Map<String, String> getMapProperty() {
            return mapProperty;
        }
    }
}