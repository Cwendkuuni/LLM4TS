import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();

 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);

 assertEquals(LinkedList.class, resolvedType.getRawClass());
 JavaType javaType2 = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType resolvedType2 = resolver.findTypeMapping(config, javaType2);

 assertEquals(LinkedList.class, resolvedType2.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNull() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = null;
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);

 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithInvalidMapping() {
 SimpleAbstractTypeResolver resolverInvalid = new SimpleAbstractTypeResolver();
 resolverInvalid.addMapping(List.class, ArrayList.class);

 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType resolvedType = resolverInvalid.findTypeMapping(config, javaType);

 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);

 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractTypeWithMappings() {
 SimpleAbstractTypeResolver resolverMappings = new SimpleAbstractTypeResolver();
 resolverMappings.addMapping(Collection.class, LinkedList.class);
 resolverMappings.addMapping(List.class, LinkedList.class);

 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType resolvedType = resolverMappings.resolveAbstractType(config, javaType);

 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testResolveAbstractTypeWithInvalidMapping() {
 SimpleAbstractTypeResolver resolverInvalid = new SimpleAbstractTypeResolver();
 resolverInvalid.addMapping(List.class, ArrayList.class);

 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType resolvedType = resolverInvalid.resolveAbstractType(config, javaType);

 assertNull(resolvedType);
 }

 @Test
 public void testTypeResolverBuilder() {
 TypeResolverBuilder builder = new TypeResolverBuilder(new DeserializationConfig());
 JavaType javaType = TypeReference.constructType(LinkedList.class, new ClassIdType(LinkedList.class, null));
 builder.registerAbstractTypes(SimpleAbstractTypeResolver.class);
 JavaType resolvedType = builder.resolveAbstractTypes().resolve(javaType);

 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testTypeResolverBuilderWithInvalidMapping() {
 TypeResolverBuilder builder = new TypeResolverBuilder(new DeserializationConfig());
 SimpleAbstractTypeResolverResolverInvalid = new SimpleAbstractTypeResolver();
 builder.registerAbstractTypes(SimpleAbstractTypeResolverResolverInvalid.class);
 JavaType javaType = TypeReference.constructType(LinkedList.class, new ClassIdType(LinkedList.class, null));
 JavaType resolvedType = builder.resolveAbstractTypes().resolve(javaType);

 assertNull(resolvedType);
 }
}