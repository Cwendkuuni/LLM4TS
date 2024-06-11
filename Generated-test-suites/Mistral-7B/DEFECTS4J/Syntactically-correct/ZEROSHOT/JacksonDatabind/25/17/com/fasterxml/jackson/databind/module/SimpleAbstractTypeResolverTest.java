import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;
import com.fasterxml.jackson.module.java8.CollectionTypeFactory;
import com.fasterxml.jackson.module.java8.Java8Module;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new Java8Module());
 TypeFactory typeFactory = objectMapper.getTypeFactory();

 JavaType collectionType = typeFactory.constructType(ArrayList.class);
 JavaType expectedType = typeFactory.constructType(LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new ObjectMapper()), collectionType);

 assertEquals(expectedType, actualType);

 JavaType listType = typeFactory.constructType(ArrayList.class);
 JavaType anotherExpectedType = typeFactory.constructType(LinkedList.class);
 JavaType anotherActualType = resolver.findTypeMapping(new DeserializationConfig(new ObjectMapper()), listType);

 assertEquals(anotherExpectedType, anotherActualType);

 assertNull(resolver.findTypeMapping(new DeserializationConfig(new ObjectMapper()), String.class));
 }

 @Test(expected = RuntimeException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new Java8Module());
 TypeFactory typeFactory = objectMapper.getTypeFactory();

 JavaType collectionType = typeFactory.constructType(ArrayList.class);
 JavaType expectedType = typeFactory.constructType(LinkedList.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new ObjectMapper()), collectionType);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType collectionType = Types.constructType(ArrayList.class);
 JavaType expectedType = null;
 JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(new ObjectMapper()), collectionType);

 assertEquals(expectedType, actualType);
 }
}