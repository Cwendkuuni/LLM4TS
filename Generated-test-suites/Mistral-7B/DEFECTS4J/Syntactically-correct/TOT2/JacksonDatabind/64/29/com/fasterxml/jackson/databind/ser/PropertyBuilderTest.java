import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 BeanFactory beanFactory = mapper.getTypeFactory();
 beanDesc = beanFactory.constructDescForClass(Person.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testBuildWriter_NoAnnotation() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 JavaType declaredType = beanDesc.getType();
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 BeanPropertyWriter expectedWriter = mock(BeanPropertyWriter.class);
 when(am.getType()).thenReturn(Integer.class);
 when(propertyBuilder.buildWriter(eq(config.getSerializerProvider()), eq(propDef), eq(declaredType), eq(ser), eq(typeSer), eq(contentTypeSer), eq(am), anyBoolean())).thenReturn(expectedWriter);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriter_JsonIncludeAnnotation() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("age");
 JavaType declaredType = beanDesc.getType();
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.isAnnotationPresent(JsonInclude.class)).thenReturn(true);
 when(propertyBuilder.buildWriter(eq(config.getSerializerProvider()), eq(propDef), eq(declaredType), eq(ser), eq(typeSer), eq(contentTypeSer), eq(am), anyBoolean())).thenThrow(JsonMappingException.class);
 try {
 propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetClassAnnotations() {
 Class<?> clazz = Person.class;
 ClassAnnotations expected = beanDesc.getClassAnnotations();
 ClassAnnotations actual = propertyBuilder.getClassAnnotations();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_IllegalType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 when(a.getType()).thenReturn(new ClassType(Object.class, Object.class));
 propertyBuilder.findSerializationType(a, true, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindSerializationType_ValidType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 when(a.getType()).thenReturn(new ClassType(String.class, String[].class));
 propertyBuilder.findSerializationType(a, true, null);
 // Verify no exception was thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTyping_IllegalType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 when(a.getType()).thenReturn(declaredType);
 JsonSerialize.Typing typing = propertyBuilder.findSerializationTyping(a);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindSerializationTyping_DefaultTyping() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 when(a.getType()).thenReturn(declaredType);
 JsonSerialize.Typing expectedTyping = JsonSerialize.Typing.DEFAULT_TYPING;
 JsonSerialize.Typing actualTyping = propertyBuilder.findSerializationTyping(a);
 assertEquals(expectedTyping, actualTyping);
 }

 @Test
 public void testFindSerializationTyping_StaticTyping() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 when(a.getType()).thenReturn(declaredType);
 JsonSerialize.Typing expectedTyping = JsonSerialize.Typing.STATIC;
 ObjectAnnotation annotation = mock(ObjectAnnotation.class);
 when(annotation.getValue("useStaticTyping")).thenReturn(true);
 when(a.getAnnotation(ObjectAnnotation.class)).thenReturn(annotation);
 JsonSerialize.Typing actualTyping = propertyBuilder.findSerializationTyping(a);
 assertEquals(expectedTyping, actualTyping);
 }

 @Test
 public void testFindSerializationType_AnnotatedType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 JavaType declaredType = beanDesc.getType();
 when(a.getType()).thenReturn(declaredType);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 when(propertyBuilder._annotationIntrospector).thenReturn(annotationIntrospector);
 JavaType expectedType = new ClassType(Integer[].class, Integer[].class);
 when(annotationIntrospector.findSerializationType(any(Annotated.class), anyBoolean(), any(JavaType.class))).thenReturn(expectedType);
 JavaType actualType = propertyBuilder.findSerializationType(a, false, null);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPropertyDefaultValue_NullValue() {
 String propertyName = "name";
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, null);
 assertNull(actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_StringValue() {
 String propertyName = "gender";
 String expectedDefaultValue = "male";
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, null);
 assertEquals(expectedDefaultValue, actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_NumberValue() {
 String propertyName = "height";
 int expectedDefaultValue = 170;
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 assertEquals(expectedDefaultValue, actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_NullBeen() {
 String propertyName = "age";
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 assertNull(actualDefaultValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_NoDefaultValue() throws JsonMappingException {
 String propertyName = "address";
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetPropertyDefaultValue_BeanValue() throws JsonMappingException {
 String propertyName = "email";
 Person expectedDefaultValue = new Person("John Doe", 25);
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 assertEquals(expectedDefaultValue, actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_ArrayValue() throws JsonMappingException {
 String propertyName = "skills";
 String[] expectedDefaultValue = {"Java", "Python", "C++"};
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 assertArrayEquals(expectedDefaultValue, (String[]) actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_EmptyArrayValue() throws JsonMappingException {
 String propertyName = "skills";
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, beanDesc.getType());
 assertArrayEquals(new String[0], (String[]) actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_NullType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(null);
 assertNull(actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_EmptyStringType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(new ClassType(String.class, String.class));
 assertEquals("", actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_StringType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(new ClassType(String.class, String.class));
 assertEquals("", actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_NumberType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(new ClassType(int[].class, int[].class));
 assertEquals(0, actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_ContainerType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(new ClassType(List.class, Object.class));
 assertEquals(Collections.emptyList(), actualDefaultValue);
 }

 @Test
 public void testGetDefaultValue_ReferenceType() {
 Object actualDefaultValue = propertyBuilder.getDefaultValue(new ClassType(Person.class, Person.class));
 assertNull(actualDefaultValue);
 }

 @Test
 public void testThrowsJsonMappingException_NonDefault() throws JsonMappingException {
 String propertyName = "age";
 Object propertyValue = 25;
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, false).writeValueAsString(propertyValue);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testThrowsJsonMappingException_Default() throws JsonMappingException {
 String propertyName = "name";
 Object propertyValue = "John Doe";
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, true).writeValueAsString(propertyValue);
 assertEquals(propertyValue.toString(), propertyBuilder.buildWriter(null, null, null, null, null, null, null, true).writeValueAsString(propertyValue));
 } catch (JsonProcessingException e) {
 fail("Expected JsonMappingException");
 }
 }
}