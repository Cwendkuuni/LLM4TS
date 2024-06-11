package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    private ObjectCodec objectCodec;
    private GeneratorBase generatorBase;
    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        objectCodec = mock(ObjectCodec.class);
        jsonWriteContext = mock(JsonWriteContext.class);
        when(objectCodec.version()).thenReturn(new Version("1.0.0"));
        generatorBase = new GeneratorBase(0, objectCodec, jsonWriteContext) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };
    }

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() throws IOException {
        assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
        generatorBase.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() throws IOException {
        assertTrue(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        generatorBase.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        assertEquals(0, generatorBase.getFeatureMask());
        generatorBase.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generatorBase.getFeatureMask());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentValue() throws IOException {
        generatorBase.getCurrentValue();
    }

    @Test
    public void testSetCurrentValue() throws IOException {
        generatorBase.setCurrentValue(null);
        assertNull(generatorBase.getCurrentValue());
        generatorBase.setCurrentValue("test");
        assertEquals("test", generatorBase.getCurrentValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteFieldName_Null() throws IOException {
        generatorBase.writeFieldName(null);
    }

    @Test
    public void testWriteString() throws IOException {
        generatorBase.writeString("test");
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generatorBase.writeRawValue("test");
    }

    @Test
    public void testWriteRawValue_CharArray() throws IOException {
        byte[] test = "test".getBytes();
        generatorBase.writeRawValue(test, 0, test.length);
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        generatorBase.writeRawValue("test");
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        generatorBase.writeRawValue(new StringShield("test"));
    }

    @Test(expected = IOException.class)
    public void testWriteBinary() throws IOException {
        generatorBase.writeBinary(null, null, 0);
    }

    @Test
    public void testWriteNumber_Int() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_Long() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_Double() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_Float() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        generatorBase.writeNumber(new BigDecimal("123.45"));
    }

    @Test
    public void testWriteBoolean() throws IOException {
        // Not implemented at this level
    }

    @Test
    public void testWriteNull() throws IOException {
        generatorBase.writeNull();
    }

    @Test
    public void testWriteObject() throws IOException {
        Object object = new Object();
        generatorBase.writeObject(object);
    }

    @Test
    public void testWriteTree() throws IOException {
        Object tree = new Object();
        generatorBase.writeTree((TreeNode) tree);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlush() throws IOException {
        generatorBase.flush();
    }

    @Test
    public void testClose() throws IOException {
        generatorBase.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(generatorBase.isClosed());
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }

    @Test
    public void test_releaseBuffers() throws IOException {
        // Package method for testing
    }

    @Test
    public void test_verifyValueWrite() throws IOException {
        // Package method for testing
    }
}

class StringShield implements SerializableString {

    private final String value;

    public StringShield(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}