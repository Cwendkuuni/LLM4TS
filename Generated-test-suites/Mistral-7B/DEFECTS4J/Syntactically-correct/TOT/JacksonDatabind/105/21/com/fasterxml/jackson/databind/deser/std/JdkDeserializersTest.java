import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.fasterxml.jackson.core.version.Version.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdkDeserializersTest {

 @BeforeAll
 static void setup() {
 ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationModule());
 mapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 }

 @Test
 public void testFind_UUID() {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
 UUID expected = UUID.randomUUID();
 String jsonString = mapper.writerWithTypeValidator(new Version(2, 12)).writeValueAsString(expected);
 UUID actual = mapper.readerForUpdating(null).readValue(jsonString);
 assertEquals(expected, actual);
 }

 @Test
 public void testFind_AtomicBoolean() {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 AtomicBoolean expected = new AtomicBoolean(true);
 String jsonString = "true";
 AtomicBoolean actual = mapper.readerForUpdating(null).readValue(jsonString, AtomicBoolean.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFind_StackTraceElement() {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 StackTraceElement expected = new StackTraceElement("JdkDeserializersTest", "testFind_StackTraceElement", "JdkDeserializersTest.java", 1);
 String jsonString = mapper.writerWithTypeValidator(new Version(2, 12)).writeValueAsString(expected);
 StackTraceElement actual = mapper.readerForUpdating(null).readValue(jsonString, StackTraceElement.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFind_ByteBuffer() {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 ByteBuffer expected = ByteBuffer.allocate(10).putInt(1).putInt(2).putInt(3).flip();
 String jsonString = mapper.writerWithTypeValidator(new Version(2, 12)).writeValueAsString(expected);
 ByteBuffer actual = mapper.readerForUpdating(null).readValue(jsonString, ByteBuffer.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFind_UnknownClass() {
 Class<String> stringClass = String.class;
 String clsName = Object.class.getName();
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @AfterAll
 static void tearDown() {
 ObjectMapper mapper = null;
 }

}