import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 builder = new StdTypeResolverBuilder();
 mapper = new MapperFactory(new AnnotationIntrospector()).builder()
 .init(JsonTypeInfo.Id.CLASS, null)
 .build();
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idResolver = new TypeNameIdResolver(mapper, null, null, true, false);
 builder.init(idType, idResolver);
 assertEquals(idType, builder._idType);
 assertEquals(idResolver, builder._customIdResolver);
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(noTypeInfoBuilder._idType);
 assertNull(noTypeInfoBuilder._customIdResolver);
 }

 @Test
 public void testBuildTypeSerializer() {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 TypeSerializer result = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
 assertNull(result);

 idType = JsonTypeInfo.Id.PROPERTY;
 idResolver = new TypeNameIdResolver(mapper, baseType, subtypes, true, false);
 result = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithInvalidIncludeAs() {
 idType = JsonTypeInfo.Id.NONE;
 idResolver = new TypeNameIdResolver(mapper, null, null, true, false);
 builder.init(idType, idResolver);
 builder.include(JsonTypeInfo.As.WRAPPER_ARRAY);
 builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null);
 }

 @Test
 public void testBuildTypeDeserializer() {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = null;
 TypeDeserializer result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
 assertNull(result);

 idType = JsonTypeInfo.Id.PROPERTY;
 idResolver = new TypeNameIdResolver(mapper, baseType, subtypes, false, true);
 result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithInvalidIncludeAs() {
 idType = JsonTypeInfo.Id.NONE;
 idResolver = new TypeNameIdResolver(mapper, null, null, false, true);
 builder.init(idType, idResolver);
 builder.include(JsonTypeInfo.As.WRAPPER_ARRAY);
 builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null);
 }

 @Test
 public void testInclusion() {
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
 builder.inclusion(includeAs);
 assertEquals(includeAs, builder._includeAs);
 }

 @Test
 public void testTypeProperty() {
 String typeIdPropName = "typeId";
 builder.typeProperty(typeIdPropName);
 assertEquals(typeIdPropName, builder._typeProperty);
 }

 @Test
 public void testDefaultImpl() {
 Class<?> defaultImpl = String[].class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder._defaultImpl);
 }

 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

 @Test
 public void testCustomIdResolver() {
 TypeIdResolver customIdResolver = new TypeNameIdResolver(mapper, null, null, true, false);
 builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);
 assertEquals(customIdResolver, builder._customIdResolver);
 }
}