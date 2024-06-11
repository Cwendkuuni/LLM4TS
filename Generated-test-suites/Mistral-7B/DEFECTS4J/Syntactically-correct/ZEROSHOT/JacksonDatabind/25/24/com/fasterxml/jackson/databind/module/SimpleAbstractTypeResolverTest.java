import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
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
 JavaType javaType = JavaType.construct(Collection.class, int.class);
 JavaType expectedType = JavaType.construct(LinkedList.class, int.class);
 JavaType actualType = resolver.findTypeMapping(null, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 assertNull(resolver.findTypeMapping(null, null));
 }

 @Test
 public void testFindTypeMappingWithInvalidType() {
 JavaType javaType = JavaType.construct(Integer.class);
 assertNull(resolver.findTypeMapping(null, javaType));
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = JavaType.construct(Collection.class);
 assertNull(resolver.resolveAbstractType(null, javaType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(Collection.class, ArrayList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSuperType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(ArrayList.class, LinkedList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Integer.class, LinkedList.class);
 }
}