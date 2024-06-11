import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class StdKeySerializerTest {

 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private StdKeySerializer serializerUnderTest;

 @Before
 public void setUp() {
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 serializerUnderTest = new StdKeySerializer();
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 serializerUnderTest.serialize(date, mockJsonGenerator, mockSerializerProvider);

 verify(mockJsonGenerator).writeFieldName( date.toISO8601String() );
 }

 @Test
 public void testSerializeString() throws IOException {
 String str = "test";
 serializerUnderTest.serialize(str, mockJsonGenerator, mockSerializerProvider);

 verify(mockJsonGenerator).writeFieldName(str);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode schema = serializerUnderTest.getSchema(mockSerializerProvider, StdSerializer.Nulls.NO_CLASS);

 assertNotNull(schema);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = new JavaType(Map.class, String.class, IntTypeFactory.defaultInstance());

 serializerUnderTest.acceptJsonFormatVisitor(visitor, typeHint);

 verify(visitor).expectStringFormat(typeHint);
 }
}