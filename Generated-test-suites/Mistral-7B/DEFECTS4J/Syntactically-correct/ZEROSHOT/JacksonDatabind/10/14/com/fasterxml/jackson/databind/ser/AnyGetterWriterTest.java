package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.TestBean;

public class AnyGetterWriterTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() throws Exception {
 ObjectCarrier carrier = new ObjectCarrier();
 BeanProperty property = new BeanProperty("anyMap", Map.class, null, carrier, null);
 AnnotatedMember accessor = new AnnotatedMember("anyGetter", Map.class, null, carrier, property, null);
 MapSerializer mapSerializer = new MapSerializer(null, null, null, null, null, null, null, null, null);
 AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
 JacksonAnnotations.Impl jacksonAnnotations = new JacksonAnnotations.Impl();
 ObjectModifier modifier = new ObjectModifier();
 property.addAnnotation(JacksonAnnotationType.ANNOTATION_TYPE_JSON_ANY_GETTER, jacksonAnnotations);
 PropertyName propertyName = new PropertyName("anyMap");
 writer = modifier.addValueHandler(writer, propertyName, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 AnyGetterWriter testWriter = writer;
 TestUtils.setFieldValue(testWriter, "_mapSerializer", mapSerializer);
 TestUtils.setFieldValue(testWriter, "_accessor", accessor);
 TestUtils.setFieldValue(testWriter, "_property", property);
 TestUtils.setStaticFieldValue(MapSerializer.class, "_emptyMapSerializer", mapSerializer);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new TestBean();
 TestUtils.setFieldValue(bean, "anyMap", new HashMap<>());
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 mapper.writeValueAsString(gen);
 gen.flush();
 Map<String, Object> resultMap = mapper.readValue(mapper.writeValueAsString(bean), Map.class);
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 assertEquals(expectedMap, resultMap);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new TestBean();
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 mapper.writeValueAsString(gen);
 gen.flush();
 fail("Expected JsonMappingException with message: Value returned by 'any-getter' (anyMap()) not java.util.Map but null");
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 TestUtils.setFieldValue(bean, "anyMap", new Integer(1));
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndSerialize(bean, gen, mapper.getSerializerProvider());
 mapper.writeValueAsString(gen);
 gen.flush();
 fail("Expected JsonMappingException with message: Value returned by 'any-getter' (anyMap()) not java.util.Map but class java.lang.Integer");
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new TestBean();
 TestUtils.setFieldValue(bean, "anyMap", new HashMap<>());
 PropertyFilter filter = new Filter("testFilter");
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 mapper.writeValueAsString(gen);
 gen.flush();
 Map<String, Object> resultMap = mapper.readValue(mapper.writeValueAsString(bean), Map.class);
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1Filtered");
 expectedMap.put("key2", null);
 assertEquals(expectedMap, resultMap);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 Object bean = new TestBean();
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 PropertyFilter filter = new Filter("testFilter");
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 mapper.writeValueAsString(gen);
 gen.flush();
 fail("Expected JsonMappingException with message: Value returned by 'any-getter' (anyMap()) not java.util.Map but null");
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 TestUtils.setFieldValue(bean, "anyMap", new Integer(1));
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(mapper.getSerializerProvider());
 PropertyFilter filter = new Filter("testFilter");
 JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getJsonFactory()
 .createGenerator(System.out);
 writer.getAndFilter(bean, gen, mapper.getSerializerProvider(), filter);
 mapper.writeValueAsString(gen);
 gen.flush();
 fail("Expected JsonMappingException with message: Value returned by 'any-getter' (anyMap()) not java.util.Map but class java.lang.Integer");
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
 writer = writer.withResolved(objectMapper.getSerializerProvider());
 Assert.assertNotNull(writer.getMapSerializer());
 }

 @Test
 public void testEmptyMapSerializer() throws Exception {
 MapSerializer mapSerializer = new MapSerializer(null, null, null, null, null, null, null, null, null);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectCarrier carrier = new ObjectCarrier();
 BeanPropertyDef property = new BeanPropertyDef("anyMap", Map.class, null, carrier, null, null);
 AnnotatedMember accessor = new AnnotatedMember("anyGetter", Map.class, null, carrier, property, null);
 AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
 ObjectModifier modifier = new ObjectModifier();
 writer = modifier.addValueHandler(writer, new PropertyName("anyMap"), null);
 writer = writer.withResolved(objectMapper.getSerializerProvider());
 Assert.assertEquals(mapSerializer, writer.getMapSerializer());
 }

}

class TestBean {
 Map<String, Object> anyMap;
}

class Filter {
 String filterName;

 Filter(String filterName) {
 this.filterName = filterName;
 }

 @Override
 public String toString() {
 return "testFilter";
 }
}

class ObjectCarrier {
}

class BeanPropertyDef extends PropertyDefBase {
 String propertyName;

 BeanPropertyDef(String propertyName, Class<?> propertyType, PropertyMarker marker, ObjectCarrier carrier,
 AnnotatedMember member, JsonSerializer<?> serializer) {
 super(propertyName, propertyType, marker, carrier, member, serializer);
 this.propertyName = propertyName;
 }
}

class PropertyDefBase {
 private final String propertyName;
 private final Class<?> propertyType;
 private final PropertyMarker marker;
 private final ObjectCarrier carrier;
 private final AnnotatedMember member;
 private final JsonSerializer<?> serializer;

 protected PropertyDefBase(String propertyName, Class<?> propertyType, PropertyMarker marker, ObjectCarrier carrier,
 AnnotatedMember member, JsonSerializer<?> serializer) {
 this.propertyName = propertyName;
 this.propertyType = propertyType;
 this.marker = marker;
 this.carrier = carrier;
 this.member = member;
 this.serializer = serializer;
 }

 public String getName() {
 return propertyName;
 }

 public Class<?> getType() {
 return propertyType;
 }

 public PropertyMarker getMarker() {
 return marker;
 }

 public ObjectCarrier getCarrier() {
 return carrier;
 }

 public AnnotatedMember getMember() {
 return member;
 }

 public JsonSerializer<?> getSerializer() {
 return serializer;
 }
}

class ObjectModifier {
 // Implement methods to set value handlers and other modifications here
}