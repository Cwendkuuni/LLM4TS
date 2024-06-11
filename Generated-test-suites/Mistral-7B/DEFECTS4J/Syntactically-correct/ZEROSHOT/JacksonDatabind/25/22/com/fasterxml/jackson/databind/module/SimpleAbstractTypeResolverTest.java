import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType collectionType = JavaType.constructRawType(Collection.class, String.class);
 JavaType expectedType = JavaType.constructRawType(LinkedList.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, collectionType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingGivenInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType collectionType = JavaType.constructRawType(Collection.class, String.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, Integer.class);
 JavaType actualType = resolver.findTypeMapping(config, collectionType);
 assertNull(actualType);
 }

 @Test
 public void testFindTypeMappingWithAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractType = JavaType.constructRawType(AbstractList.class, String.class);
 JavaType expectedType = JavaType.constructRawType(LinkedList.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, abstractType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithNonAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType concreteType = JavaType.constructRawType(ArrayList.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, concreteType);
 assertNull(actualType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractType = JavaType.constructRawType(AbstractList.class, String.class);
 JavaType expectedType = null;
 JavaType actualType = resolver.resolveAbstractType(config, abstractType);
 assertEquals(expectedType, actualType);
 }
}