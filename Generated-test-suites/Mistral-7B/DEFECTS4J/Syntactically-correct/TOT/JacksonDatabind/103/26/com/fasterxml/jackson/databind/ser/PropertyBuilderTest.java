package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

 private SerializationConfig _config;
 private BeanDescription _beanDesc;
 private Object _defaultBean;

 @Before
 public void setUp() throws Exception {
 _config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(_config);
 _beanDesc = mapper.getSerializationConfig().findBeanDescription(MyPojo.class);
 _defaultBean = mapper.getValueFactory().getSimpleValueInstance(MyPojo.class);
 }

 /**
 * Test for default value handling when property has no default value.
 */
 @Test
 public void testNoDefaultValue() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyPojo.class.getType(), null, null, null, null, false);
 assertNull(bpw.getValueToSuppress());
 }

 /**
 * Test for default value handling when property has a default value.
 */
 @Test
 public void testHasDefaultValue() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyPojo.class.getType(), null, null, null, null, false);
 Object defaultValue = BeanUtil.getDefaultValue(MyPojo.class);
 assertEquals(defaultValue, bpw.getValueToSuppress());
 }

 /**
 * Test for default value handling when property has a custom null serializer.
 */
 @Test
 public void testCustomNullSerializer() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setName("Test");
 MyAnnotated myAnnotated = new MyAnnotated();
 myAnnotated.setProperty(pojo);
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyAnnotated.class.getType(), null, null, null, myAnnotated, false);
 Object nullValue = bpw.getValueToSuppress();
 assertNotNull(nullValue);
 // Custom null serializer should not serialize null
 JsonSerializer<Object> nullSerializer = new JsonNullSerializer();
 assertEquals(nullSerializer, bpw.getNullSerializer());
 }

 /**
 * Test for default value handling when property is marked as non-default.
 * The test checks that the property value is not serialized when inclusion
 * type is {@link JsonInclude.Include#NON_DEFAULT}.
 */
 @Test
 public void testNonDefault() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 MyPojo pojo = new MyPojo();
 pojo.setName("Test");
 pojo.setDefaultValue(BeanUtil.getDefaultValue(MyPojo.class));
 _beanDesc = ObjectMapper.defaultInstance().getSerializationConfig().findBeanDescription(MyPojo.class);
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyPojo.class.getType(), null, null, null, null, false);
 StringWriter sw = new StringWriter();
 JsonGenerator jg = new JsonFactory().createGenerator(sw);
 ObjectWriter ow = new JsonFactory().createObjectWriter(jg);
 ow.writeValue(jg, pojo);
 assertTrue(sw.toString().contains("\"name\":\""+pojo.getName()+"\""));
 assertTrue(sw.toString().contains("\"defaultValue\":null"));
 }

 /**
 * Test for default value handling when property has a custom inclusion filter.
 */
 @Test
 public void testCustomInclusionFilter() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setName("Test");
 pojo.setDefaultValue(BeanUtil.getDefaultValue(MyPojo.class));
 MyAnnotated myAnnotated = new MyAnnotated();
 myAnnotated.setProperty(pojo);
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyAnnotated.class.getType(), null, null, null, myAnnotated, false);
 // Custom inclusion filter should not serialize default value
 InclusionFilter inclusionFilter = new InclusionFilter() {
 @Override public boolean includeField(SerializedName name) {
 return false;
 }
 };
 assertEquals(inclusionFilter, bpw.getInclusionFilter());
 }

 /**
 * Test for default value handling when property is marked as non-null.
 */
 @Test
 public void testNonNull() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 MyPojo pojo = new MyPojo();
 pojo.setName("Test");
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyPojo.class.getType(), null, null, null, null, false);
 assertFalse(bpw.suppressNulls());
 // Non-null property should not suppress nulls
 MyPojo pojoWithNull = new MyPojo();
 pojoWithNull.setName(null);
 try {
 bpw.writeValueAsString(pojoWithNull);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 /**
 * Test for default value handling when property is marked as non-empty.
 */
 @Test
 public void testNonEmpty() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
 MyPojo pojo = new MyPojo();
 pojo.setName("Test");
 pojo.setList(new ArrayList<>());
 BeanPropertyWriter bpw = pb.buildWriter(null, null, MyPojo.class.getType(), null, null, null, null, false);
 assertFalse(bpw.suppressNulls());
 // Non-empty property should not suppress empty collections
 MyPojo pojoWithEmptyList = new MyPojo();
 pojoWithEmptyList.setName("Test");
 pojoWithEmptyList.setList(new ArrayList<>());
 try {
 bpw.writeValueAsString(pojoWithEmptyList);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }
}