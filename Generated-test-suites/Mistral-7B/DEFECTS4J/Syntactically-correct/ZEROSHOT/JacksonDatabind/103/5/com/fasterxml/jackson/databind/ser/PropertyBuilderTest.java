package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

 private SerializationConfig createTestConfig() {
 return new SerializationConfig();
 }

 @Test
 public void testPropertyBuilder_Empty() throws Exception {
 PropertyBuilder pb = new PropertyBuilder(createTestConfig(null), null);
 assertNull(pb.getClassAnnotations());
 assertNull(pb.buildWriter(null, null, null, null, null, null, null, false));
 }

 @Test
 public void testPropertyBuilder_Simple() throws Exception {
 SerializationConfig config = createTestConfig();
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(Dummy.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 Annotations annotations = pb.getClassAnnotations();
 assertNotNull(annotations);
 assertEquals(0, annotations.size());

 BeanPropertyWriter writer = pb.buildWriter(null, null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testPropertyBuilder_CustomSerializationType() throws Exception {
 Class<?> testClass = CustomBean.class;
 SerializationConfig config = createTestConfig();
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(testClass));
 AnnotatedMember member = Reflections.getDeclaredField(testClass, "foo");
 JsonSerialize annotation = member.getAnnotation(JsonSerialize.class);
 CustomSerializer customSerializer = new CustomSerializer();
 TypeSerializer typeSerializer = TestTypeSerializers.createStringTypeSerializer(config);
 JavaType declaredType = TypeFactory.uncheckedType(testClass, JavaType.Generic.unbound());
 JavaType serializationType = new TypeFactory().constructType(testClass, String.class);
 Annotated am = AnnotatedClass.construct(member.getDeclaringClass(), member);
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter writer = pb.buildWriter(null, null, declaredType, null, null, null, am, false)
 .withSerializer(customSerializer)
 .withSerializerProvider(null)
 .withTypeSerializer(typeSerializer)
 .withContentTypeSerializer(null)
 .withSerializationType(serializationType);
 assertTrue(writer instanceof BeanPropertyWriter);
 }

 @Test
 public void testPropertyBuilder_NonDefault() throws Exception {
 SerializationConfig config = createTestConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(DummyWithDefaults.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 Object defaultBean = pb.getDefaultBean();
 assertNotNull(defaultBean);
 assertEquals(5, defaultBean.getClass().getDeclaredFields().length);
 }

 @Test
 public void testPropertyBuilder_NonEmpty() throws Exception {
 SerializationConfig config = createTestConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(DummyWithEmpty.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 Object defaultBean = pb.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testPropertyBuilder_SuppressNulls() throws Exception {
 SerializationConfig config = createTestConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(DummyWithNull.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter writer = pb.buildWriter(null, null, null, null, null, null, null, false);
 assertEquals(JsonInclude.Include.NON_NULL, writer.getSerializationInclusion());
 }

 @Test
 public void testPropertyBuilder_CustomNullSerializer() throws Exception {
 SerializationConfig config = createTestConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(DummyWithNull.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 Object customNullSerializer = new CustomNullSerializer();
 BeanPropertyWriter writer = pb.buildWriter(null, null, null, null, null, null, null, false)
 .withSerializerProvider(null)
 .withSerializer(customNullSerializer);
 assertEquals(customNullSerializer, writer.getSerializer());
 }

 @Test
 public void testPropertyBuilder_UnwrappingWriter() throws Exception {
 SerializationConfig config = createTestConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(DummyWithNested.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 NameTransformer unwrapper = new NameTransformer() {
 @Override public String transform(String name, TypeSerializer ts) {
 return "wrapped_" + name;
 }
 };
 BeanPropertyWriter writer = pb.buildWriter(null, null, null, null, null, null, null, false)
 .unwrappingWriter(unwrapper);
 assertEquals("wrapped_foo", writer.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyBuilder_InvalidSerializableType() throws Exception {
 SerializationConfig config = createTestConfig();
 BeanDescription beanDesc = new BeanDescription(new ClassInfo(int.class));
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter writer = pb.buildWriter(null, null, null, null, null, null, null, false);
 fail("Expected IllegalArgumentException");
 }
}

class CustomBean {
 @JsonSerialize(using = CustomSerializer.class)
 String foo;
}

class CustomSerializer extends JsonSerializer<String> {
 @Override public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString("Custom");
 }
}

class Dummy {
}

class DummyWithDefaults {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 int foo = 1;
 int bar = 42;
}

class DummyWithEmpty {
 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 String foo;
}

class DummyWithNull {
 int foo;
}

class DummyWithNested {
 @JsonUnwrapped
 public Unwrapped unwrapped;

 public static class Unwrapped {
 @JsonProperty("foo") String wrapped_foo;
 }
}