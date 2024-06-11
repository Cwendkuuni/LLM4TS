package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;
 private TypeFactory typeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 objectMapper = JsonMapper.builder().build();
 typeFactory = objectMapper.getTypeFactory();
 }

 @Test
 public void testFindNull() throws Exception {
 Class<?> nullClass = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindString() throws Exception {
 Class<?> stringClass = String.class;
 String clsName = String.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindUuid() throws Exception {
 Class<?> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertEquals(UUIDDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindAtomicBoolean() throws Exception {
 Class<?> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindStackTraceElement() throws Exception {
 Class<?> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindByteBuffer() throws Exception {
 Class<?> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindUnknownClass() throws Exception {
 Class<?> unknownClass = Object.class;
 String clsName = String.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(unknownClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindFromStringDeserializer() throws Exception {
 Class<?> stringClass = String.class;
 String clsName = String.class.getName();
 JsonDeserializer<?> stringDeserializer = new StringDeserializer();
 JdkDeserializers.find(stringClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertEquals(StringDeserializer.class, deserializer.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindAtomicBooleanDeserializer() throws Exception {
 Class<?> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());

 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .constructValueType(null, new TypeReference<List<AtomicBoolean>>() {});
 JsonDeserializer<List<AtomicBoolean>> listDeserializer = mapper.findValueDeserializer(defaultTypingResolver);
 assertEquals(ListDeserializer.with(new AtomicBooleanDeserializer(), null), listDeserializer);
 }

 @Test
 public void testFindByteBufferDeserializer() throws Exception {
 Class<?> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());

 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .constructValueType(null, new TypeReference<List<ByteBuffer>>() {});
 JsonDeserializer<List<ByteBuffer>> listDeserializer = mapper.findValueDeserializer(defaultTypingResolver);
 assertEquals(ListDeserializer.with(new ByteBufferDeserializer(), null), listDeserializer);
 }

 @Test
 public void testFindUuidDeserializer() throws Exception {
 Class<?> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertEquals(UUIDDeserializer.class, deserializer.getClass());

 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .constructValueType(null, new TypeReference<List<UUID>>() {});
 JsonDeserializer<List<UUID>> listDeserializer = mapper.findValueDeserializer(defaultTypingResolver);
 assertEquals(ListDeserializer.with(new UUIDDeserializer(), null), listDeserializer);
 }

 @Test
 public void testFindStackTraceElementDeserializer() throws Exception {
 Class<?> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());

 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapper.getTypeFactory()
 .constructValueType(null, new TypeReference<List<StackTraceElement>>() {});
 JsonDeserializer<List<StackTraceElement>> listDeserializer = mapper.findValueDeserializer(defaultTypingResolver);
 assertEquals(ListDeserializer.with(new StackTraceElementDeserializer(), null), listDeserializer);
 }

 @Test
 public void testFindDeserializerWithFromString() throws Exception {
 Class<?> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 String jsonString = "\"123e4567-89ab-cdef-1234-567890abcdef\"";
 Object uuid = objectMapper.readValue(jsonString, uuidClass);
 assertEquals(UUID.fromString(jsonString), uuid);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<UUID> uuidDeserializer = new UUIDDeserializer();
 deserializer = JdkDeserializers.find(uuidClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertEquals(uuidDeserializer, deserializer);
 }

 @Test
 public void testFindDeserializerWithFromStringNull() throws Exception {
 Class<?> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 String jsonString = null;
 Object uuid = objectMapper.readValue(jsonString, uuidClass);
 assertNull(uuid);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<UUID> uuidDeserializer = new UUIDDeserializer();
 deserializer = JdkDeserializers.find(uuidClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithByteBuffer() throws Exception {
 Class<?> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 ByteBuffer byteBuffer = ByteBuffer.allocate(10);
 byteBuffer.put("Hello World!".getBytes());
 byteBuffer.flip();
 byteBuffer.mark();

 String jsonString = "{\"byteBuffer\":";
 jsonString += byteBuffer.array();
 jsonString += "}";
 Object byteBufferObj = objectMapper.readValue(jsonString, byteBufferClass);
 byteBuffer.position(byteBuffer.limit());
 byteBuffer.reset();
 assertEquals(byteBuffer, byteBufferObj);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<ByteBuffer> byteBufferDeserializer = new ByteBufferDeserializer();
 deserializer = JdkDeserializers.find(byteBufferClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertEquals(byteBufferDeserializer, deserializer);
 }

 @Test
 public void testFindDeserializerWithByteBufferNull() throws Exception {
 Class<?> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 ByteBuffer byteBuffer = null;
 String jsonString = "{\"byteBuffer\":";
 jsonString += byteBuffer.array();
 jsonString += "}";
 Object byteBufferObj = objectMapper.readValue(jsonString, byteBufferClass);
 assertNull(byteBufferObj);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<ByteBuffer> byteBufferDeserializer = new ByteBufferDeserializer();
 deserializer = JdkDeserializers.find(byteBufferClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerWithAtomicBoolean() throws Exception {
 Class<?> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 boolean value = true;
 AtomicBoolean atomicBoolean = new AtomicBoolean(value);
 String jsonString = "{\"atomicBoolean\":";
 jsonString += value;
 jsonString += "}";
 Object atomicBooleanObj = objectMapper.readValue(jsonString, atomicBooleanClass);
 assertEquals(value, atomicBooleanObj);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<AtomicBoolean> atomicBooleanDeserializer = new AtomicBooleanDeserializer();
 deserializer = JdkDeserializers.find(atomicBooleanClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertEquals(atomicBooleanDeserializer, deserializer);
 }

 @Test
 public void testFindDeserializerWithAtomicBooleanNull() throws Exception {
 Class<?> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 AtomicBoolean atomicBoolean = null;
 String jsonString = "{\"atomicBoolean\":null}";
 Object atomicBooleanObj = objectMapper.readValue(jsonString, atomicBooleanClass);
 assertNull(atomicBooleanObj);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNull(deserializer);

 JsonDeserializer<AtomicBoolean> atomicBooleanDeserializer = new AtomicBooleanDeserializer();
 deserializer = JdkDeserializers.find(atomicBooleanClass, clsName, new DeserializationContext(objectMapper, typeFactory));
 assertNull(deserializer);
 }
}