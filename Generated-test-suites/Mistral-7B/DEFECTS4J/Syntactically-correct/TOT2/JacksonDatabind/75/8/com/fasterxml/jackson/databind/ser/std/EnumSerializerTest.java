import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;
 private EnumValues enumValues;

 @Before
 public void setup() {
 enumValues = new EnumValues(MyEnum.class);
 serializer = new EnumSerializer(enumValues, false);
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectNode actual = mapper.writeValueAsTree(enumValue);
 String expected = MyEnum.VALUE_ONE.name();
 assertEquals(expected, actual.get(0).asText());
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectMapper mapperWithIndexEnabled = new ObjectMapper();
 mapperWithIndexEnabled.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
 ObjectNode actual = mapperWithIndexEnabled.writeValueAsTree(enumValue);
 int expected = enumValue.ordinal();
 assertEquals(expected, actual.getInt(0));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), MyEnum.class.asType());
 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.expectStringFormat(MyEnum.class.asType())).thenReturn(null);
 serializer.acceptJsonFormatVisitor(visitor, MyEnum.class.asType());
 verify(visitor, times(1)).writeString("VALUE_ONE");
 verify(visitor, times(1)).writeString("VALUE_TWO");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedSerializationShape() throws JsonMappingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.BOOLEAN);
 SerializerProvider serializers = mock(SerializerProvider.class);
 when(serializers.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(true);
 serializer = new EnumSerializer(enumValues, null);
 serializer.createContextual(null, null);
 serializer.acceptJsonFormatVisitor(null, MyEnum.class.asType());
 }
}

enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 VALUE_ONE,
 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 VALUE_TWO
}