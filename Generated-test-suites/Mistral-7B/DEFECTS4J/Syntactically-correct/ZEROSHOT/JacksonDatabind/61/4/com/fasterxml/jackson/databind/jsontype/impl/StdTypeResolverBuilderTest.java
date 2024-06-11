import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private MapperFactory mapperFactory;

 @Before
 public void setUp() {
 mapperFactory = new MapperFactory();
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, noTypeInfoBuilder._idType);
 assertNull(noTypeInfoBuilder._customIdResolver);
 assertNull(noTypeInfoBuilder._typeProperty);
 assertNull(noTypeInfoBuilder._typeIdVisible);
 assertNull(noTypeInfoBuilder._defaultImpl);
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 builder.init(idType, null);
 assertEquals(idType, builder._idType);
 assertNull(builder._customIdResolver);
 assertEquals("", builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);

 JsonTypeInfo.Id idTypeName = JsonTypeInfo.Id.NAME;
 TypeIdResolver idResolver = mock(TypeIdResolver.class);
 builder.init(idTypeName, idResolver);
 assertEquals(idTypeName, builder._idType);
 assertEquals(idResolver, builder._customIdResolver);
 assertEquals("", builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
}

 @Test
 public void testBuildTypeSerializer() {
 // Test with null config and baseType
 assertNull(builder.buildTypeSerializer(null, null, null));

 // Test with valid config, baseType and subtypes
 MapperConfig config = mapperFactory.getConfig();
 config.setSerializationInclusion(JsonInclude.Include.ALL);
 JavaType baseType = mapperFactory.constructType(String[].class);
 Collection<NamedType> subtypes = new ArrayList<>();
 NamedType namedType1 = new StandardNamedType(Integer.class);
 NamedType namedType2 = new StandardNamedType(Double.class);
 subtypes.add(namedType1);
 subtypes.add(namedType2);
 ObjectMapper objectMapper = new ObjectMapper(config);
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 TypeIdResolver idResolver = new ClassNameIdResolver(baseType, typeFactory);
 builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, idResolver);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(typeSerializer);
 // Test for TypeSerializer implementation
 assertEquals(AsArrayTypeSerializer.class, typeSerializer.getClass());
 }

 @Test
 public void testBuildTypeDeserializer() {
 // Test with null config and baseType
 assertNull(builder.buildTypeDeserializer(null, null, null));

 // Test with valid config, baseType and subtypes
 MapperConfig config = mapperFactory.getConfig();
 config.setSerializationInclusion(JsonInclude.Include.ALL);
 JavaType baseType = mapperFactory.constructType(String[].class);
 Collection<NamedType> subtypes = new ArrayList<>();
 NamedType namedType1 = new StandardNamedType(Integer.class);
 NamedType namedType2 = new StandardNamedType(Double.class);
 subtypes.add(namedType1);
 subtypes.add(namedType2);
 ObjectMapper objectMapper = new ObjectMapper(config);
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 TypeIdResolver idResolver = new ClassNameIdResolver(baseType, typeFactory);
 builder.init(JsonTypeInfo.Id.PROPERTY, idResolver);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(typeDeserializer);
 // Test for TypeDeserializer implementation
 assertEquals(AsPropertyTypeDeserializer.class, typeDeserializer.getClass());
 }

 @Test
 public void testInclusion() {
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
 }

 @Test
 public void testTypeProperty() {
 builder.typeProperty("typeProperty");
 assertEquals("typeProperty", builder._typeProperty);
 }

 @Test
 public void testDefaultImpl() {
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder._defaultImpl);
 }

 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

}