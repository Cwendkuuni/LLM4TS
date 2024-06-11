import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.IOContext;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

 private JsonGenerator jsonGenerator;
 private CharacterEscapes characterEscapes;
 private IOContext ioContext;

 @Before
 public void setup() {
 ioContext = new IOContext();
 characterEscapes = new CharacterEscapes();
 JsonFactory factory = new JsonFactory();
 jsonGenerator = factory.createGenerator(ioContext);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 }

 @Test
 public void testJsonGeneratorImplConstruction() throws IOException {
 assertNotNull(jsonGenerator);
 assertEquals(Version.UNKNOWN, jsonGenerator.version());
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(123);
 assertEquals(123, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetHighestNonEscapedCharNegative() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(-123);
 assertNull(jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes newCharacterEscapes = new CharacterEscapes();
 jsonGenerator.setCharacterEscapes(newCharacterEscapes);
 assertNotNull(jsonGenerator.getCharacterEscapes());
 assertNotSame(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetCharacterEscapesNull() throws IOException {
 jsonGenerator.setCharacterEscapes(null);
 assertNull(jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testWriteStringField() throws IOException {
 String fieldName = "testFieldName";
 String value = "testValue";
 jsonGenerator.writeStringField(fieldName, value);
 assertEquals(fieldName, jsonGenerator.getCurrentName());
 assertEquals(value, jsonGenerator.toString());
 assertNull(jsonGenerator.getCurrentName());
 }

 @Test
 public void testWriteStringFieldNullValue() throws IOException {
 String fieldName = "testFieldName";
 jsonGenerator.writeStringField(fieldName, null);
 assertEquals(fieldName, jsonGenerator.getCurrentName());
 assertNull(jsonGenerator.toString());
 assertNull(jsonGenerator.getCurrentName());
 }

 @Test
 public void testEnableFeatureQuoteFieldNames() throws IOException {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 String fieldName = "testFieldName";
 String value = "testValue";
 jsonGenerator.writeStringField(fieldName, value);
 assertEquals("\"" + fieldName + "\":\"" + value + "\"", jsonGenerator.toString());
 assertNull(jsonGenerator.getCurrentName());
 }

 @Test
 public void testEnableFeatureQuoteFieldNamesOff() throws IOException {
 jsonGenerator.setConfigOverrides(new GeneratorConfigOverride[]{
 new GeneratorConfigOverride(Feature.QUOTE_FIELD_NAMES, false)});
 String fieldName = "testFieldName";
 String value = "testValue";
 jsonGenerator.writeStringField(fieldName, value);
 assertEquals(fieldName + ":", jsonGenerator.toString());
 assertNull(jsonGenerator.getCurrentName());
 }

 @Test
 public void testVersion() throws IOException {
 Version expectedVersion = VersionUtil.versionFor(JsonGeneratorImpl.class);
 assertEquals(expectedVersion, jsonGenerator.version());
 }
}