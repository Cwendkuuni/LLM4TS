import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 // Given
 Date date = new Date();
 when(mockSerializerProvider.defaultSerializeDateKey(date, mockJsonGenerator)).thenCallRealMethod();

 // When
 serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(anyString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 // Given
 String value = "test";

 // When
 serializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockJsonGenerator, times(1)).writeFieldName(value);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // When
 JsonNode schema = serializer.getSchema(mockSerializerProvider, Object.class);

 // Then
 ObjectNode expectSchema = new ObjectMapper().createObjectNode().put("type", "string");
 assertEquals(expectSchema, schema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 // Given
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = new JavaType(String.class);

 // When
 serializer.acceptJsonFormatVisitor(visitor, typeHint);

 // Then
 verify(visitor, times(1)).expectStringFormat(typeHint);
 }
}