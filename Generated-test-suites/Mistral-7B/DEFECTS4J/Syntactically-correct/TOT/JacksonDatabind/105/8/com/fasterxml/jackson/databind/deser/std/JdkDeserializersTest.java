import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JdkDeserializersTest {

 @BeforeAll
 static void setup() {
 // You can initialize any shared state here if needed
 }

 @AfterAll
 static void teardown() {
 // You can clean up any shared state here if needed
 }

 @ParameterizedTest
 @MethodSource("provideDataForFindMethodTest")
 void testFindMethod(Class<?> rawType, String clsName, JsonDeserializer<?> expectedDeserializer) throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertEquals(expectedDeserializer, deserializer);
 }

 @Test
 void testFindMethod_UnknownClass() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "UnknownClass");
 assertNull(deserializer);
 }

 @Test
 void testUuidDeserializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = objectMapper.createObjectNode();
 node.put("uuid", "00000000-0000-0000-0000-000000000000");
 InputStream inputStream = new ByteArrayInputStream(node.toString().getBytes());
 Type type = new TypeReference<UUID>() {}.getType();
 UUID uuid = objectMapper.readValue(inputStream, type);

 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer");
 UUID deserializedUuid = objectMapper.readValue(inputStream, UUID.class, deserializer);
 assertEquals(uuid, deserializedUuid);
 }

 @Test
 void testAtomicBooleanDeserializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode node = objectMapper.createObjectNode();
 node.put("bool", false);
 InputStream inputStream = new ByteArrayInputStream(node.toString().getBytes());
 Type type = new TypeReference<AtomicBoolean>() {}.getType();
 AtomicBoolean atomicBoolean = new AtomicBoolean(true);
 AtomicBoolean deserializedAtomicBoolean = objectMapper.readValue(inputStream, type);
 assertTrue(deserializedAtomicBoolean.get());
 assertEquals(atomicBoolean, deserializedAtomicBoolean);

 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, "com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer");
 AtomicBoolean deserializedAtomicBooleanViaDeserializer = objectMapper.readValue(inputStream, AtomicBoolean.class, deserializer);
 assertTrue(deserializedAtomicBooleanViaDeserializer.get());
 assertEquals(atomicBoolean, deserializedAtomicBooleanViaDeserializer);
 }

 @Test
 void testByteBufferDeserializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 byte[] byteArray = new byte[1024];
 Arrays.fill(byteArray, (byte) 0x01);
 ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("byteBuffer", byteBuffer.array());
 InputStream inputStream = new ByteArrayInputStream(node.toString().getBytes());
 Type type = new TypeReference<ByteBuffer>() {}.getType();
 ByteBuffer byteBufferFromJson = objectMapper.readValue(inputStream, type);
 assertArrayEquals(byteArray, byteBufferFromJson.array());

 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, "com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer");
 ByteBuffer deserializedByteBuffer = objectMapper.readValue(inputStream, ByteBuffer.class, deserializer);
 assertArrayEquals(byteArray, deserializedByteBuffer.array());
 }

 private static Stream<Arguments> provideDataForFindMethodTest() {
 return Stream.of(
 Arguments.arguments(UUID.class, "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer", new UUIDDeserializer()),
 Arguments.arguments(AtomicBoolean.class, "com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer", new AtomicBooleanDeserializer()),
 Arguments.arguments(ByteBuffer.class, "com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer", new ByteBufferDeserializer())
 );
 }
}