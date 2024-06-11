import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 JsonParser parser = new ObjectMapper().reader().forType(Integer.class);
 Integer deserialized = parser.readValueAs(Integer.class, json);
 assertEquals(value, deserialized);

 // Edge case: minimum and maximum values
 Integer minValue = Integer.MIN_VALUE;
 Integer maxValue = Integer.MAX_VALUE;
 ObjectWriter writerMin = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonMin = writerMin.writeValueAsString(minValue);
 JsonParser parserMin = new ObjectMapper().reader().forType(Integer.class);
 Integer deserializedMin = parserMin.readValueAs(Integer.class, jsonMin);
 assertEquals(minValue, deserializedMin);

 ObjectWriter writerMax = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonMax = writerMax.writeValueAsString(maxValue);
 JsonParser parserMax = new ObjectMapper().reader().forType(Integer.class);
 Integer deserializedMax = parserMax.readValueAs(Integer.class, jsonMax);
 assertEquals(maxValue, deserializedMax);

 // Error scenario: non-number value
 ObjectMapper mapper = new ObjectMapper();
 assertThrows(JsonProcessingException.class, () -> mapper.readValue("{\"name\":123}", Integer.class));
 }

 @Test
 public void testIntegerSerializerWithContextual() throws JsonProcessingException {
 Integer value = 123;
 ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 JsonParser parser = new ObjectMapper().reader().forType(Integer.class);
 Integer deserialized = parser.readValueAs(Integer.class, json);
 assertEquals(value, deserialized);

 // Test contextual serialization with @JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING, numberFormat = "integer")
 class MyClass {
 private Integer number;

 public Integer getNumber() {
 return number;
 }

 public void setNumber(Integer number) {
 this.number = number;
 }
 }
 MyClass myObject = new MyClass();
 myObject.setNumber(123);
 ObjectWriter writerMyObject = new ObjectMapper().writer().withDefaultPrettyPrinter();
 String jsonMyObject = writerMyObject.writeValueAsString(myObject);
 ObjectMapper parserMyObject = new ObjectMapper();
 MyClass deserializedMyObject = parserMyObject.readValue(JsonNodeClass.class, jsonMyObject);
 assertEquals(myObject.getNumber(), deserializedMyObject.getNumber());
 }

 // ... test other serializers in a similar way
}