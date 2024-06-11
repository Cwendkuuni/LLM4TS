package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.VersionUtil;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeneratorBaseTest {

 // Test constructor
 @Test
 public void testConstructor() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 assertEquals(GeneratorBase.DERIVED_FEATURES_MASK, gb.getFeatureMask());
 }

 // Test getCurrentValue and setCurrentValue
 @Test
 public void testGetAndSetCurrentValue() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_Features_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 Object obj = new Object();
 gb.setCurrentValue(obj);
 assertEquals(obj, gb.getCurrentValue());
 }

 // Test isEnabled and setFeatureMask
 @Test
 public void testIsEnabledAndSetFeatureMask() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 assertTrue(gb.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 gb.setFeatureMask(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.WRITE_NULL_AS_NULL.getMask());
 assertFalse(gb.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 assertTrue(gb.isEnabled(GeneratorBase.Feature.WRITE_NULL_AS_NULL));
 }

 // Test writeFieldName
 @Test
 public void testWriteFieldName() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeFieldName("test");
 }

 // Test writeNumber
 @Test
 public void testWriteNumber() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeNumber(123);
 gb.writeNumber(-456);
 gb.writeNumber(123.45F);
 gb.writeNumber(123.45);
 gb.writeNumber(new BigDecimal("123.45"));
 }

 // Test writeBoolean
 @Test
 public void testWriteBoolean() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeBoolean(true);
 gb.writeBoolean(false);
 }

 // Test writeNull
 @Test
 public void testWriteNull() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeNull();
 }

 // Test writeString
 @Test
 public void testWriteString() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeString("test");
 }

 // Test writeRawValue
 @Test
 public void testWriteRawValue() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeRawValue("test");
 }

 // Test writeRawValue with offset and length
 @Test
 public void testWriteRawValueWithOffsetAndLength() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 String test = "test";
 gb.writeRawValue(test.substring(1), 0, test.length()-1);
 }

 // Test writeRawValue with byte array
 @Test
 public void testWriteRawValueWithByteArray() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 String test = "test";
 gb.writeRawValue(test.getBytes());
 }

 // Test writeRawValue with SerializableString
 @Test
 public void testWriteRawValueWithSerializableString() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 SerializableString ss = new SerializableString("test");
 gb.writeRawValue(ss);
 }

 // Test writeRawValue with char array
 @Test
 public void testWriteRawValueWithCharArray() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 char[] charArray = {'t', 'e', 's', 't'};
 gb.writeRawValue(charArray);
 }

 // Test writeObject
 @Test
 public void testWriteObject() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeObject(new Object());
 }

 // Test writeTree
 @Test
 public void testWriteTree() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeTree(new Object());
 }

 // Test flush
 @Test
 public void testFlush() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 gb.setOutput(baos);
 gb.writeString("test");
 gb.flush();
 assertEquals("test", baos.toString());
 }

 // Test close
 @Test
 public void testClose() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 gb.setOutput(baos);
 gb.writeString("test");
 gb.close();
 assertFalse(gb.isClosed());
 }

 // Test writeStartArray and writeEndArray
 @Test
 public void testWriteStartAndEndArray() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeStartArray();
 gb.writeNumber(1);
 gb.writeString("test");
 gb.writeEndArray();
 }

 // Test writeStartObject and writeEndObject
 @Test
 public void testWriteStartAndEndObject() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeStartObject();
 gb.writeFieldName("test");
 gb.writeNumber(1);
 gb.writeEndObject();
 }

 // Test writeBinary with Base64Variant
 @Test
 public void testWriteBinaryWithBase64Variant() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 ByteArrayInputStream bais = new ByteArrayInputStream("RnJvbSBvZiB1bSBzdHlsZSBxdWVyeSBwcm9ibGVt".getBytes());
 gb.writeBinary(JsonGenerator.Base64Variant.STANDARD, bais, bais.available());
 }

 // Test writeBinary with null data
 @Test(expected = JsonGenerationException.class)
 public void testWriteBinaryWithNullData() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb.writeBinary(JsonGenerator.Base64Variant.STANDARD, null, 0);
 }

 // Test writeBinary with negative length
 @Test(expected = IOException.class)
 public void testWriteBinaryWithNegativeLength() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 ByteArrayInputStream bais = new ByteArrayInputStream("RnJvbSBvZiB1bSBzdHlsZSBxdWVyeSBwcm9ibGVt".getBytes());
 gb.writeBinary(JsonGenerator.Base64Variant.STANDARD, bais, -1);
 }

 // Test _verifyValueWrite with illegal value
 @Test(expected = JsonGenerationException.class)
 public void testVerifyValueWriteWithIllegalValue() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb._verifyValueWrite("write illegal value");
 }

 // Test _verifyValueWrite with null value
 @Test(expected = JsonProcessingException.class)
 public void testVerifyValueWriteWithNullValue() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 gb._verifyValueWrite("write null value");
 }

 // Test _decodeSurrogate
 @Test
 public void testDecodeSurrogate() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 int i = gb._decodeSurrogate('\uD801', '\uDC00');
 assertEquals(0x1037F, i);
 }

 // Test _decodeSurrogate with invalid second surrogate
 @Test(expected = JsonProcessingException.class)
 public void testDecodeSurrogateWithInvalidSecondSurrogate() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 int i = gb._decodeSurrogate('\uD801', '\uDEAD');
 }

 // Test _releaseBuffers
 @Test
 public void testReleaseBuffers() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory()) {
 @Override
 protected void _releaseBuffers() throws IOException {
 // no-op
 }
 };
 gb.writeString("test");
 gb.close();
 }

 // Test version
 @Test
 public void testVersion() throws IOException {
 GeneratorBase gb = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, new ObjectMapper().getDefaultInstance().getFactory());
 Version version = gb.version();
 Version expectedVersion = new Version("2.10.3", null);
 assertEquals(expectedVersion.toString(), version.toString());
 }
}