import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdKeySerializerTest {

 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private StdKeySerializer stdKeySerializer;

 @Before
 public void setup() {
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 stdKeySerializer = new StdKeySerializer();
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 when(mockSerializerProvider.defaultSerializeDateKey(date, mockJsonGenerator)).thenCallRealMethod();
 stdKeySerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);
 verify(mockJsonGenerator, times(1)).writeFieldName(getValueFromJsonGenerator(mockJsonGenerator));
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "test";
 stdKeySerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
 verify(mockJsonGenerator, times(1)).writeFieldName(value);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonSchema jsonSchema = stdKeySerializer.getSchema(mockSerializerProvider, Object.class);
 Assert.assertEquals("string", jsonSchema.getType());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 stdKeySerializer.acceptJsonFormatVisitor(visitor, Object.class);
 verify(visitor, times(1)).expectStringFormat(Object.class);
 }

 private String getValueFromJsonGenerator(JsonGenerator jsonGenerator) throws IOException {
 JsonParser parser = new JsonFactory().createParser(new StringWriter());
 parser.setCodec(new ObjectMapper());
 parser.setCurrentValue(jsonGenerator.getOutputValue());
 return parser.getValueAsString();
}

}