package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Test case for {@link StringArrayDeserializer}.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    @JacksonStdImpl
    public static final class TestContext extends DeserializationContext {
        public TestContext(ObjectDeserializer deserializer) {
            super(deserializer, null);
        }
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"hello\", \"world\", \"!\" ]";
        String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser(json), new TestContext(null));
        assertArrayEquals(new String[]{"hello", "world", "!"}, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        String json = "[null, \"custom\", null]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new LocalDateDeserializer());
        String[] result = deserializer.deserialize(MAPPER.getFactory().createParser(json), new TestContext(deserializer));
        assertArrayEquals(new String[]{"null", "custom", null}, result);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws IOException {
        String json = "{\"foo\": \"bar\"}";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(MAPPER.getFactory().createParser(json), new TestContext(deserializer));
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        String json = "[null, null, \"custom\"]";

        // If no custom deserializer is provided, should fall back to default deserialization
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result1 = deserializer.deserialize(MAPPER.getFactory().createParser(json), new TestContext(deserializer));
        assertArrayEquals(new String[]{"null", null, "custom"}, result1);

        // If a custom deserializer is provided through contextualization, it should be used
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer();
        JsonDeserializer<LocalDate> localDateDeserializer = new LocalDateDeserializer();
        String[] result2 = customDeserializer.createContextual(new TestContext(customDeserializer), MAPPER.findBeanProperty("", ""))
                .deserialize(MAPPER.getFactory().createParser(json), new TestContext(customDeserializer));
        assertArrayEquals(new String[]{"null", null, "custom"}, result2);

        // If a custom deserializer is provided through contextualization, it should be used recursively
        LocalDateDeserializer localDateDeserializerWrapper = new LocalDateDeserializer() {
            @Override
            public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
                return new LocalDateDeserializer();
            }
        };
        StringArrayDeserializer customDeserializerWrapper = new StringArrayDeserializer(localDateDeserializerWrapper);
        String[] result3 = customDeserializerWrapper.createContextual(new TestContext(customDeserializerWrapper), MAPPER.findBeanProperty("", ""))
                .deserialize(MAPPER.getFactory().createParser(json), new TestContext(customDeserializerWrapper));
        assertArrayEquals(new String[]{"null", null, "custom"}, result3);
    }
}