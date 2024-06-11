import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStringSerializer() throws JsonProcessingException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);

        String value = "Hello World!";
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);

        stringSerializer.serialize(value, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        String serializedValue = stringWriter.toString();
        ObjectNode actualNode = objectMapper.readValue(new StringSource(serializedValue), ObjectNode.class);

        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "string");
        expectedNode.put("value", value);

        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testCustomSerializer() throws JsonProcessingException {
        StdSerializer<CustomClass> customSerializer = new MyCustomSerializer();

        CustomClass value = new CustomClass("Custom Value");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);

        customSerializer.serialize(value, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        String serializedValue = stringWriter.toString();
        ObjectNode actualNode = objectMapper.readValue(new StringSource(serializedValue), ObjectNode.class);

        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "customType");
        expectedNode.put("value", "Custom Value");

        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test(expected = JsonMappingException.class)
    public void testException() throws JsonProcessingException {
        StdSerializer<ExceptionClass> exceptionSerializer = new MyExceptionSerializer();

        ExceptionClass value = new ExceptionClass();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);

        exceptionSerializer.serialize(value, jsonGenerator, objectMapper.getSerializerProvider());

        // Exception should be thrown here
        jsonGenerator.flush();
    }

    static class MyCustomSerializer extends StdSerializer<CustomClass> {
        public MyCustomSerializer() { this(CustomClass.class); }

        protected MyCustomSerializer(Class<CustomClass> t) { super(t); }

        @Override
        public void serialize(CustomClass value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            jgen.writeStartObject();
            jgen.writeFieldName("type");
            jgen.writeValue("customType");
            jgen.writeFieldName("value");
            jgen.writeValue(value.getValue());
            jgen.writeEndObject();
        }
    }

    static class MyExceptionSerializer extends StdSerializer<ExceptionClass> {
        public MyExceptionSerializer() { this(ExceptionClass.class); }

        @SuppressWarnings("unchecked")
        protected MyExceptionSerializer(JavaType type) { super((Class<ExceptionClass>) type.getRawClass()); }

        @Override
        public void serialize(ExceptionClass value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            throw new JsonMappingException("Exception encountered during serialization");
        }
    }

    static class CustomClass {
        private String value;

        public CustomClass() {}

        public CustomClass(String value) { this.value = value; }

        public String getValue() { return value; }
    }

    static class ExceptionClass {}

}