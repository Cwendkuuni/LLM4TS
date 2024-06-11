package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    private GeneratorBase generator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        generator = objectMapper.getPolymorphicSerializer(String.class).getGenerator();
    }

    @Test
    public void testVersion() throws IOException {
        assertEquals("2.13.0", generator.version().toString());
    }

    @Test
    public void testIsEnabled() throws IOException {
        // Default features
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Enable some features
        generator = generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = generator.enable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Enable all features
        generator = generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS | Feature.ESCAPE_NON_ASCII | Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        assertEquals(0, generator.getFeatureMask());

        generator = generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = generator.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        generator = generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testWriteString() throws IOException {
        String text = "Hello, World!";
        generator.writeString(text);
        assertEquals(text, generator.getCurrentValue().toString());
    }

    @Test
    public void testWriteRawString() throws IOException {
        String text = "Hello, World!";
        generator.writeRawValue(text);
        assertEquals(text, generator.getCurrentValue().toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        String text = "Hello, World!";
        generator.writeRawValue(text.getBytes());
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueWithOffset() throws IOException {
        String text = "Hello, World!";
        generator.writeRawValue(text.getBytes(), 0, text.length());
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertTrue(generator.getCurrentValue().booleanValue());

        generator.writeBoolean(false);
        assertFalse(generator.getCurrentValue().booleanValue());
    }

    @Test
    public void testWriteNumber() throws IOException {
        // Test int
        generator.writeNumber(123);
        assertEquals(123, ((Number) generator.getCurrentValue()).intValue());

        // Test long
        generator.writeNumber(123456789012345L);
        assertEquals(123456789012345L, ((Number) generator.getCurrentValue()).longValue());

        // Test double
        generator.writeNumber(123.45);
        assertEquals(123.45, ((Number) generator.getCurrentValue()).doubleValue(), 0.001);

        // Test BigDecimal
        BigDecimal decimal = new BigDecimal("123.456");
        generator.writeNumber(decimal);
        assertEquals(decimal, ((BigDecimal) generator.getCurrentValue()));
    }

    @Test
    public void testWriteObject() throws IOException {
        String text = "Hello, World!";
        generator.writeObject(text);
        assertEquals(text, generator.getCurrentValue().toString());

        generator.close();
        assertNull(generator.getCurrentValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteObjectWithNullValue() throws IOException {
        generator.writeObject(null);
    }

    @Test
    public void testWriteTree() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Mustang\"},{\"color\":\"blue\",\"model\":\"Corvette\"}]}";
        ObjectNode node = objectMapper.readValue(new StringReader(json), ObjectNode.class);
        generator.writeTree(node);
        assertEquals(json, generator.getCurrentValue().toString());

        generator.close();
        assertNull(generator.getCurrentValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteTreeWithNullValue() throws IOException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", null);
        generator.writeTree(node);
    }

    @Test
    public void testFlush() throws IOException {
        String text = "Hello, World!";
        generator.writeString(text);
        generator.flush();
        assertEquals(text, generator.getCurrentValue().toString());
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testClose() throws IOException {
        String text = "Hello, World!";
        generator.writeString(text);
        generator.close();
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(generator.isClosed());
        generator.close();
        assertTrue(generator.isClosed());
    }

    @After
    public void tearDown() throws Exception {
        if (generator != null) {
            generator.close();
        }
    }
}