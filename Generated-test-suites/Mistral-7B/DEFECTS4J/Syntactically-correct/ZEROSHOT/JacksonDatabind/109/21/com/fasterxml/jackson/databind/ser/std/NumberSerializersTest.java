import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerialization() throws IOException {
 Integer number = 123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 assertEquals(123, parser.readValueAs(Integer.class));
 }

 @Test
 public void testIntegerDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("123".getBytes()));
 assertEquals(123, parser.readValueAs(Integer.class));
 }

 @Test
 public void testLongSerialization() throws IOException {
 Long number = 456L;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 assertEquals(456, parser.readValueAs(Long.class));
 }

 @Test
 public void testLongDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("456".getBytes()));
 assertEquals(456, parser.readValueAs(Long.class));
 }

 @Test
 public void testShortSerialization() throws IOException {
 Short number = (short) 789;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 assertEquals(789, parser.readValueAs(Short.class));
 }

 @Test
 public void testShortDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("789".getBytes()));
 assertEquals(789, parser.readValueAs(Short.class));
 }

 @Test
 public void testFloatSerialization() throws IOException {
 Float number = 3.14F;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 assertEquals(3.14F, parser.readValueAs(Float.class));
 }

 @Test
 public void testFloatDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("3.14".getBytes()));
 assertEquals(3.14F, parser.readValueAs(Float.class));
 }

 @Test
 public void testDoubleSerialization() throws IOException {
 Double number = 1.23;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 assertEquals(1.23, parser.readValueAs(Double.class));
 }

 @Test
 public void testDoubleDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("1.23".getBytes()));
 assertEquals(1.23, parser.readValueAs(Double.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidNumberDeserialization() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("abc".getBytes()));
 }
}