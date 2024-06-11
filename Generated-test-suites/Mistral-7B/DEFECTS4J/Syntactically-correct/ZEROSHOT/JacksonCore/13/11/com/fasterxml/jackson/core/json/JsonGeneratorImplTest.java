import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private IOContext ioContext;

 @Before
 public void setup() {
 ioContext = new IOContext();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 }

 @Test
 public void testDefaultOutputEscapes() throws IOException {
 byte[] actualBytes = new byte[100];
 jsonGenerator.writeStartObject();
 jsonGenerator.writeNumberField("field1", 123);
 jsonGenerator.writeEndObject();
 jsonGenerator.close();
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 CharacterEscapes standardEscapes = jsonGenerator.getCharacterEscapes();
 assertArrayEquals(standardEscapes.getEscapeBytes("field1"), actualBytes, 0, actualBytes.length);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes customEscapes = new StandardCharacterEscapes();
 jsonGenerator.setCharacterEscapes(customEscapes);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeNumberField("field1", 123);
 jsonGenerator.writeEndObject();
 jsonGenerator.close();
 byte[] actualBytes = new byte[100];
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 assertArrayEquals(customEscapes.getEscapeBytes("field1"), actualBytes, 0, actualBytes.length);
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 SerializableString rootValueSeparator = new SerializableString("_");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeNumber(123);
 jsonGenerator.writeEndObject();
 byte[] actualBytes = new byte[100];
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 assertEquals(rootValueSeparator.getValue(), new String(actualBytes, 0, actualBytes.length));
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(255);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeNumberField("field1", 123);
 jsonGenerator.writeEndObject();
 jsonGenerator.close();
 byte[] actualBytes = new byte[100];
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 int maxNonEscapedChar = jsonGenerator.getHighestEscapedChar();
 assertNotEquals(127, maxNonEscapedChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testEnableQuoteFieldNamesFeature() throws IOException {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeNumber(123);
 jsonGenerator.writeEndObject();
 jsonGenerator.close();
 byte[] actualBytes = new byte[100];
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 assertFalse(isQuotedString(actualBytes, 0, 7)); // "field1":
 }

 @Test
 public void testDisableQuoteFieldNamesFeature() throws IOException {
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeNumber(123);
 jsonGenerator.writeEndObject();
 jsonGenerator.close();
 byte[] actualBytes = new byte[100];
 jsonGenerator.getOutputBuffer().read(actualBytes, 0, actualBytes.length);
 assertTrue(isQuotedString(actualBytes, 0, 7)); // "field1":
 }

 private boolean isQuotedString(byte[] bytes, int start, int length) {
 return bytes[start] == '{' && bytes[start + length + 1] == '}';
 }
}