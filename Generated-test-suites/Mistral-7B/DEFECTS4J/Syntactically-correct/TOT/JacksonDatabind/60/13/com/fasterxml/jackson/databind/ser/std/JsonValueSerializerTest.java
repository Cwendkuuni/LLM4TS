package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class JsonValueSerializerTest extends BaseTest {

    private JsonValueSerializer _serde;
    private Object _bean;

    @BeforeMethod
    public void setup() throws InstantiationException, IllegalAccessException {
        Class<Object> clazz = Object.class;
        AnnotatedMethod method = AnnotatedMethod.construct(getClass(), "getValue", clazz);
        JsonSerializer<Object> valueSerializer = new JsonStringSerializer();
        _serde = new JsonValueSerializer(method, valueSerializer);
        _bean = new TestBean();
    }

    @Test
    public void testNullValue() throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = factory.createGenerator(sw);
        _serde.serialize(_bean, gen, new SerializerProvider.StandardProvider());
        assertEquals(sw.toString(), "null");
    }

    @Test
    public void testSimpleValue() throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = factory.createGenerator(sw);
        _serde.serialize(_bean, gen, new SerializerProvider.StandardProvider());
        assertEquals(sw.toString(), "testStringValue");
    }

    @Test
    public void testSerializableValue() throws IOException {
        TestSerializable testSerializable = new TestSerializable("testSerializableValue");
        _serde._accessorMethod = AnnotatedMethod.construct(TestSerializable.class, "getValue");
        _serde._valueSerializer = null;
        StringWriter sw = new StringWriter();
        JsonGenerator gen = factory.createGenerator(sw);
        _serde.serialize(testSerializable, gen, new SerializerProvider.StandardProvider());
        assertEquals(sw.toString(), "testSerializableValue");

        _serde._valueSerializer = new JsonStringSerializer();
        _serde._forceTypeInformation = true;
        sw = new StringWriter();
        gen = factory.createGenerator(sw);
        _serde.serialize(testSerializable, gen, new SerializerProvider.StandardProvider());
        assertEquals(sw.toString(), "\"testSerializableValue\"");
    }

    @Test
    public void testJsonDeserializedValue() throws IOException {
        TestDeserializable testDeserializable = mapper.readValue(new StringReader("testStringValue"), TestDeserializable.class);
        StringWriter sw = new StringWriter();
        JsonGenerator gen = factory.createGenerator(sw);
        _serde.serialize(testDeserializable, gen, new SerializerProvider.StandardProvider());
        assertEquals(sw.toString(), "testDeserializedValue");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testErrorScenario() throws IOException {
        _serde._valueSerializer = new ThrowingSerializer();
        StringWriter sw = new StringWriter();
        JsonGenerator gen = factory.createGenerator(sw);
        _serde.serialize(_bean, gen, new SerializerProvider.StandardProvider());
    }

    @Test
    public void testSchemaAware() throws IOException {
        _serde = new JsonValueSerializer(new TestSchemaAware(), new JsonStringSerializer());
        ObjectMapper mapper = new ObjectMapper();
        SchemaFactory sf = new SchemaFactoryImpl();
        JsonSchema schema = sf.getSchema(mapper.getFactory().getJsonFactory());
        schema = _serde.getSchema(mapper.getSerializerProvider(), null);
        assertEquals(schema.getTitle(), "Test Schema Aware Class");
    }

    static class TestBean {
        public String getValue() {
            return "testStringValue";
        }
    }

    static class TestSerializable implements Serializable {
        private static final long serialVersionUID = 1L;

        public TestSerializable(String value) {
            this._value = value;
        }

        public String getValue() {
            return _value;
        }

        private String _value;
    }

    static class TestDeserializable {
        public String value;

        @JsonDeserialize(using = JsonValueDeserializer.class)
        public TestDeserializable() {}

        public String getValue() {
            return value;
        }
    }

    static class TestSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider prov) throws IOException {
            throw new RuntimeException("Throwing serializer.");
        }
    }

    static class ThrowingSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider prov) throws IOException {
            throw new IOException("Throwing serializer with IOException.");
        }
    }

    static class TestSchemaAware {
        public String getValue() {
            return "Test Schema Aware Class";
        }
    }
}