import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder builder;
 private Object defaultBean;

 @Before
 public void setup() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(getClass());
 SerializationConfig config = mapper.getSerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 this.builder = propertyBuilder;
 this.defaultBean = propertyBuilder.getDefaultBean();
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = builder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testBuildWriter_NullProperties() throws JsonMappingException {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = null;
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNull(bpw);
 }

 @Test
 public void testBuildWriter_ValidProperties() throws JsonMappingException {
 Class<?> clazz = getClass();
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(clazz, "testProperty", String.class, null, null, null, null);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> ser = mapper.findValueSerializer(String.class, null);
 TypeSerializer typeSer = mapper.getTypeSerializer();
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(bpw);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_Exception() throws JsonMappingException {
 Class<?> clazz = getClass();
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(clazz, "testProperty", String.class, null, null, null, null);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> ser = mapper.findValueSerializer(String.class, null);
 TypeSerializer typeSer = mapper.getTypeSerializer();
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(bpw);
 BeanPropertyWriter.Marker marker = new BeanPropertyWriter.Marker();
 bpw.serializeValue(marker, null, null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 when(a.getDeclaredAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JavaType secondary = builder.findSerializationType(a, false, declaredType);
 assertEquals(String.class, secondary.getRawClass());
 }

 @Test
 public void testFindSerializationType_SuperType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 when(a.getDeclaredAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
 JavaType declaredType = mapper.getTypeFactory().constructType(Number.class);
 JavaType secondary = builder.findSerializationType(a, false, declaredType);
 assertNotNull(secondary);
 assertTrue(Number.class.isAssignableFrom(secondary.getRawClass()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_InvalidType() throws JsonMappingException {
 Annotated a = mock(Annotated.class);
 when(a.getDeclaredAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
 JavaType declaredType = mapper.getTypeFactory().constructType(Integer.class);
 JavaType secondary = builder.findSerializationType(a, false, declaredType);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetDefaultBean() {
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonMappingException {
 String propName = "testProperty";
 String defaultValue = "defaultValue";
 BeanPropertyWriter.Marker marker = new BeanPropertyWriter.Marker();
 Object propertyDefaultValue = builder.getPropertyDefaultValue(propName, marker, getClass());
 assertNotNull(propertyDefaultValue);
 assertEquals(defaultValue, propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_Null() throws JsonMappingException {
 String propName = "testProperty";
 BeanPropertyWriter.Marker marker = new BeanPropertyWriter.Marker();
 Object propertyDefaultValue = builder.getPropertyDefaultValue(propName, marker, getType());
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_NonDefault() throws JsonMappingException {
 String propName = "testProperty";
 Object defaultBean = builder.getDefaultBean();
 BeanPropertyWriter.Marker marker = new BeanPropertyWriter.Marker();
 Object propertyDefaultValue = builder.getPropertyDefaultValue(propName, marker, getClass());
 assertNotNull(propertyDefaultValue);
 assertNotEquals(defaultBean, propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValue() {
 Class<?> intType = int.class;
 Object intDefaultValue = builder.getDefaultValue(intType);
 assertEquals(0, intDefaultValue);
 }

 @Test
 public void testGetDefaultValue_String() {
 Class<?> stringType = String.class;
 Object stringDefaultValue = builder.getDefaultValue(stringType);
 assertEquals("", stringDefaultValue);
 }

 @Test
 public void testGetDefaultValue_ContainerType() {
 JavaType containerType = mapper.getTypeFactory().constructType(ArrayList.class);
 Object containerDefaultValue = builder.getDefaultValue(containerType);
 assertTrue(containerDefaultValue instanceof BeanPropertyWriter.Marker);
 }

 @Test
 public void testGetDefaultValue_ReferenceType() {
 Class<?> referenceType = String[].class;
 Object referenceDefaultValue = builder.getDefaultValue(referenceType);
 assertTrue(referenceDefaultValue instanceof BeanPropertyWriter.Marker);
 }

 @Test
 public void testDefaultValueWithOverride() throws JsonMappingException {
 JsonInclude jsonIncludeAnnotation = new JsonInclude(JsonInclude.Include.NON_DEFAULT);
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(String[].class);
 beanDesc.addAnnotation(jsonIncludeAnnotation);
 PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 Object defaultBean = propertyBuilder.getDefaultBean();
 String defaultValue = "[defaultValue]";
 BeanPropertyWriter.Marker marker = new BeanPropertyWriter.Marker();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("length", marker, String[].class);
 assertEquals(defaultValue, propertyDefaultValue);
 }
}