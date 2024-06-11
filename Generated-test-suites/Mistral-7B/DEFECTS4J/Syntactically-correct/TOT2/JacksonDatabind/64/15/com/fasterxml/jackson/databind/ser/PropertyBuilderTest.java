import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode testObject;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode testObjNode = mapper.createObjectNode();
 testObject = mapper.readValue(testObjNode.toString(), Object.class);
 propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), new BeanDescription(testObject.getClass()));
 }

 @Test
 public void testBuildWriter_NormalCase() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriter_NullSerializer() throws JsonMappingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 assertNull(writer.getSerializer());
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 doReturn(serializer).when(propertyBuilderSpy).buildSerializer(any(BeanPropertyDefinition.class), any(Object.class), any(JavaType.class), anyBoolean());
 BeanPropertyWriter writerSpy = spy(writer);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 when(propDef.getName()).thenReturn("testProp");
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 when(propertyBuilderSpy.buildWriter(null, eq(propDef), eq(declaredType), eq(serializer), any(TypeSerializer.class), any(TypeSerializer.class), any(AnnotatedMember.class), eq(false))).thenReturn(writerSpy);
 Object value = "test";
 assertEquals(writerSpy.getSerializer(), serializer);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, null, null, declaredType, null, null, null, false, null);
 verify(writerSpy).getSerializer();
 verify(writerSpy).assignNullSerializer(eq(serializer));
 verifyNoMoreInteractions(writerSpy);
 assertEquals(expectedWriter, writer);
 }

 @Test
 public void testFindSerializationType_NullAnnotation() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(String[].class);
 assertNull(propertyBuilder.findSerializationType(null, false, type));
 }

 @Test
 public void testFindSerializationType_NullDeclaredType() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(String[].class);
 assertNull(propertyBuilder.findSerializationType(mock(Annotated.class), false, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_IllegalType() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(new Class[] { String.class, Integer.class }.getClass());
 propertyBuilder.findSerializationType(null, false, type);
 }

 @Test
 public void testGetClassAnnotations() throws JsonMappingException {
 JsonInclude annotation = mock(JsonInclude.class);
 when(annotation.value()).thenReturn(JsonInclude.Include.NON_NULL);
 when(annotation.asJavaAnnotations()).thenReturn(new JsonInclude[] { annotation });
 assertEquals(annotation, propertyBuilder.getClassAnnotations()[0]);
 }

 @Test
 public void testBuildWriter_PropertyDefaultValue() throws JsonMappingException {
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class))).thenReturn("default");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, mapper.getTypeFactory().constructType(String.class), null, null, null, false, "default");
 assertEquals(expectedWriter, writer);
 }

 @Test
 public void testGetPropertyDefaultValue_NoDefaultBean() throws JsonMappingException {
 assertNull(propertyBuilder.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_NullMember() throws JsonMappingException {
 assertNull(propertyBuilder.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyWithDefaultValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();testObjNode.put("testProp", "default");
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", any(AnnotatedMember.class), mapper.getTypeFactory().constructType(String.class))).thenReturn("default");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, mapper.getTypeFactory().constructType(String.class), null, null, null, false, "default");
 assertEquals(expectedWriter, writer);
 }

 @Test
 public void testGetPropertyDefaultValue_NoDefaultValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getDefaultBean()).thenReturn(testObj);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", any(AnnotatedMember.class), mapper.getTypeFactory().constructType(String.class))).thenReturn(null);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNull(writer.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_DefaultBeanIsNull() throws JsonMappingException {
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getDefaultBean()).thenReturn(null);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", any(AnnotatedMember.class), mapper.getTypeFactory().constructType(String.class))).thenReturn("default");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, mapper.getTypeFactory().constructType(String.class), null, null, null, false, "default");
 assertEquals(expectedWriter, writer);
 }

 @Test
 public void testGetPropertyDefaultValue_PropertyWithEmptyValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();testObjNode.put("testProp", "");
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class))).thenReturn("");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, mapper.getTypeFactory().constructType(String.class), null, null, null, false, "");
 assertEquals(expectedWriter, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_PropertyWithNullValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();testObjNode.put("testProp", null);
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getDefaultBean()).thenReturn(testObj);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class))).thenReturn(null);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNull(writer.getValueToSuppress());
 }

 @Test
 public void testGetPropertyDefaultValue_ArrayPropertyWithEmptyValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();testObjNode.put("testProp", new Object(){});
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String[].class))).thenReturn(new String[0]);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, mapper.getTypeFactory().constructType(String[].class), null, null, null, false, new String[0]);
 assertEquals(expectedWriter, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_ArrayPropertyWithNullValue() throws JsonMappingException {
 ObjectNode testObjNode = mapper.createObjectNode();testObjNode.put("testProp", null);
 Object testObj = mapper.readValue(testObjNode.toString(), Object.class);
 PropertyBuilder propertyBuilderSpy = spy(propertyBuilder);
 when(propertyBuilderSpy.getDefaultBean()).thenReturn(testObj);
 when(propertyBuilderSpy.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String[].class))).thenReturn(null);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNull(writer.getValueToSuppress());
 }
}