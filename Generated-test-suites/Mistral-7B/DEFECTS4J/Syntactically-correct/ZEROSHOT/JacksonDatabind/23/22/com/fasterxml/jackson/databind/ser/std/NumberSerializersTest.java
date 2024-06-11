import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        Map<String, JsonSerializer<?>> deserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(deserializers);
        this.mapper = new ObjectMapper();
        this.mapper.setSerializerFactory(new DefaultSerializerFactory(deserializers));
    }

    @Test
    public void testIntegerSerialization() throws JsonProcessingException {
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);
    }

    @Test
    public void testLongSerialization() throws JsonProcessingException {
        Long number = 123L;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);
    }

    @Test
    public void testFloatSerialization() throws JsonProcessingException {
        Float number = 123f;
        String json = mapper.writeValueAsString(number);
        assertEquals("123.0", json);
    }

    @Test
    public void testDoubleSerialization() throws JsonProcessingException {
        Double number = 123.45;
        String json = mapper.writeValueAsString(number);
        assertEquals("123.45", json);
    }

    @Test
    public void testShortSerialization() throws JsonProcessingException {
        Short number = (short) 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);
    }

    @Test
    public void testJsonFormatAnnotation() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("\"123\"", json);
    }

    @Test
    public void testIntegerDeserialization() throws JsonProcessingException {
        Integer deserializedNumber = mapper.readValue("123", Integer.class);
        assertEquals(123, deserializedNumber.intValue());
    }

    @Test
    public void testLongDeserialization() throws JsonProcessingException {
        Long deserializedNumber = mapper.readValue("123", Long.class);
        assertEquals(123L, deserializedNumber.longValue());
    }

    @Test
    public void testFloatDeserialization() throws JsonProcessingException {
        Float deserializedNumber = mapper.readValue("123.0", Float.class);
        assertEquals(123f, deserializedNumber);
    }

    @Test
    public void testDoubleDeserialization() throws JsonProcessingException {
        Double deserializedNumber = mapper.readValue("123.45", Double.class);
        assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);
    }

    @Test
    public void testShortDeserialization() throws JsonProcessingException {
        Short deserializedNumber = mapper.readValue("123", Short.class);
        assertEquals((short) 123, deserializedNumber.shortValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws JsonProcessingException {
        mapper.readValue("invalid", Integer.class);
    }
}