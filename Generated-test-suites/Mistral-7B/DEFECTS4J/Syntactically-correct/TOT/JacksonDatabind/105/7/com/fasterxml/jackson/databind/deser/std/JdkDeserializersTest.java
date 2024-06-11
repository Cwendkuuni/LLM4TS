import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFind_UUID() {
 UUID expected = new UUID(1L, 2L);
 String jsonString = "\"1-2\""; // UUID as String representation
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 UUID result;
 try {
 result = objectMapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), deserializer);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertSame(expected, result);
 }

 @Test
 public void testFind_AtomicBoolean() {
 AtomicBoolean atomicBooleanExpected = new AtomicBoolean(true);
 String jsonString = "true";
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 AtomicBoolean atomicBooleanResult;
 try {
 atomicBooleanResult = objectMapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), deserializer);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertSame(atomicBooleanExpected, atomicBooleanResult);
 }

 @Test
 public void testFind_StackTraceElement() {
 StackTraceElement stackTraceElementExpected = new StackTraceElement("testClass", "testMethod", "testFile.java", 1);
 String jsonString = "{\"className\":\"testClass\", \"methodName\":\"testMethod\", \"file\":\"testFile.java\", \"lineNumber\":1}";
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 StackTraceElement stackTraceElementResult;
 try {
 stackTraceElementResult = objectMapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), deserializer);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertSame(stackTraceElementExpected, stackTraceElementResult);
 }

 @Test
 public void testFind_ByteBuffer() {
 ByteBuffer expected = ByteBuffer.allocate(4).putInt(1).order(ByteOrder.LITTLE_ENDIAN);
 String jsonString = "B0 01 00 00"; // ByteBuffer as hexadecimal string representation
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 ByteBuffer byteBufferResult;
 try {
 byteBufferResult = objectMapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), deserializer);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertSame(expected, byteBufferResult);
 }

 @Test
 public void testFind_UnknownClass() {
 String unknownClassName = "com.example.UnknownClass";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, unknownClassName);
 assertNull(deserializer);
 }

 @Test
 public void testFind_NullClass() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @Test
 public void testFind_NullClassName() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, null);
 assertNull(deserializer);
 }

 @Test
 public void testFind_EmptyClassName() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "");
 assertNull(deserializer);
 }

 @Test
 public void testFromStringDeserializer_nullRawType() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, UUID.class.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFromStringDeserializer_nonStringType() {
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, UUID.class.getName());
 assertNull(deserializer);
 }
}