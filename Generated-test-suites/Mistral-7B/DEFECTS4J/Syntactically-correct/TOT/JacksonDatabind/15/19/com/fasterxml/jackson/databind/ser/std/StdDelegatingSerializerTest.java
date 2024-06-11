import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StdDelegatingSerializerTest {

 private static final Converter<Object, Object> CONVERTER = new Converter.StdConverter<Object, Object>() {
 @Override
 public Object convert(Object source) {
 return source.toString();
 }
 };

 @Test
 public void testStringToIntegerSerializer() throws JsonGenerationException {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer<String> stringToIntegerSerializer =
 new StdDelegatingSerializer<>(Converter.stringToInt,
 mapper.getTypeFactory().constructType(Integer.class),
 mapper.findValueSerializer(Integer.class));

 String input = "42";
 Object output;
 try {
 output = stringToIntegerSerializer.serializeWithType(input, mapper.getGenerator(), mapper, mapper.getTypeSerializer());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(42, output);
 }

 @Test
 public void testNullValue() {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer<String> stringToIntegerSerializer =
 new StdDelegatingSerializer<>(Converter.stringToInt,
 mapper.getTypeFactory().constructType(Integer.class),
 mapper.findValueSerializer(Integer.class));

 String input = null;
 Object output;
 try {
 output = stringToIntegerSerializer.serializeWithType(input, mapper.getGenerator(), mapper, mapper.getTypeSerializer());
 } catch (IOException e) {
 // Expected
 }
 assertEquals(null, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameTypes() {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer<String> stringToIntegerSerializer =
 new StdDelegatingSerializer<>(Converter.stringToInt,
 mapper.getTypeFactory().constructType(String.class),
 mapper.findValueSerializer(String.class));

 String input = "42";
 Object output;
 try {
 output = stringToIntegerSerializer.serializeWithType(input, mapper.getGenerator(), mapper, mapper.getTypeSerializer());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }
}