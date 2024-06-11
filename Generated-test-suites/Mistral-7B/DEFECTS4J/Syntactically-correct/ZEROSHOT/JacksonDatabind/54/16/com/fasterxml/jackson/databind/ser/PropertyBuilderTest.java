package com.example; // replace with your test package name

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @After
 public void tearDown() throws Exception {
 objectMapper = null;
 }

 @Test
 public void testPropertyBuilder() throws JsonProcessingException {
 // Test constructor
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(objectMapper.getDefaultConfig()),
 new BeanDescription(new ClassInfo(TestClass.class)));
 assertNotNull(propertyBuilder);

 // Test getClassAnnotations()
 Annotations classAnnotations = propertyBuilder.getClassAnnotations();
 assertNotNull(classAnnotations);

 // Test buildWriter() with no arguments
 fail("Test buildWriter() with no arguments"); // should not be called

 // Test buildWriter() with serialization type and default useStaticTyping = false
 SerializerProvider serProvider = new SerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(new BeanProperty(new AnnotatedMember(TestClass.class, "property1"), null));
 JavaType declaredType = new TypeFactory().constructType(TestClass.class);
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getGenericType()).thenReturn(new TypeFactory().constructType(String.class));
 when(am.getName()).thenReturn("property1");
 Object propValue = new Object();
 when(ser.serializeValues(any(), any(), any(), any(), any(), any(), any(), anyBoolean(), any()))
 .thenReturn(propValue);
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(serProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 assertNotNull(bpw);

 // Test buildWriter() with serialization type and default useStaticTyping = true
 JavaType serializationType = new TypeFactory().constructType(String[].class);
 bpw = propertyBuilder.buildWriter(serProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, true);
 assertEquals(serializationType, bpw.getType());

 // Test getPropertyDefaultValue()
 TestClass defaultBean = new TestClass();
 Object propDefaultValue = propertyBuilder.getPropertyDefaultValue("property1", am, declaredType);
 assertNull(propDefaultValue);

 propDefaultValue = propertyBuilder.getPropertyDefaultValue("property2", am, declaredType);
 assertEquals("", propDefaultValue);

 // Test getDefaultValue()
 assertEquals(Integer.valueOf(0), propertyBuilder.getDefaultValue(new TypeFactory().constructType(int.class)));
 assertEquals(Integer.valueOf(0), propertyBuilder.getDefaultValue(new TypeFactory().constructType(Integer.class)));
 assertEquals("", propertyBuilder.getDefaultValue(new TypeFactory().constructType(String.class)));

 // Test getDefaultBean()
 TestClass defaultBean2 = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean2);

 // Test findSerializationType()
 Annotated a = mock(Annotated.class);
 when(a.getName()).thenReturn("testProperty");
 when(a.getRawType()).thenReturn(String[].class);
 JavaType declaredType2 = new TypeFactory().constructType(String[].class);
 JavaType secondary = propertyBuilder.findSerializationType(a, false, declaredType2);
 assertNull(secondary);

 // Test findSerializationType() with JsonSerialize annotation
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 jsonSerializeAnnotation.using(new ClassByName(String[].class));
 AnnotatedMember am2 = mock(AnnotatedMember.class);
 when(am2.getMember()).thenReturn(new BeanProperty(new Annotated(TestClass.class, "testProperty"), null));
 when(am2.getValue()).thenReturn("");
 when(am2.getRawType()).thenReturn(String[].class);
 when(a.getMember()).thenReturn(am2);
 when(a.getRawClass()).thenReturn(String[].class);
 when(am2.isStatic()).thenReturn(false);
 when(a.isStatic()).thenReturn(false);
 when(a.isAnnotated()).thenReturn(true);
 when(a.getAnnotations()).thenReturn(new Annotation[] { jsonSerializeAnnotation });
 secondary = propertyBuilder.findSerializationType(a, false, declaredType2);
 assertNotNull(secondary);
 assertEquals(String[].class, secondary.getRawClass());

 // Test findSerializationType() with @JsonSerialize annotation on member
 JsonSerialize jsonSerializeAnnotationOnMember = new JsonSerialize();
 jsonSerializeAnnotationOnMember.using(new ClassByName(String.class));
 when(am2.getMember()).thenReturn(new BeanProperty(new Annotated(TestClass.class, "testProperty"), null));
 when(am2.getValue()).thenReturn("");
 when(am2.getRawType()).thenReturn(String.class);
 when(a.getMember()).thenReturn(am2);
 when(a.getRawClass()).thenReturn(TestClass.class);
 when(a.isStatic()).thenReturn(false);
 when(a.isAnnotated()).thenReturn(true);
 when(a.getAnnotations()).thenReturn(new Annotation[] { jsonSerializeAnnotationOnMember });
 secondary = propertyBuilder.findSerializationType(a, false, declaredType2);
 assertNotNull(secondary);
 assertEquals(String.class, secondary.getRawClass());

 // Test findSerializationType() with @JsonSerialize annotation on class
 JsonSerialize jsonSerializeAnnotationOnClass = new JsonSerialize();
 jsonSerializeAnnotationOnClass.using(new ClassByName(TestClass.class));
 when(a.getMember()).thenReturn(null);
 when(a.getRawClass()).thenReturn(TestClass.class);
 when(a.isStatic()).thenReturn(true);
 when(a.isAnnotated()).thenReturn(true);
 when(a.getAnnotations()).thenReturn(new Annotation[] { jsonSerializeAnnotationOnClass });
 secondary = propertyBuilder.findSerializationType(a, false, declaredType2);
 assertNotNull(secondary);
 assertEquals(TestClass.class, secondary.getRawClass());
 }

 @Test
 public void testFindSerializationTypeWithStaticTyping() throws JsonProcessingException {
 // Test findSerializationType() with JsonSerialize annotation on member and static typing = true
 JsonSerialize jsonSerializeAnnotationOnMember = new JsonSerialize();
 jsonSerializeAnnotationOnMember.using(new ClassByName(Integer.class));
 AnnotatedMember am2 = mock(AnnotatedMember.class);
 when(am2.getMember()).thenReturn(new BeanProperty(new Annotated(TestClass.class, "testProperty"), null));
 when(am2.getValue()).thenReturn(1);
 when(am2.getRawType()).thenReturn(Integer.class);
 when(a.getMember()).thenReturn(am2);
 when(a.getRawClass()).thenReturn(TestClass.class);
 when(a.isStatic()).thenReturn(true);
 when(a.isAnnotated()).thenReturn(true);
 when(a.getAnnotations()).thenReturn(new Annotation[] { jsonSerializeAnnotationOnMember });
 JavaType declaredType2 = new TypeFactory().constructType(Integer.class);
 JavaType secondary = propertyBuilder.findSerializationType(a, true, declaredType2);
 assertNotNull(secondary);
 assertEquals(Integer.class, secondary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithIllegalType() throws JsonProcessingException {
 // Test findSerializationType() with illegal type
 Annotated a = mock(Annotated.class);
 when(a.getName()).thenReturn("testProperty");
 when(a.getRawType()).thenReturn(Map.class);
 when(a.isStatic()).thenReturn(false);
 when(a.isAnnotated()).thenReturn(true);
 when(a.getAnnotations()).thenReturn(new Annotation[0]);
 JavaType declaredType2 = new TypeFactory().constructType(String.class);
 JavaType secondary = propertyBuilder.findSerializationType(a, false, declaredType2);
 fail("findSerializationType() should throw IllegalArgumentException for Map type");
 }

 @Test
 public void testBuildWriterWithIllegalType() throws JsonProcessingException {
 // Test buildWriter() with illegal type
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(new BeanProperty(new AnnotatedMember(TestClass.class, "testProperty"), null), null);
 JavaType declaredType = new TypeFactory().constructType(Map.class);
 when(ser.serializeValues(any(), any(), any(), any(), any(), any(), any(), anyBoolean(), any()))
 .thenReturn(new HashMap<>());
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getGenericType()).thenReturn(new TypeFactory().constructType(Object.class));
 when(am.getName()).thenReturn("testProperty");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(serProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 fail("buildWriter() should throw IllegalArgumentException for Map type");
 }
}

class TestClass {

 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 String property1;
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 String property2 = "defaultValue";
}