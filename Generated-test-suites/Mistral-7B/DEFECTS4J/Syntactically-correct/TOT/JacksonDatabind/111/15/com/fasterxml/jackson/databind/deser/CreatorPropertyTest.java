import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

    private ObjectMapper mapper;
    private CreatorProperty property;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.valueOf(String.class),
                PropertyName.forPrefix("wrapper"),
                null,
                null,
                null,
                -1,
                "valueId",
                PropertyMetadata.STD_OBJ);
    }

    @Test
    public void testDefaultCreation() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{}");
        Object result = property.deserializeAndSet(p, null, null);
        assertNull(result);
    }

    @Test
    public void testCreationWithValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"value\":\"testValue\"}");
        Object result = property.deserializeAndSet(p, null, null);
        assertEquals("testValue", result);
    }

    @Test
    public void testCreationWithInjectableValue() throws IOException {
        Object injectableValue = new Object();
        JsonParser p = mapper.getFactory().createParser("{@valueId: " + mapper.writeValueAsString(injectableValue) + "}");
        Object result = property.deserializeAndSet(p, null, null);
        assertEquals(injectableValue, result);
    }

    @Test(expected = IOException.class)
    public void testMissingSetter() throws IOException {
        property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.valueOf(String.class),
                PropertyName.forPrefix("wrapper"),
                null,
                null,
                null,
                -1,
                "valueId",
                PropertyMetadata.STD_OBJ);
        property.markAsIgnorable();
        JsonParser p = mapper.getFactory().createParser("{\"value\":\"testValue\"}");
        property.deserializeAndSet(p, null, null);
    }

    @Test
    public void testSerialization() throws IOException {
        Object value = "testValue";
        Object result = mapper.writeValueAsString(property.setAndReturn(new Object(), value));
        assertEquals("{\"value\":\"" + value + "\"}", result);
    }

    @Test
    public void testSerializationWithInjectableValue() throws IOException {
        Object injectableValue = new Object();
        Object result = mapper.writeValueAsString(property.setAndReturn(new Object(), injectableValue));
        assertEquals("{\"@valueId\":{" + ToStringSerializer.instance.serializeValueAsString(injectableValue) + "}}", result);
    }

    @Test
    public void testAnnotation() {
        class TestClass {
            @JsonDeserialize(using = CreatorProperty.class)
            private CreatorProperty testProperty;
        }
        TestClass testClass = mapper.readValue(mapper.writeValueAsString(new TestClass()), TestClass.class);
        assertEquals(property.getClass(), testClass.testProperty.getClass());
    }
}