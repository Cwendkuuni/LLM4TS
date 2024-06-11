package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertyBuilder Test")
public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private AnnotationIntrospector annotationIntrospector;
 private PropertyBuilder propertyBuilder;

 @BeforeEach
 public void setup() {
// Create mock objects for SerializationConfig, BeanDescription and AnnotationIntrospector
 config = mock(SerializationConfig.class);
 beanDesc = mock(BeanDescription.class);
 annotationIntrospector = mock(AnnotationIntrospector.class);

 // Initialize PropertyBuilder
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 @DisplayName("Test constructor")
 public void testConstructor() {
 // Test that PropertyBuilder is initialized correctly
 assertNotNull(propertyBuilder);
 assertEquals(config, propertyBuilder._config);
 assertEquals(beanDesc, propertyBuilder._beanDesc);
 assertEquals(_defaultInclusion, propertyBuilder._defaultInclusion);
 assertEquals(annotationIntrospector, propertyBuilder._annotationIntrospector);
 }

 @Test
 @DisplayName("Test getClassAnnotations method")
 public void testGetClassAnnotations() {
 // Mock the return value of getClassAnnotations from BeanDescription
 when(beanDesc.getClassAnnotations()).thenReturn(mock(Annotations.class));

 // Test that getClassAnnotations method returns the expected result
 Annotations actual = propertyBuilder.getClassAnnotations();
 assertEquals(mock(Annotations.class), actual);
 }

 @Test
 @DisplayName("Test buildWriter method with null parameters")
 public void testBuildWriterWithNullParameters() {
 // Test that buildWriter throws IllegalArgumentException when given null parameters
 assertThrows(IllegalArgumentException.class, () -> propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
 }

 @Test
 @DisplayName("Test buildWriter method with valid parameters")
 public void testBuildWriterWithValidParameters() {
 // Mock the return value of findSerializationType from AnnotationIntrospector
 when(annotationIntrospector.refineSerializationType(eq(config), any(), any()))
 .thenReturn(mock(JavaType.class));

 // Mock the return value of getClassAnnotations from BeanDescription
 when(beanDesc.getClassAnnotations()).thenReturn(mock(Annotations.class));

 // Create mock objects for SerializerProvider, BeanPropertyDefinition, JavaType, JsonSerializer, TypeSerializer, and TypeSerializer
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mock(JavaType.class);
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);

 // Test that buildWriter returns the expected BeanPropertyWriter instance
 BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

 // Verify that findSerializationType was called with the correct arguments
 verify(annotationIntrospector).refineSerializationType(eq(config), any(), eq(declaredType));

 // Verify that BeanPropertyWriter was initialized correctly with the returned TypeSerializer and JsonSerializer
 verify(beanDesc).getClassAnnotations();
 verify(am).getValue(any());
 assertEquals(ser, actual.getSerializer());
 assertEquals(typeSer, actual.getTypeSerializer());
 }

 @Test
 @DisplayName("Test findSerializationType method with null annotation and declared type")
 public void testFindSerializationTypeWithNullAnnotationAndDeclaredType() {
 // Test that findSerializationType returns null when given null annotation and declared type
 assertEquals(null, propertyBuilder.findSerializationType(null, false, null));
 }

 @Test
 @DisplayName("Test findSerializationType method with valid annotation and declared type")
 public void testFindSerializationTypeWithValidAnnotationAndDeclaredType() {
 // Mock the return value of refineSerializationType from AnnotationIntrospector
 JavaType javaType = mock(JavaType.class);
 when(annotationIntrospector.refineSerializationType(eq(config), any(), eq(JavaType.class.cast(declaredType.getRawClass()))))
 .thenReturn(javaType);

 // Test that findSerializationType returns the expected JavaType instance
 JavaType actual = propertyBuilder.findSerializationType(mock(Annotated.class), false, declaredType);
 assertEquals(javaType, actual);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with null name, member, and type")
 public void testGetPropertyDefaultValueWithNullNameMemberAndType() {
 // Test that getPropertyDefaultValue returns null when given null name, member, and type
 assertEquals(null, propertyBuilder.getPropertyDefaultValue(null, null, null));
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with valid name, member, and type, and default bean is not null")
 public void testGetPropertyDefaultValueWithValidNameMemberTypeAndDefaultBeanIsNotNull() {
 // Mock the return value of instantiateBean from BeanDescription
 Object defaultBean = mock(Object.class);
 when(beanDesc.instantiateBean(anyBoolean()))
 .thenReturn(defaultBean);

 // Test that getPropertyDefaultValue returns the expected value
 String name = "name";
 AnnotatedMember member = mock(AnnotatedMember.class);
 JavaType type = mock(JavaType.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);

 // Verify that instantiateBean was called with the correct argument
 verify(beanDesc).instantiateBean(anyBoolean());
 assertEquals(defaultBean, actual);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with valid name, member, and type, and default bean is null")
 public void testGetPropertyDefaultValueWithValidNameMemberTypeAndDefaultBeanIsNull() {
 // Test that getPropertyDefaultValue returns null when given valid name, member, and type, and default bean is null
 String name = "name";
 AnnotatedMember member = mock(AnnotatedMember.class);
 JavaType type = mock(JavaType.class);
 Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals(null, actual);
 }

 @Test
 @DisplayName("Test getDefaultBean method with canOverrideAccessModifiers set to true")
 public void testGetDefaultBeanWithCanOverrideAccessModifiersSetToTrue() {
 // Test that getDefaultBean returns the expected default bean instance when canOverrideAccessModifiers is set to true
 Object defaultBean = new Object();
 when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);

 Object actual = propertyBuilder.getDefaultBean();
 assertEquals(defaultBean, actual);
 }

 @Test
 @DisplayName("Test getDefaultBean method with canOverrideAccessModifiers set to false")
 public void testGetDefaultBeanWithCanOverrideAccessModifiersSetToFalse() {
 // Test that getDefaultBean returns null when canOverrideAccessModifiers is set to false
 Object actual = propertyBuilder.getDefaultBean();
 assertEquals(null, actual);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with null name, and type is a primitive or wrapper type for a primitive")
 public void testGetPropertyDefaultValueWithNullNameAndTypeIsPrimitiveOrWrapperType() {
 // Test that getPropertyDefaultValue returns the default value for primitive or wrapper types when given null name
 int intValue = 0;
 Integer intWrapper = 0;

 // Test int type
 Object actualInt = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.booleanType());
 assertEquals(intValue, actualInt);

 // Test Integer wrapper type
 Object actualIntWrapper = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.intType());
 assertEquals(intWrapper, actualIntWrapper);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with null name, and type is a String")
 public void testGetPropertyDefaultValueWithNullNameAndTypeIsString() {
 // Test that getPropertyDefaultValue returns an empty String when given null name and String type
 String actual = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.stringType());
 assertEquals("", actual);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method with null name, and type is a Collection, Map, or Array")
 public void testGetPropertyDefaultValueWithNullNameAndTypeIsContainerOrReference() {
 // Test that getPropertyDefaultValue returns null when given null name and Container or Reference type
 List list = Arrays.asList(1, 2, 3);
 Map<String, String> map = new HashMap<>();
 Object[] array = new Object[3];

 // Test List type
 Object actualList = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.collectionType(JavaTypeFactory.intType(), JavaTypeFactory.listType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualList);

 // Test Map type
 Object actualMap = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.mapType(JavaTypeFactory.stringType(), JavaTypeFactory.stringType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualMap);

 // Test Array type
 Object actualArray = propertyBuilder.getPropertyDefaultValue(null, null, JavaTypeFactory.arrayType(JavaTypeFactory.stringType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualArray);
 }

 @Test
 @DisplayName("Test getDefaultValue method with null type")
 public void testGetDefaultValueWithNullType() {
 // Test that getDefaultValue returns null when given null type
 assertEquals(null, propertyBuilder.getDefaultValue(null));
 }

 @Test
 @DisplayName("Test getDefaultValue method with primitive or wrapper type for primitive")
 public void testGetDefaultValueWithPrimitiveOrWrapperTypeForPrimitive() {
 // Test that getDefaultValue returns the default value for primitive or wrapper types
 int intValue = 0;
 Integer intWrapper = 0;

 // Test int type
 Object actualInt = propertyBuilder.getDefaultValue(JavaTypeFactory.booleanType());
 assertEquals(intValue, actualInt);

 // Test Integer wrapper type
 Object actualIntWrapper = propertyBuilder.getDefaultValue(JavaTypeFactory.intType());
 assertEquals(intWrapper, actualIntWrapper);
 }

 @Test
 @DisplayName("Test getDefaultValue method with String type")
 public void testGetDefaultValueWithStringType() {
 // Test that getDefaultValue returns an empty String when given String type
 String actual = propertyBuilder.getDefaultValue(JavaTypeFactory.stringType());
 assertEquals("", actual);
 }

 @Test
 @DisplayName("Test getDefaultValue method with Collection, Map, or Array type")
 public void testGetDefaultValueWithContainerOrReferenceType() {
 // Test that getDefaultValue returns null when given Container or Reference type
 List list = Arrays.asList(1, 2, 3);
 Map<String, String> map = new HashMap<>();
 Object[] array = new Object[3];

 // Test List type
 Object actualList = propertyBuilder.getDefaultValue(JavaTypeFactory.collectionType(JavaTypeFactory.intType(), JavaTypeFactory.listType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualList);

 // Test Map type
 Object actualMap = propertyBuilder.getDefaultValue(JavaTypeFactory.mapType(JavaTypeFactory.stringType(), JavaTypeFactory.stringType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualMap);

 // Test Array type
 Object actualArray = propertyBuilder.getDefaultValue(JavaTypeFactory.arrayType(JavaTypeFactory.stringType()));
 assertEquals(JsonInclude.Include.NON_EMPTY, actualArray);
 }
}