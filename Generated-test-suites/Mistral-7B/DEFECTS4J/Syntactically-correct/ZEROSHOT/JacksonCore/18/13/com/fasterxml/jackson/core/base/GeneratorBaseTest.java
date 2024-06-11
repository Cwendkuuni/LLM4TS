package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    private GeneratorBase generator;
    private ObjectCodec objectCodec;
    private JsonWriteContext writeContext;
    private PrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        objectCodec = mock(ObjectCodec.class);
        writeContext = new JsonWriteContext(null);
        prettyPrinter = new DefaultPrettyPrinter();
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return prettyPrinter;
            }

            @Override
            protected void _releaseBuffers() {
                // no-op
            }

            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // no-op
            }
        };
    }

    @Test
    public void testIsEnabled() throws IOException {
        Feature feature = Feature.WRITE_NUMBERS_AS_STRINGS;
        generator.isEnabled(feature);
        assertTrue("Write numbers as strings feature should be enabled", generator.isEnabled(feature));
        generator.disable(feature);
        assertFalse("Write numbers as strings feature should be disabled", generator.isEnabled(feature));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        assertEquals("GeneratorBase should have feature mask 0x" + Integer.toHexString(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask()),
                Integer.toHexString(generator.getFeatureMask()), generator.getFeatureMask());
    }

    @Test
    public void testVersion() throws IOException {
        Version version = generator.version();
        assertEquals("GeneratorBase version should match", VersionUtil.versionFor(GeneratorBase.class), version);
    }

    @Test
    public void testGetCurrentValue() throws IOException {
        generator.setCurrentValue(null);
        assertNull("Current value should be null", generator.getCurrentValue());
        generator.setCurrentValue("test");
        assertEquals("Current value should be test", "test", generator.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream out = outputStream;
        generator.setCurrentValue(out);
        assertSame("Current output stream should be set to the provided one", out, generator.getCurrentValue());
    }

    @Test
    public void testConfigure() throws IOException {
        // no tests as method is empty
    }

    @Test
    public void testEnable() throws IOException {
        Feature feature = Feature.WRITE_NUMBERS_AS_STRINGS;
        generator.enable(feature);
        assertTrue("Write numbers as strings feature should be enabled", generator.isEnabled(feature));
        generator.disable(feature);
        assertFalse("Write numbers as strings feature should be disabled", generator.isEnabled(feature));
    }

    @Test
    public void testDisable() throws IOException {
        Feature feature = Feature.WRITE_NUMBERS_AS_STRINGS;
        generator.disable(feature);
        assertFalse("Write numbers as strings feature should be disabled", generator.isEnabled(feature));
        generator.enable(feature);
        assertTrue("Write numbers as strings feature should be enabled", generator.isEnabled(feature));
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        int mask = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        generator.setFeatureMask(mask);
        assertTrue("Write numbers as strings feature should be enabled", generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue("Escape non-ASCII feature should be enabled", generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        // Test with different feature mask
        generator.setFeatureMask(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertEquals("GeneratorBase feature mask should be 0x" + Integer.toHexString(mask),
                Integer.toHexString(generator.getFeatureMask()), generator.getFeatureMask());
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        int newFeatures = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.overrideStdFeatures(newFeatures, Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue("Strict duplicate detection feature should be enabled", generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        generator.overrideStdFeatures(newFeatures, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue("Write numbers as strings feature should be enabled", generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.overrideStdFeatures(newFeatures, Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue("Escape non-ASCII feature should be enabled", generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals("GeneratorBase feature mask should be 0x" + Integer.toHexString(newFeatures),
                Integer.toHexString(generator.getFeatureMask()), generator.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generator.useDefaultPrettyPrinter();
        assertSame("Pretty printer should be set to the default one", prettyPrinter, generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws IOException {
        generator.setCodec(objectCodec);
        assertSame("ObjectCodec should be set to the provided one", objectCodec, generator.getCodec());
    }

    @Test
    public void testGetCodec() throws IOException {
        assertSame("ObjectCodec should be the one set earlier", objectCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() throws IOException {
        assertSame("Output context should be the one set in constructor", writeContext, generator.getOutputContext());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("test");
        assertEquals("Written string should be test", "test", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueString() throws IOException {
        generator.writeRawValue("test");
        assertEquals("Written raw value should be test", "test", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueStringOffsetLength() throws IOException {
        generator.writeRawValue("test", 0, 3);
        assertEquals("Written raw value should be test", "tes", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueCharArray() throws IOException {
        char[] characters = {'t', 'e', 's', 't'};
        generator.writeRawValue(characters, 0, characters.length);
        assertEquals("Written raw value should be test", "test", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueSerializableString() throws IOException {
        SerializableString string = new SerializableString("test");
        generator.writeRawValue(string);
        assertEquals("Written raw value should be test", "test", generator.getCurrentValue());
    }

    @Test
    public void testWriteObject() throws IOException {
        String value = "test";
        generator.writeObject(value);
        assertEquals("Written value should be test", value, generator.getCurrentValue());
    }

    @Test
    public void testWriteTree() throws IOException {
        Object value = new Object();
        generator.writeTree(value);
        assertEquals("Written value should be the provided one", value, generator.getCurrentValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBinaryUnsupportedOperation() throws IOException {
        generator.writeBinary(JsonGenerator.Base64Variant.STANDARD, null, 0);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNumberInt() throws IOException {
        generator.writeNumber(42);
        _reportError("Unexpectedly written a number instead of value");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNumberLong() throws IOException {
        generator.writeNumber(42L);
        _reportError("Unexpectedly written a number instead of value");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNumberDouble() throws IOException {
        generator.writeNumber(42.0);
        _reportError("Unexpectedly written a number instead of value");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNumberBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal(42);
        generator.writeNumber(value);
        _reportError("Unexpectedly written a number instead of value");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        _reportError("Unexpectedly written a boolean instead of value");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteNull() throws IOException {
        generator.writeNull();
        _reportError("Unexpectedly wrote null instead of value");
    }

    @Test
    public void testClose() throws IOException {
        generator.close();
        assertFalse("Generator should be closed", generator.isOpen());
    }

    @Test
    public void testIsClosed() throws IOException {
        generator.isClosed();
        assertTrue("Generator should be open", generator.isOpen());
        generator.close();
        assertTrue("Generator should be closed", generator.isClosed());
    }

    private void _reportError(String msg) throws IOException {
        throw new IOException(String.format("GeneratorBase test failed: %s", msg));
    }
}