import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldPropertyTest {

    private ObjectNode objectNode;
    private JsonFactory jsonFactory;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        objectNode = jsonFactory.createObjectNode();
    }

    @Test
    public void testSimpleDeserializeAndSet() throws IOException {
        Object target = new Object();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        fieldProperty.deserializeAndSet(jsonParser("{\"testField\": 1}"), deserializationContext(target), target);
        assertEquals(1, target.getClass().getField("testField").get(target));
    }

    @Test
    public void testNullDeserializeAndSet() throws IOException {
        Object target = new Object();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        if (fieldProperty._skipNulls) {
            assertTrue(target.getClass().getField("testField").get(target) == null);
        } else {
            fieldProperty.deserializeAndSet(jsonParser("{\"testField\": null}"), deserializationContext(target), target);
            assertNull(target.getClass().getField("testField").get(target));
        }
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object target = new Object();
        JavaType javaType = new TypeFactory().constructType(Integer.class);
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, new TypeDeserializer() {}, null, javaType, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        fieldProperty.deserializeAndSet(jsonParser("{\"testField\": 1}"), deserializationContext(target), target);
        assertEquals(1, target.getClass().getField("testField").get(target));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidType() throws IOException {
        Object target = new Object();
        JavaType javaType = new TypeFactory().constructType(String.class);
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, new TypeDeserializer() {}, null, javaType, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        fieldProperty.deserializeAndSet(jsonParser("{\"testField\": 1}"), deserializationContext(target), target);
        fail("Expected IOException");
    }

    @Test
    public void testSet() throws IOException {
        Object target = new Object();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        fieldProperty.set(target, 1);
        assertEquals(1, target.getClass().getField("testField").get(target));
    }

    @Test
    public void testSetNull() throws IOException {
        Object target = new Object();
        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class), "testField"), null, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Target.class), "testField"), Target.class));
        fieldProperty.set(target, null);
        assertNull(target.getClass().getField("testField").get(target));
    }

    private JsonParser jsonParser(String json) throws IOException {
        return jsonFactory.createParser(json);
    }

    private DeserializationContext deserializationContext(Object target) {
        return new TestDeserializationContext();
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public DeserializationConfig config() {
            return new TestDeserializationConfig();
        }

        private static class TestDeserializationConfig extends DeserializationConfig {
            @Override
            public boolean isEnabled(MapperFeature feature) {
                return true;
            }
        }
    }
}