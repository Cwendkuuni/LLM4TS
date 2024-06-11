import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private static final ObjectMapper om = new ObjectMapper();

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(parser, new TestContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser("[\"" + "test" + "\"]");
 String[] result = deserializer.deserialize(parser, new TestContext());
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test
 public void testDeserializeArrayWithNullValue() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser("[\"" + "test" + "\", null]");
 String[] result = deserializer.deserialize(parser, new TestContext());
 assertArrayEquals(new String[]{"test", null}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new MyStringDeserializer());
 JsonParser parser = om.getFactory().createParser("[\"" + "test1" + "\", \"" + "test2" + "\"]");
 String[] result = deserializer.deserialize(parser, new TestContext());
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser("{ \"name\": \"John\" }");
 String[] result = deserializer.deserialize(parser, new TestContext());
 fail("Expected IOException but got " + result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullInput() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser(null);
 String[] result = deserializer.deserialize(parser, new TestContext());
 fail("Expected IOException but got " + result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyInput() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser parser = om.getFactory().createParser("");
 String[] result = deserializer.deserialize(parser, new TestContext());
 fail("Expected IOException but got " + result.getClass().getName());
 }

 @Test
 public void testDeserializeCustomContentConverter() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 MyStringContentConverter converter = new MyStringContentConverter();
 JsonParser parser = om.getFactory().createParser("[\"[1,2]\", \"[3,4]\"]");
 ObjectReader reader = om.readerForUpdating(parser);
 reader.setContentConverter(converter);
 JsonDeserializer<String[]> deser = reader.readValueAs(String[].class);
 String[] result = deserializer.deserialize(parser, new TestContext());
 MyStringContentConverter.assertConverted(converter, 1, 2, 3, 4);
 assertArrayEquals(new String[]{"[1,2]", "[3,4]"}, result);
 }

 @Test
 public void testCreateContextual() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 BeanProperty property = new BeanProperty("testProperty", String[].class);
 JsonDeserializer<?> resultDeserializer = deserializer.createContextual(new TestContext(), property);
 assertTrue(resultDeserializer instanceof StringArrayDeserializer);
 }

}

class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leaseObjectBuffer() {
 throw new UnsupportedOperationException();
 }
}

class MyStringContentConverter extends JsonDeserializer.BaseJsonDeserializer<Object> {
 private int firstValue;
 private int secondValue;

 static void assertConverted(MyStringContentConverter converter, int firstValue, int secondValue) {
 assertEquals(new Integer[] { firstValue, secondValue }, converter.deserialize(null, new TestContext()));
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String[] strings = ctxt.readValueAs(String[].class, p);
 firstValue = Integer.parseInt(strings[0].substring(1, strings[0].length() - 1).replaceAll("\\]", ""));
 secondValue = Integer.parseInt(strings[1].substring(1, strings[1].length() - 1).replaceAll("\\]", ""));
 return new Object[] { firstValue, secondValue };
 }
}