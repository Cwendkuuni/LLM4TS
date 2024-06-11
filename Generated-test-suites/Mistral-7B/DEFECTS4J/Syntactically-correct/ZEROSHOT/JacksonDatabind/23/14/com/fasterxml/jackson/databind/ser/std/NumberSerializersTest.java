package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 private ObjectMapper mapper;

 @BeforeAll
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() throws Exception {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 assertNotNull(NumberSerializers.class.getMethod("addAll", Map.class));
 NumberSerializers.addAll(allDeserializers);

 assertEquals(11, allDeserializers.size());
 assertEquals(Integer.class.getName(), allDeserializers.get(Integer.class.getName()).getClass().getName());
 assertEquals(Integer.TYPE.getName(), allDeserializers.get(Integer.TYPE.getName()).getClass().getName());
 assertEquals(Long.class.getName(), allDeserializers.get(Long.class.getName()).getClass().getName());
 assertEquals(Long.TYPE.getName(), allDeserializers.get(Long.TYPE.getName()).getClass().getName());
 assertEquals(Byte.class.getName(), allDeserializers.get(Byte.class.getName()).getClass().getName());
 assertEquals(Byte.TYPE.getName(), allDeserializers.get(Byte.TYPE.getName()).getClass().getName());
 assertEquals(Short.class.getName(), allDeserializers.get(Short.class.getName()).getClass().getName());
 assertEquals(Short.TYPE.getName(), allDeserializers.get(Short.TYPE.getName()).getClass().getName());
 assertEquals(Float.class.getName(), allDeserializers.get(Float.class.getName()).getClass().getName());
 assertEquals(Float.TYPE.getName(), allDeserializers.get(Float.TYPE.getName()).getClass().getName());
 assertEquals(Double.class.getName(), allDeserializers.get(Double.class.getName()).getClass().getName());
 assertEquals(Double.TYPE.getName(), allDeserializers.get(Double.TYPE.getName()).getClass().getName());
 }

 @Test
 public void testSerializeBase() throws Exception {
 Object shortValue = Short.MIN_VALUE;
 Object intValue = Integer.MIN_VALUE;
 Object longValue = Long.MIN_VALUE;
 Object floatValue = Float.NaN;
 Object doubleValue = Double.NaN;

 short[] shortArray = {Short.MIN_VALUE, Short.MAX_VALUE};
 int[] intArray = {Integer.MIN_VALUE, Integer.MAX_VALUE};
 long[] longArray = {Long.MIN_VALUE, Long.MAX_VALUE};
 float[] floatArray = {Float.NaN, Float.POSITIVE_INFINITY};
 double[] doubleArray = {Double.NaN, Double.POSITIVE_INFINITY};

 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 mapper.writeValue(gen, shortValue);
 mapper.writeValue(gen, intValue);
 mapper.writeValue(gen, longValue);
 mapper.writeValue(gen, floatValue);
 mapper.writeValue(gen, doubleValue);
 mapper.writeValue(gen, shortArray);
 mapper.writeValue(gen, intArray);
 mapper.writeValue(gen, longArray);
 mapper.writeValue(gen, floatArray);
 mapper.writeValue(gen, doubleArray);
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Short.MIN_VALUE, mapper.readValue(parser, Short.class));
 assertEquals(Integer.MIN_VALUE, mapper.readValue(parser, Integer.class));
 assertEquals(Long.MIN_VALUE, mapper.readValue(parser, Long.class));
 assertEquals(Float.NaN, mapper.readValue(parser, Float.class));
 assertEquals(Double.NaN, mapper.readValue(parser, Double.class));
 assertEquals(shortArray, mapper.readValue(parser, short[].class));
 assertEquals(intArray, mapper.readValue(parser, int[].class));
 assertEquals(longArray, mapper.readValue(parser, long[].class));
 assertEquals(floatArray, mapper.readValue(parser, float[].class));
 assertEquals(doubleArray, mapper.readValue(parser, double[].class));
 parser.close();
 }

 @Test
 public void testSerializeShortSerializer() throws Exception {
 Object shortValue = Short.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.ShortSerializer.instance.serialize(shortValue, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Short.MIN_VALUE, mapper.readValue(parser, Short.class));
 parser.close();
 }

 @Test
 public void testSerializeIntegerSerializer() throws Exception {
 Object intValue = Integer.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.IntegerSerializer.instance.serialize(intValue, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Integer.MIN_VALUE, mapper.readValue(parser, Integer.class));
 parser.close();
 }

 @Test
 public void testSerializeIntLikeSerializer() throws Exception {
 Object intValue = Integer.MIN_VALUE;
 Object intWrapper = new Integer(intValue);
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.IntLikeSerializer.instance.serialize(intWrapper, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Integer.MIN_VALUE, mapper.readValue(parser, Integer.class));
 parser.close();
 }

 @Test
 public void testSerializeLongSerializer() throws Exception {
 Object longValue = Long.MIN_VALUE;
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.LongSerializer.instance.serialize(longValue, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Long.MIN_VALUE, mapper.readValue(parser, Long.class));
 parser.close();
 }

 @Test
 public void testSerializeFloatSerializer() throws Exception {
 Object floatValue = Float.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.FloatSerializer.instance.serialize(floatValue, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Float.NaN, mapper.readValue(parser, Float.class));
 parser.close();
 }

 @Test
 public void testSerializeDoubleSerializer() throws Exception {
 Object doubleValue = Double.NaN;
 JsonGenerator gen = mapper.getFactory().createGenerator(new java.io.File("test.json"));
 NumberSerializers.DoubleSerializer.instance.serialize(doubleValue, gen, mapper.getSerializationConfig());
 gen.close();

 // Verify serialized json
 JsonParser parser = mapper.getFactory().createParser(new java.io.File("test.json"));
 assertEquals(Double.NaN, mapper.readValue(parser, Double.class));
 parser.close();
 }
}