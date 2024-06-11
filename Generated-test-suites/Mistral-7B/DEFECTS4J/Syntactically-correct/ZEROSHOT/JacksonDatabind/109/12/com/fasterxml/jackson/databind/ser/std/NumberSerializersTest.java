import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testNumberSerializers() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test Integer serialization
 Integer intValue = 123;
 JsonNode intNode = mapper.valueToTree(intValue);
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 mapper.writeValue(gen, intValue);
 String intString = intNode.asText();
 assertEquals("123", intString);

 // Test Integer serialization with type information
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 JsonNode intNodeWithType = mapper.valueToTree(intValue);
 intNodeWithType = mapper.treeModel().addTypeId(intNodeWithType, typeSerializer.serialize(intValue.getClass()));
 JsonNode intNodeWithTypeSerialized = mapper.writeValueAsTree(intValue);
 assertEquals(intNodeWithType, intNodeWithTypeSerialized);

 // Test Long serialization
 Long longValue = 456L;
 JsonNode longNode = mapper.valueToTree(longValue);
 mapper.writeValue(gen, longValue);
 String longString = longNode.asText();
 assertEquals("456", longString);

 // Test Double serialization
 Double doubleValue = 789.123;
 JsonNode doubleNode = mapper.valueToTree(doubleValue);
 mapper.writeValue(gen, doubleValue);
 String doubleString = doubleNode.asText();
 assertEquals("789.123", doubleString);

 // Test JsonFormat annotation
 @JsonFormat(shape=JsonFormat.Shape.STRING, numberFormat="0.##")
 int jsonFormatInt = 123;
 intJsonFormatNode = mapper.valueToTree(jsonFormatInt);
 mapper.writeValue(gen, jsonFormatInt);
 String jsonFormatIntString = intJsonFormatNode.asText();
 assertEquals("123", jsonFormatIntString);
 }
}