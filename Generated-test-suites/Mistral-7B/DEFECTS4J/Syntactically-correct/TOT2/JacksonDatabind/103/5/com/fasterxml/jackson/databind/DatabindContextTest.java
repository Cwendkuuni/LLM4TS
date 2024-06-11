import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonDeserializer.None;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Test cases for {@link DatabindContext} class.
 */
public class DatabindContextTest {

 private DeserializationContext context;
 private SerializerProvider serializerProvider;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 context = new MapperFeatureContext();
 serializerProvider = new MapperFeatureSerializerProvider();
 mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapper.setSerializationFeatures( Arrays.asList(SerializationFeature.FAIL_ON_EMPTY_BEANS) );
 mapper.setConfig(context);
 }

 /**
 * Test for {@link DatabindContext#getConfig()} method.
 */
 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertEquals(MapperFeatureContext.class, config.getClass());
 }

 /**
 * Test for {@link DatabindContext#getAnnotationIntrospector()} method.
 */
 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
 assertNull(annotationIntrospector);
 }

 /**
 * Test for {@link DatabindContext#isEnabled(MapperFeature)} method.
 */
 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS_AS_BEANS));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
 }

 /**
 * Test for {@link DatabindContext#canOverrideAccessModifiers()} method.
 */
 @Test
 public void testCanOverrideAccessModifiers() {
 assertFalse(context.canOverrideAccessModifiers());
 }

 /**
 * Test for {@link DatabindContext#getActiveView()} method.
 */
 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 /**
 * Test for {@link DatabindContext#getLocale()} method.
 */
 @Test
 public void testGetLocale() {
 assertEquals(Locale.getDefault(), context.getLocale());
 }

 /**
 * Test for {@link DatabindContext#getTimeZone()} method.
 */
 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 /**
 * Test for {@link DatabindContext#getDefaultPropertyFormat(Class)} method.
 */
 @Test
 public void testGetDefaultPropertyFormat() throws IOException {
 JavaType javaType = context.getTypeFactory().constructType(String.class);
 JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(javaType);
 assertEquals(JsonFormat.Value.any(), defaultPropertyFormat);
 }

 /**
 * Test for {@link DatabindContext#getAttribute(Object)} method.
 */
 @Test
 public void testGetAttribute() {
 context.setAttribute("test", "value");
 Object attribute = context.getAttribute("test");
 assertEquals("value", attribute);
 }

 /**
 * Test for {@link DatabindContext#setAttribute(Object, Object)} method.
 */
 @Test
 public void testSetAttribute() {
 context.setAttribute("test", "value");
 assertEquals("value", context.getAttribute("test"));
 context.setAttribute("test", null);
 assertNull(context.getAttribute("test"));
 }

 /**
 * Test for {@link DatabindContext#constructType(Type)} method.
 */
 @Test
 public void testConstructType() throws Exception {
 Type objectType = new TypeReference<Object>() {}.getType();
 JavaType javaType = context.constructType(objectType);
 assertNotNull(javaType);
 }

 /**
 * Test for {@link DatabindContext#constructSpecializedType(JavaType, Class)} method.
 */
 @Test
 public void testConstructSpecializedType() throws Exception {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subtype = context.constructSpecializedType(baseType, String[].class);
 assertEquals(String[].class, subtype.getRawClass());
 }

 /**
 * Test for {@link DatabindContext#resolveSubType(JavaType, String)} method.
 */
 @Test
 public void testResolveSubType() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 assertNull(context.resolveSubType(baseType, "List<String>"));
 }

 /**
 * Test for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)} method.
 */
 @Test
 public void testInvalidTypeIdException() {
 assertThrows(JsonMappingException.class, () -> context.invalidTypeIdException(null, "test", ""));
 }

 /**
 * Test for {@link DatabindContext#getTypeFactory()} method.
 */
 @Test
 public void testgetTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 JavaType javaType = typeFactory.constructType(String.class);
 assertEquals(String.class, javaType.getRawClass());
 }

 /**
 * Test for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)} method.
 */
 @JsonAutoDetect
 @JsonSubTypes({ @JsonSubTypes.Type(value = Child.class, name = "childType") })
 static class Parent {
 private String id;
 private Child child;
 }

 static class Child {
 private int id;
 }

 @Test
 public void testObjectIdGeneratorInstance() throws Exception {
 Parent parent = new Parent();
 ObjectIdInfo objectIdInfo = new ObjectIdGenerator.IdInfoImpl<>(Parent.class, "id");
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(context.constructValueType(Parent.class), objectIdInfo);
 assertEquals(String.class, objectIdGenerator.getClass());
 }

 /**
 * Test for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)} method.
 */
 @Test
 public void testObjectIdResolverInstance() throws Exception {
 Parent parent = new Parent();
 ObjectIdInfo objectIdInfo = new ObjectIdGenerator.IdInfoImpl<>(Parent.class, "id");
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(context.constructValueType(Parent.class), objectIdInfo);
 assertEquals(ObjectIdResolver.class, objectIdResolver.getClass());
 }

 /**
 * Test for {@link DatabindContext#converterInstance(Annotated, Object)} method.
 */
 @JsonAutoDetect
 @JsonSubTypes({ @JsonSubTypes.Type(value = Child.class, name = "childType") })
 static class ParentConverter {
 @JsonDeserializer(content = Child.class)
 static Converter<Object, Child> converter = new Converter.Std() {};
 }

 static class Child {
 private int id;
 }

 @Test
 public void testConverterInstance() throws Exception {
 Parent parent = new Parent();
 Parent annotated = context.constructValueType(Parent.class);
 ParentConverter converterDef = ParentConverter.class;
 Converter<Object, Child> converter = context.converterInstance(annotated, converterDef);
 assertEquals(converterDef, converter);
 }

 /**
 * Test for {@link DatabindContext#reportBadDefinition(JavaType, String)} method.
 */
 @Test(expected = JsonMappingException.class)
 public void testReportBadDefinition() throws Exception {
 context.reportBadDefinition(context.getTypeFactory().constructType(Integer.class), "Invalid definition");
 }
}