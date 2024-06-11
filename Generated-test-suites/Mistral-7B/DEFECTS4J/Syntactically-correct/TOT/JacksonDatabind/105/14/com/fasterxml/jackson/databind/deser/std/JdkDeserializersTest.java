import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testUUIDDeserializer() {
 String json = "\"some-uuid\"";
 Object result = JdkDeserializers.find(UUID.class, "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(json.getBytes()), new TypeReference<UUID>() {});
 assertEquals("some-uuid", result.toString());
 }

 @Test
 public void testFromStringDeserializer() {
 String json = "\"FromStringDeserializer ClassName: com.fasterxml.jackson.databind.deser.std.SomeClass\"";
 Object result = JdkDeserializers.find(String.class, "com.fasterxml.jackson.databind.deser.std.FromStringDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(json.getBytes()), new TypeReference<String>() {});
 assertEquals("com.fasterxml.jackson.databind.deser.std.SomeClass", result);
 }

 @Test
 public void testStackTraceElementDeserializer() {
 String json = "{" +
 " \"className\": \"com.example.MyClass\"," +
 " \"methodName\": \"myMethod\"," +
 " \"file\": \"/path/to/file.java\"," +
 " \"lineNumber\": 42," +
 " \"columnNumber\": 5" +
 "}";
 StackTraceElement expected = new StackTraceElement("com.example.MyClass", "myMethod", "/path/to/file.java", 42, 5);
 StackTraceElement result = (StackTraceElement) JdkDeserializers.find(StackTraceElement.class, "com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(json.getBytes()), new TypeReference<StackTraceElement>() {});
 assertEquals(expected.getClassName(), result.getClassName());
 assertEquals(expected.getMethodName(), result.getMethodName());
 assertEquals(expected.getFileName(), result.getFileName());
 assertEquals(expected.getLineNumber(), result.getLineNumber());
 assertEquals(expected.getColumnNumber(), result.getColumnNumber());
 }

 @Test
 public void testAtomicBooleanDeserializer() {
 ObjectMapper mapper = new ObjectMapper();
 String jsonTrue = "true";
 Object jsonTrueObj = mapper.readValue(new ByteArrayInputStream(jsonTrue.getBytes()), JsonValue.class);
 Object resultTrue = JdkDeserializers.find(AtomicBoolean.class, "com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(jsonTrue.getBytes().clone()), new TypeReference<AtomicBoolean>() {});
 AtomicBoolean expectedTrue = new AtomicBoolean(true);
 assertTrue(resultTrue instanceof AtomicBoolean);
 assertEquals(expectedTrue, resultTrue);

 String jsonFalse = "false";
 Object jsonFalseObj = mapper.readValue(new ByteArrayInputStream(jsonFalse.getBytes()), JsonValue.class);
 Object resultFalse = JdkDeserializers.find(AtomicBoolean.class, "com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(jsonFalse.getBytes().clone()), new TypeReference<AtomicBoolean>() {});
 AtomicBoolean expectedFalse = new AtomicBoolean(false);
 assertTrue(resultFalse instanceof AtomicBoolean);
 assertEquals(expectedFalse, resultFalse);
 }

 @Test
 public void testByteBufferDeserializer() {
 String json = "\"01-02-03-04-05-06-07-08-09-0a-0b-0c-0d-0e-0f-10-11-12-13-14-15-16-17-18-19-1a-1b-1c-1d-1e-1f-20-21-22-23-24-25-26-27-28-29-2a-2b-2c-2d-2e-2f\"";
 byte[] bytes = json.getBytes();
 ByteBuffer expected = ByteBuffer.wrap(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f });
 ByteBuffer result = (ByteBuffer) JdkDeserializers.find(ByteBuffer.class, "com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer")
 .deserialize(mapper, new ByteArrayInputStream(bytes), new TypeReference<ByteBuffer>() {});
 byte[] resultBytes = result.array();
 assertArrayEquals(expected.array(), resultBytes);
 }

 @Test
 public void testFindDeserializerForUnknownType() {
 Class<?> rawType = new Object().getClass();
 String clsName = "com.example.UnknownClass";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }
}