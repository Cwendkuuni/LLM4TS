package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 *
 * @author John Doe
 */
public class StringArrayDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithSingleElement() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"" + "test" + "\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { "test" }, result);
    }

    @Test
    public void testDeserializeArrayWithMultipleElements() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"test1\", \"test2\", \"test3\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { "test1", "test2", "test3" }, result);
    }

    @Test
    public void testDeserializeArrayWithNullElement() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[null, \"test1\", null, \"test2\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { null, "test1", null, "test2" }, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("{test: \"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[1, 2, 3]"); // not String[]
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createNumberDeserializer(null, null));
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { "1", "2", "3" }, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingStartArrayToken() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("test1, test2");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader objectReader = mapper.readerForValueType(String[].class);
        String[] result = objectReader.readValue(jp);
        assertNull(result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer1 = StringArrayDeserializer.instance;
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().getDeserializerFactory()
                .findValueDeserializer(String.class, new AnnotatedClass(String.class));
        StringArrayDeserializer deserializer2 = new StringArrayDeserializer(stringDeserializer);
        assertEquals(deserializer1.getClass(), deserializer1.getClass());
        assertEquals(deserializer2.getClass(), deserializer2.getClass());
        assertNotEquals(deserializer1, deserializer2);
    }

    private boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
        return deserializer != null && deserializer.getClass() == StringDeserializer.class;
    }

    @Test
    public void testDeserializeNullElementWithDefaultDeserializer() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[null, null]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { null, null }, result);
    }

    @Test
    public void testDeserializeNullElementWithCustomDeserializer() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[null, null]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader objectReader = mapper.readerForValueType(String[].class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().getDeserializerFactory()
                .findValueDeserializer(String.class, new AnnotatedClass(String.class)));
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { null, null }, result);
    }

    @Test
    public void testDeserializeSingleStringWithTypeDeserializer() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"test\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = mapper.getDeserializationConfig().findValueDeserializer(String[].class);
        String[] result = StringArrayDeserializer.instance.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(new String[] { "test" }, result);
    }
}