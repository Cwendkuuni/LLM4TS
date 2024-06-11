import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setUp() {
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testInitNullIdType() {
 assertThrows(IllegalArgumentException.class, () -> {
 builder.init(null, null);
 });
 }

 @Test
 public void testInitNullIdResolver() {
 TypeIdResolver idResolver = null;
 assertThrows(IllegalArgumentException.class, () -> {
 builder.init(JsonTypeInfo.Id.CLASS, idResolver);
 });
 }

 @Test
 public void testInitClassIdType() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 }

 @Test
 public void testInitMinimalClassIdType() {
 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder.getIdType());
 }

 @Test
 public void testInitNameIdType() {
 builder.init(JsonTypeInfo.Id.NAME, null);
 assertEquals(JsonTypeInfo.Id.NAME, builder.getIdType());
 }

 @Test
 public void testInitNoneIdType() {
 builder.init(JsonTypeInfo.Id.NONE, null);
 assertNull(builder.getIdType());
 }

 @Test
 public void testInitCustomIdTypeAndResolver() {
 TypeIdResolver idResolver = null;
 builder.init(JsonTypeInfo.Id.CUSTOM, idResolver);
 assertNull(builder.getIdType());
 assertNull(builder.getIdResolver());
 }

 @Test
 public void testInitCustomIdTypeAndResolverWithCustomResolver() {
 TypeIdResolver idResolver = new TypeIdResolver() {
 @Override
 public IdType idFromValue(Object value) {
 return null;
 }

 @Override
 public IdType idFromValues(Object idValue, Class<?> expectedType) {
 return null;
 }

 @Override
 public TypeId idFromParameter(Method method, JavaType parameterType) {
 return null;
 }

 @Override
 public TypeId idFromContext(DeserializationContext ctxt, BeanDescription beanDesc) {
 return null;
 }
 };
 builder.init(JsonTypeInfo.Id.CUSTOM, idResolver);
 assertNotNull(builder.getIdType());
 assertNotNull(builder.getIdResolver());
 }

 @Test
 public void testBuildTypeSerializerNullConfig() {
 assertNull(builder.buildTypeSerializer(null, new ClassType(Integer.class), null));
 }

 @Test
 public void testBuildTypeSerializerJsonTypeInfoIdNone() {
 assertNull(builder.buildTypeSerializer(new MapperConfig(), new ClassType(Integer.class), null));
 }

 @Test
 public void testBuildTypeSerializerJsonTypeInfoIdClass() {
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Integer.class), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerJsonTypeInfoIdWrapperArray() {
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Integer[].class), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerJsonTypeInfoIdProperty() {
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Person.class), null);
 assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerJsonTypeInfoIdWrapperObject() {
 builder.include(JsonTypeInfo.As.WRAPPER_OBJECT);
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Person.class), null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerJsonTypeInfoIdExternalProperty() {
 builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Person.class), null);
 }

 @Test
 public void testBuildTypeSerializerJsonTypeInfoIdExistingProperty() {
 builder.include(JsonTypeInfo.As.EXISTING_PROPERTY);
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), new ClassType(Person.class), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeDeserializerNullConfig() {
 assertNull(builder.buildTypeDeserializer(null, new ClassType(Integer.class), null));
 }

 @Test
 public void testBuildTypeDeserializerJsonTypeInfoIdNone() {
 assertNull(builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Integer.class), null));
 }

 @Test
 public void testBuildTypeDeserializerJsonTypeInfoIdClass() {
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Integer.class), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerJsonTypeInfoIdWrapperArray() {
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Integer[].class), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerJsonTypeInfoIdProperty() {
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Person.class), null);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerJsonTypeInfoIdWrapperObject() {
 builder.include(JsonTypeInfo.As.WRAPPER_OBJECT);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Person.class), null);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerJsonTypeInfoIdExternalProperty() {
 builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Person.class), null);
 }

 @Test
 public void testBuildTypeDeserializerJsonTypeInfoIdExistingProperty() {
 builder.include(JsonTypeInfo.As.EXISTING_PROPERTY);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), new ClassType(Person.class), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testSetInclude() {
 builder.include(JsonTypeInfo.As.WRAPPER_ARRAY);
 assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
 }

 @Test
 public void testSetIncludeNull() {
 assertThrows(IllegalArgumentException.class, () -> {
 builder.include(null);
 });
 }

 @Test
 public void testSetTypeProperty() {
 builder.typeProperty("typeProperty");
 assertEquals("typeProperty", builder.getTypeProperty());
 }

 @Test
 public void testSetDefaultImpl() {
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testSetTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }
}