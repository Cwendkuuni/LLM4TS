package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.AnnotatedTestClass;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

private static final String TEST_CLASS_NAME = AnnotatedTestClass.class.getName();

@SuppressWarnings("unchecked")
@org.junit.Before
public void setUp() throws Exception {
 super.setUp();
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 _mapper.introspect(AnnotatedTestClass.class).findProperty("anyMap"),
 _mapper.findAnnotatedMember(AnnotatedTestClass.class, "getAnyMap"),
 new MapSerializer(null, null, null, null, null, null, null, null, null, null, null, null, null, MapSerializer.SerializerFeature.EMPTY_INT_AS_NULL, MapSerializer.SerializerFeature.FAIL_ON_EMPTY, MapSerializer.SerializerFeature.USE_EMPTY_INT_AS_NULL, MapSerializer.SerializerFeature.USE_QUICK_FOR_EMPTY_ARRAYS, MapSerializer.SerializerFeature.USE_QUICK_FOR_EMPTY_OBJECTS, MapSerializer.SerializerFeature.WRITE_EMPTY_KEYS_ARRAYS, MapSerializer.SerializerFeature.WRITE_EMPTY_KEYS_OBJECTS, MapSerializer.SerializerFeature.WRITE_NULL_KEYS, MapSerializer.SerializerFeature.WRITE_NULL_VALUE_AS_NULL, MapSerializer.SerializerFeature.WRITE_NULL_VALUE_AS_EMPTY_STRING, MapSerializer.SerializerFeature.WRITE_EMPTY_VALUE_AS_NULL, MapSerializer.SerializerFeature.WRITE_EMPTY_VALUE_AS_EMPTY_STRING, MapSerializer.SerializerFeature.WRITE_EMPTY_VALUE_AS_ABSENT, MapSerializer.SerializerFeature.WRITE_EMPTY_ARRAYS, MapSerializer.SerializerFeature.WRITE_EMPTY_ARRAYS_UNQUOTED, MapSerializer.SerializerFeature.WRITE_EMPTY_OBJECTS, MapSerializer.SerializerFeature.WRITE_EMPTY_OBJECTS_UNQUOTED));
 anyGetterWriter.resolve(_serializerProvider);
 _anyGetterWriter = anyGetterWriter;
}

@org.junit.Test
public void testGetAndSerialize() throws IOException, JsonProcessingException {
 Object bean = _objectMapper.readValue(getClass().getResource("/AnyGetterWriterTest-input.json"), AnnotatedTestClass.class);
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndSerialize(bean, gen, _serializerProvider);
 gen.flush();
 Object result = _mapper.readValue(new ByteArrayInputStream(_byteArrayOutputStream.toByteArray()), AnnotatedTestClass.class);
 assertEquals(bean, result);
}

@org.junit.Test
public void testGetAndFilter() throws IOException, JsonProcessingException {
 Object bean = _objectMapper.readValue(getClass().getResource("/AnyGetterWriterTest-input.json"), AnnotatedTestClass.class);
 PropertyFilter filter = new FilterFinder().findFilter(PropertyFilter.FilterProvider.Std.FILTER_EXCLUDE);
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndFilter(bean, gen, _serializerProvider, filter);
 gen.flush();
 Object result = _mapper.readValue(new ByteArrayInputStream(_byteArrayOutputStream.toByteArray()), AnnotatedTestClass.class);
 assertEquals(bean, result);
}

@org.junit.Test(expected = JsonMappingException.class)
public void testGetAndSerializeWithNullValue() throws IOException, JsonProcessingException {
 Object bean = new AnnotatedTestClass();
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndSerialize(bean, gen, _serializerProvider);
 fail("Should have thrown JsonMappingException");
}

@org.junit.Test(expected = JsonMappingException.class)
public void testGetAndSerializeWithNonMapValue() throws IOException, JsonProcessingException {
 Object bean = new AnnotatedTestClass();
 bean.setAnyMap(new String[]{"test"});
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndSerialize(bean, gen, _serializerProvider);
 fail("Should have thrown JsonMappingException");
}

@org.junit.Test(expected = JsonMappingException.class)
public void testGetAndFilterWithNullValue() throws IOException, JsonProcessingException {
 Object bean = new AnnotatedTestClass();
 PropertyFilter filter = new FilterFinder().findFilter(PropertyFilter.FilterProvider.Std.FILTER_EXCLUDE);
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndFilter(bean, gen, _serializerProvider, filter);
 fail("Should have thrown JsonMappingException");
}

@org.junit.Test(expected = JsonMappingException.class)
public void testGetAndFilterWithNonMapValue() throws IOException, JsonProcessingException {
 Object bean = new AnnotatedTestClass();
 bean.setAnyMap(new String[]{"test"});
 PropertyFilter filter = new FilterFinder().findFilter(PropertyFilter.FilterProvider.Std.FILTER_EXCLUDE);
 JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 _anyGetterWriter.getAndFilter(bean, gen, _serializerProvider, filter);
 fail("Should have thrown JsonMappingException");
}

@org.junit.After
public void tearDown() throws Exception {
 super.tearDown();
 _anyGetterWriter = null;
}

}