import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

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
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 resolver.addMapping(Collection.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidSuperType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidSubType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType superType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType subType = TypeFactory.defaultInstance().constructType(String.class);
 resolver.addMapping(superType, subType);
 JavaType javaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType actualType = resolver.resolveAbstractType(config, javaType);
 assertNull(actualType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolveAbstractTypeWithInvalidType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType actualType = resolver.resolveAbstractType(config, javaType);
 fail("Expected IllegalStateException");
 }
}