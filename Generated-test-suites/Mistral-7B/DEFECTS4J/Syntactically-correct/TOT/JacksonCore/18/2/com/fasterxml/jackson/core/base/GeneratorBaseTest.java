package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorBaseTest {

    @Mock
    private ObjectCodec objectCodec;

    @InjectMocks
    private GeneratorBase generatorBase;

    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = byteArrayOutputStream;
        jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.getPrettyPrinter()).thenReturn(new DefaultPrettyPrinter());
        when(jsonGenerator.getOutputContext()).thenReturn(generatorBase._writeContext);
        when(jsonGenerator.getCodec()).thenReturn(objectCodec);
        generatorBase = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.QUIRK_MODE_RELAXED.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec, generatorBase._writeContext);
    }

    @Test
    public void testIsEnabled_WriteNumbersAsStrings() throws Exception {
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_StrictDupDetect() throws Exception {
        assertFalse(generatorBase.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));

        generatorBase = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.QUIRK_MODE_RELAXED.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask(), objectCodec, generatorBase._writeContext);
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testIsEnabled_AllFeatures() throws Exception {
        generatorBase = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.QUIRK_MODE_RELAXED.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask() | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask(), objectCodec, generatorBase._writeContext);
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.QUIRK_MODE_RELAXED));
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testWriteString() throws IOException {
        generatorBase.writeString("test");
        assertEquals("test", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteString_Empty() throws IOException {
        generatorBase.writeString("");
        assertEquals("", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteString_Null() throws IOException {
        generatorBase.writeString(null);
        assertNull(byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generatorBase.writeRawValue("test");
        assertEquals("test", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteRawValue_Null() throws IOException {
        generatorBase.writeRawValue(null);
        assertNull(byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteRawValue_Empty() throws IOException {
        generatorBase.writeRawValue("");
        assertEquals("", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        generatorBase.writeNumber(42);
        assertEquals("42", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteNumber_Long() throws IOException {
        generatorBase.writeNumber(Long.MAX_VALUE);
        assertEquals(Long.toString(Long.MAX_VALUE), byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteNumber_Float() throws IOException {
        generatorBase.writeNumber(3.14f);
        assertEquals("3.14", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteNumber_Double() throws IOException {
        generatorBase.writeNumber(3.14);
        assertEquals("3.14", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generatorBase.writeBoolean(true);
        assertEquals("true", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        generatorBase.writeNull();
        assertNull(byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteObject() throws IOException, JsonProcessingException, JsonMappingException {
        generatorBase.writeObject(new String("test"));
        assertEquals("\"test\"", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteObject_Null() throws IOException {
        generatorBase.writeObject(null);
        assertNull(byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        generatorBase.writeStartArray();
        assertEquals("[", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        generatorBase.writeStartArray();
        generatorBase.writeString("test");
        generatorBase.writeEndArray();
        assertEquals("[\"test\"]", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        generatorBase.writeStartObject();
        generatorBase.writeFieldName("test");
        generatorBase.writeString("value");
        generatorBase.writeEndObject();
        assertEquals("{\"test\":\"value\"}", byteArrayOutputStream.toString());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generatorBase.useDefaultPrettyPrinter();
        generatorBase.writeStartObject();
        generatorBase.writeFieldName("test");
        generatorBase.writeString("value");
        generatorBase.writeEndObject();
        assertEquals(new DefaultPrettyPrinter().printValueAsString(new Object[]{new String("test"), "value"}), byteArrayOutputStream.toString());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec testObjectCodec = mock(ObjectCodec.class);
        generatorBase.setCodec(testObjectCodec);
        assertEquals(testObjectCodec, generatorBase.getCodec());
    }

    @Test
    public void testVersion() throws Exception {
        Version expectedVersion = new Version("2.11.3", null);
        when(objectCodec.version()).thenReturn(expectedVersion);
        assertEquals(expectedVersion, generatorBase.version());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBinary_UnsupportedOperation() throws IOException {
        generatorBase.writeBinary(JsonGenerator.Base64Variant.STANDARD, mock(InputStream.class), 10);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteObject_JsonMappingException() throws IOException, JsonMappingException {
        generatorBase.writeObject(new Object(){});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCodec_Null() throws IOException {
        generatorBase.setCodec(null);
    }

    @Test
    public void testClose() throws IOException {
        generatorBase.writeString("test");
        generatorBase.close();
        assertFalse(generatorBase.isClosed());
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }
}