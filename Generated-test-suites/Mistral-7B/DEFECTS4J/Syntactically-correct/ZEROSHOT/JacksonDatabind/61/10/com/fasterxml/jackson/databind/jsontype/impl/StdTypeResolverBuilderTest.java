import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
 public void testDefaultConstructor() {
 assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
 assertNull(builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, noTypeInfoBuilder.getIdType());
 assertNull(noTypeInfoBuilder.getCustomIdResolver());
 assertNull(noTypeInfoBuilder.getTypeProperty());
 assertFalse(noTypeInfoBuilder.isTypeIdVisible());
 assertNull(noTypeInfoBuilder.getDefaultImpl());
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = new ClassNameIdResolver(Object.class, null);
 builder.init(idType, idRes);
 assertEquals(idType, builder.getIdType());
 assertEquals(idRes, builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIdType() {
 builder.init(null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBuildTypeSerializer() {
 ClassIdType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 subtypes.add(new SimpleName("MyIntType"));
 JacksonAnnotationIntType type = new JacksonAnnotationIntType();
 type.setId(JsonTypeInfo.Id.PROPERTY);
 type.setInclude(JsonTypeInfo.As.PROPERTY);
 TypeFactory typeFactory = new TypeFactoryBuilder()
         .withoutDefaultTyping()
         .build();
MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector(new AnnotationIntrospector.Default(), null), typeFactory);
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerNullIdType() {
 builder.init(null, null);
 builder.buildTypeSerializer(null, null, null);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testBuildTypeDeserializer() {
 ClassIdType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 subtypes.add(new SimpleName("MyIntType"));
 JacksonAnnotationIntType type = new JacksonAnnotationIntType();
 type.setId(JsonTypeInfo.Id.PROPERTY);
 type.setInclude(JsonTypeInfo.As.PROPERTY);
 TypeFactory typeFactory = new TypeFactoryBuilder()
         .withoutDefaultTyping()
         .build();
 MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector(new AnnotationIntrospector.Default(), null), typeFactory);
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerNullIdType() {
 builder.init(null, null);
 builder.buildTypeDeserializer(null, null, null);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testInclusion() {
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
 builder.inclusion(includeAs);
 assertEquals(includeAs, builder.getIncludeAs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusionNullIncludeAs() {
 builder.inclusion(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testTypeProperty() {
 String typeProperty = "typeProperty";
 builder.typeProperty(typeProperty);
 assertEquals(typeProperty, builder.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 Class<Integer> defaultImpl = Integer.class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 boolean typeIdVisible = true;
 builder.typeIdVisibility(typeIdVisible);
 assertEquals(typeIdVisible, builder.isTypeIdVisible());
 }

 @Test
 public void testGetDefaultImpl() {
 Class<Integer> defaultImpl = Integer.class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder.getDefaultImpl());
 }

 @Test
 public void testIsTypeIdVisible() {
 boolean typeIdVisible = true;
 builder.typeIdVisibility(typeIdVisible);
 assertEquals(typeIdVisible, builder.isTypeIdVisible());
 }
}

class JacksonAnnotationIntType extends StdType {
 @JsonSerialize(using = ToStringSerializer.class)
 @JsonDeserialize(using = FromStringDeserializer.class)
 @JsonTypeInfo(use = JsonTypeInfo.Id.PROPERTY, include = JsonTypeInfo.As.PROPERTY, property = "type")
 public static class MyIntType extends Number {
 // Empty class
 }
}