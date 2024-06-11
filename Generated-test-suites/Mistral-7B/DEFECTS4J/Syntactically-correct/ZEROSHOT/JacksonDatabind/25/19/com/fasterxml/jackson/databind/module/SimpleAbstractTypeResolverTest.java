import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaTypeCollection = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType javaTypeLinkedList = mapper.getTypeFactory().constructType(LinkedList.class);

 JavaType actual = resolver.findTypeMapping(config, javaTypeCollection);
 JavaType expected = javaTypeLinkedList;
 assertEquals("Find type mapping for Collection", expected.getRawClass(), actual.getRawClass());

 JavaType actual2 = resolver.findTypeMapping(config, javaTypeLinkedList);
 JavaType expected2 = javaTypeLinkedList;
 assertEquals("Find type mapping for LinkedList", expected2.getRawClass(), actual2.getRawClass());

 JavaType actual3 = resolver.findTypeMapping(config, JavaType.constructType(String[].class));
 JavaType expected3 = null;
 assertNull("Find type mapping for String[]", expected3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 resolver.addMapping(Collection.class, Collection.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidTypeRelationship() {
 resolver.addMapping(Collection.class, String.class);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaTypeAbstractList = mapper.getTypeFactory().constructType(AbstractList.class);

 JavaType actual = resolver.resolveAbstractType(config, javaTypeAbstractList);
 JavaType expected = null;
 assertEquals("Resolve AbstractType for AbstractList", expected.getRawClass(), actual.getRawClass());
 }
}