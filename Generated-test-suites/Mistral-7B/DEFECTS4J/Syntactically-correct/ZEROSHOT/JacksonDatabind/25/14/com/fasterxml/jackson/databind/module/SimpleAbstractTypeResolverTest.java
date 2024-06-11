import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private DeserializationConfig config;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 config = mock(DeserializationConfig.class);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddMapping() {
 Class<?> collectionClass = Collection.class;
 Class<? extends Collection<?>> linkedListClass = LinkedList.class;
 resolver.addMapping(collectionClass, linkedListClass);

 ClassKey collectionKey = new ClassKey(collectionClass);
 Class<?> mappedClass = resolver.getMapping(collectionKey);

 assertNotNull(mappedClass);
 assertEquals(linkedListClass, mappedClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingToSameClass() {
 Class<?> collectionClass = Collection.class;
 resolver.addMapping(collectionClass, collectionClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithIncorrectSubtype() {
 Class<?> collectionClass = Collection.class;
 Class<? extends Number> integerClass = Integer.class;
 resolver.addMapping(collectionClass, integerClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 Class<?> listClass = List.class;
 Class<? extends List<?>> linkedListClass = LinkedList.class;
 resolver.addMapping(listClass, linkedListClass);

 JavaType listType = new TypeReference<List<String>>() {}.getType(config);
 JavaType linkedListType = resolver.findTypeMapping(config, listType);

 assertNull(linkedListType);
 }

 @Test
 public void testFindTypeMapping() {
 Class<?> collectionClass = Collection.class;
 Class<? extends Collection<?>> linkedListClass = LinkedList.class;
 resolver.addMapping(collectionClass, linkedListClass);

 JavaType collectionType = new TypeReference<Collection<String>>() {}.getType(config);
 JavaType linkedListType = resolver.findTypeMapping(config, collectionType);

 assertNotNull(linkedListType);
 assertEquals(LinkedList.class, linkedListType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNullInput() {
 JavaType nullType = null;
 JavaType mappedType = resolver.findTypeMapping(config, nullType);

 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractType() {
 Class<?> abstractListClass = AbstractList.class;
 JavaType listType = new TypeReference<List<String>>() {}.getType(config);
 JavaType mappedType = resolver.resolveAbstractType(config, listType);

 assertNull(mappedType);
 }
}