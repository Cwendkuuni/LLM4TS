package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * Test class for {@link GeneratorBase}.
 */
public class GeneratorBaseTest {

    @Test
    public void testVersion() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        Version version = generator.version();
        assertEquals("2.12.3", version.toString());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_BOOLEAN_AS_INTS));

        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_BOOLEAN_AS_INTS.getMask();
        generator = new GeneratorBase(features, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.WRITE_BOOLEAN_AS_INTS));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        int features = generator.getFeatureMask();
        assertEquals(0, features);

        int features2 = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_BOOLEAN_AS_INTS.getMask();
        generator = new GeneratorBase(features2, null);
        assertEquals(features2, generator.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_BOOLEAN_AS_INTS);
        assertTrue(generator.isEnabled(Feature.WRITE_BOOLEAN_AS_INTS));

        int features = Feature.FEATURE_MASK;
        generator = new GeneratorBase(features, null);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_BOOLEAN_AS_INTS);
        assertFalse(generator.isEnabled(Feature.WRITE_BOOLEAN_AS_INTS));
    }

    @Test
    public void testWriteFieldName() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));
        generator.writeFieldName("fieldName");

        assertEquals("\"fieldName\"", out.toString());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));
        generator.writeString("test");

        assertEquals("\"test\"", out.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        generator.writeRawValue("test");
        assertEquals("\"test\"", out.toString());

        generator.writeRawValue("test\\\\");
        assertEquals("\"test\\\\\"", out.toString());

        generator.writeRawValue("\\u0001test");
        assertEquals("\"\\u0001test\"", out.toString());

        generator.writeRawValue("\\ud800\\udc00test");
        assertEquals("\"\uD800\uDC00test\"", out.toString());

        generator.writeRawValue("test\u0001");
        assertEquals("\"test\\u0001\"", out.toString());

        generator.writeRawValue("test\\ud800\\udc00");
        assertEquals("\"test\\uD800\\uDC00\"", out.toString());

        ByteArrayInputStream is = new ByteArrayInputStream("test".getBytes());
        generator.writeRawValue(is, is.available());
        assertEquals("\"test\"", out.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        generator.writeNull();
        assertEquals("null", out.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        generator.writeBoolean(true);
        assertEquals("true", out.toString());

        generator.writeBoolean(false);
        assertEquals("false", out.toString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        generator.writeNumber(1);
        assertEquals("1", out.toString());

        generator.writeNumber(123);
        assertEquals("123", out.toString());

        generator.writeNumber(-1);
        assertEquals("-1", out.toString());

        generator.writeNumber(-123);
        assertEquals("-123", out.toString());

        generator.writeNumber(123.45);
        assertEquals("123.45", out.toString());

        generator.writeNumber(123.45E-5);
        assertEquals("1.2345E-5", out.toString());

        generator.writeNumber(1.2345E2);
        assertEquals("1234.5", out.toString());

        generator.writeNumber(Double.MAX_VALUE);
        assertEquals("4.9402930229508175e308", out.toString());

        generator.writeNumber(Double.MIN_VALUE);
        assertEquals("-4.9402930229508175e-324", out.toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        generator.writeObject(null);
        assertEquals("null", out.toString());

        Object obj = new Object() {};
        generator.writeObject(obj);

        String expected = "{\"class\":\"[Ljava.lang.Object;\",\"fields\":[{}]}";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        TreeNode treeNode = new TreeNode();
        treeNode.setA("test");
        treeNode.setB(123);

        generator.writeTree(treeNode);

        String expected = "{\"A\":\"test\",\"B\":123}";
        assertEquals(expected, out.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteObjectAfterClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.writeObject(new Object());
    }

    @Test
    public void testWriteBinary() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), out));

        // Unsupported operation
        generator.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream("test".getBytes()), "test".length());
    }
}