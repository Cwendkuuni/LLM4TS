import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockGenerator;
 private SerializerProvider mockProvider;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockGenerator = mock(JsonGenerator.class);
 mockProvider = mock(SerializerProvider.class);
 }

 @Test
 public void testSerializeDate() throws JsonGenerationException, IOException {
 // Given
 Date date = new Date();

 // When
 serializer.serialize(date, mockGenerator, mockProvider);

 // Then
 verify(mockGenerator, times(1)).writeNumber(anyLong(), anyBoolean());
 verifyNoMoreInteractions(mockGenerator);
 }

 @Test
 public void testSerializeString() throws JsonGenerationException, IOException {
 // Given
 String value = "test";

 // When
 serializer.serialize(value, mockGenerator, mockProvider);

 // Then
 verify(mockGenerator, times(1)).writeFieldName(value);
 verifyNoMoreInteractions(mockGenerator);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // Given
 JsonNode expectedSchema = JsonNodeFactory.instance.textNode("string");

 // When
 JsonNode actualSchema = serializer.getSchema(mockProvider, Object.class);

 // Then
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 // Given
 JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);

 // When
 serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);

 // Then
 verify(mockVisitor, times(1)).expectStringFormat(typeHint);
 verifyNoMoreInteractions(mockVisitor);
 }
}