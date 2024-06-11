import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StdTypeResolverBuilderTest {

 @Test
 public void testStdTypeResolverBuilderDefaultConstruction() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 Assertions.assertNull(builder.getIdType());
 Assertions.assertNull(builder.getIncludeAs());
 Assertions.assertNull(builder.getTypeProperty());
 Assertions.assertNull(builder.getDefaultImpl());
 Assertions.assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testStdTypeResolverBuilderInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 Assertions.assertEquals(idType, builder.getIdType());
 Assertions.assertNull(builder.getIncludeAs());
 Assertions.assertEquals("", builder.getTypeProperty());
 Assertions.assertNull(builder.getDefaultImpl());
 Assertions.assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testStdTypeResolverBuilderInitWithCustomIdResolver() {
 TypeIdResolver customIdResolver = null; // TODO: Implement a mock TypeIdResolver
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
 Assertions.assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 Assertions.assertEquals(customIdResolver, builder.getCustomIdResolver());
 Assertions.assertEquals("", builder.getTypeProperty());
 Assertions.assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testStdTypeResolverBuilderTypeProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeProperty("typePropertyName");
 Assertions.assertEquals("typePropertyName", builder.getTypeProperty());
 }

 @Test
 public void testStdTypeResolverBuilderDefaultImpl() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 Class<?> defaultImpl = Object.class;
 builder.defaultImpl(defaultImpl);
 Assertions.assertEquals(defaultImpl, builder.getDefaultImpl());
 }

 @Test
 public void testStdTypeResolverBuilderTypeIdVisibility() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 Assertions.assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testStdTypeResolverBuilderInclusion() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 As includeAs = As.PROPERTY;
 builder.inclusion(includeAs);
 Assertions.assertEquals(includeAs, builder.getIncludeAs());
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializer() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializer() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithClassIdType() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithNameIdType() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.NAME, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithCustomIdType() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeIdResolver customIdResolver = null; // TODO: Implement a mock TypeIdResolver
 builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithClassIdType() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithNameIdType() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.NAME, null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithCustomIdType() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeIdResolver customIdResolver = null; // TODO: Implement a mock TypeIdResolver
 builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerAndTypeDeserializer() throws JsonMappingException, JsonParseException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithIncludeAsWrapperArray() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(List.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(As.WRAPPER_ARRAY);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithIncludeAsProperty() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.PROPERTY);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithIncludeAsWrapperObject() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(As.WRAPPER_OBJECT);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithIncludeAsExternalProperty() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.EXTERNAL_PROPERTY);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeSerializerWithIncludeAsExistingProperty() throws JsonMappingException {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.EXISTING_PROPERTY);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithIncludeAsWrapperArray() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(List.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(As.WRAPPER_ARRAY);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithIncludeAsProperty() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.PROPERTY);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithIncludeAsWrapperObject() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(As.WRAPPER_OBJECT);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithIncludeAsExternalProperty() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.EXTERNAL_PROPERTY);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderBuildTypeDeserializerWithIncludeAsExistingProperty() throws JsonMappingException, JsonParseException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 builder.inclusion(As.EXISTING_PROPERTY);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 Assertions.assertNotNull(typeDeserializer);
 }
}