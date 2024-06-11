import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        SimpleAbstractTypeResolver resolverInstance = resolver.addMapping(listClass, linkedListClass);
        Class<? extends List> mappedClass = resolverInstance._mappings.get(new ClassKey(listClass));

        assertEquals(LinkedList.class, mappedClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<LinkedList> linkedListClass = LinkedList.class;

        resolver.addMapping(linkedListClass, linkedListClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToNonAbstractClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<String> stringClass = String.class;

        resolver.addMapping(stringClass, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSuperTypeSubTypeRelationship() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        resolver.addMapping(stringClass, integerClass);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);

        JavaType resolvedJavaType = resolver.findTypeMapping(null, javaType);

        assertEquals(LinkedList.class, resolvedJavaType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);

        JavaType resolvedJavaType = resolver.resolveAbstractType(null, javaType);

        assertNull(resolvedJavaType);
    }

    @Test
    public void testFindTypeMappingWithEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);

        JavaType resolvedJavaType = resolver.findTypeMapping(null, javaType);

        assertNull(resolvedJavaType);
    }

    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingWithNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;

        JavaType resolvedJavaType = resolver.findTypeMapping(null, javaType);
    }
}