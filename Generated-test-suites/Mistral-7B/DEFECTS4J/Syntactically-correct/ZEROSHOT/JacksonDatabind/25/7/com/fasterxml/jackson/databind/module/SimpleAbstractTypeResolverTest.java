import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactoryBuilder;
import com.fasterxml.jackson.databind.type.TypeKey;
import com.fasterxml.jackson.databind.type.TypeReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);

 objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule().setAbstractTypeResolver(resolver));
 }

 @Test
 public void testFindTypeMapping() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), javaType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingCustomClassIds() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(new TypeReference<Collection<String>>(){}.getType(), ArrayList.class);

 JavaType javaType = objectMapper.getTypeFactory().constructTypeId(Collection.class, new ClassIdType("MyTypeId"));
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
 assertNotNull(resolvedType);
 assertEquals(ArrayList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithTypeFactory() {
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 JavaType javaType = typeFactory.constructType(List.class);
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithTypeReference() {
 JavaType javaType = new TypeReference<Collection<String>>(){}.getType();
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithTypeKey() {
 TypeKey typeKey = TypeKey.collectionType(String.class, List.class);
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), ObjectMapper.getDefaultInstance().getTypeFactory().constructType(typeKey));
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithGenericType() {
 JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<Map<String, List<String>>>() {}.getType());
 JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
 assertNotNull(resolvedType);
 assertEquals(Map.class, resolvedType.getRawClass());
 assertEquals(LinkedList.class, resolvedType.getGenericType().getType(0).getRawClass());
 }
}