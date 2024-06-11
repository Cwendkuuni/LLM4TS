import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> collectionType = List.class;
        Class<LinkedList<String>> linkedListType = LinkedList.class;

        // Test adding a valid mapping
        resolver.addMapping(collectionType, linkedListType);
        Class<?> mappedType = resolver._mappings.get(new ClassKey(collectionType));
        assertEquals(linkedListType, mappedType);

        // Test adding null super types
        assertThrows(IllegalArgumentException.class, () -> resolver.addMapping(null, linkedListType));

        // Test adding mapping for the same class
        assertThrows(IllegalArgumentException.class, () -> resolver.addMapping(linkedListType, linkedListType));

        // Test adding mapping for non-abstract super types
        Class<String> stringType = String.class;
        assertThrows(IllegalArgumentException.class, () -> resolver.addMapping(stringType, linkedListType));

        // Test adding mapping for non-subtype relationships
        Class<ArrayList<String>> arrayListType = ArrayList.class;
        assertThrows(IllegalArgumentException.class, () -> resolver.addMapping(collectionType, arrayListType));
    }

    @Test
    public void testFindTypeMapping() {
        // Test finding a type mapping for a valid type
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        JavaType listType = typeFromClass(List.class);
        JavaType mapping = resolver.findTypeMapping(null, listType);
        assertNotNull(mapping);
        assertEquals(LinkedList.class, mapping.getRawClass());

        // Test finding a type mapping for a null type
        assertNull(resolver.findTypeMapping(null, null));

        // Test finding a type mapping for an unregistered type
        JavaType unregisteredType = typeFromClass(HashMap.class);
        assertNull(resolver.findTypeMapping(null, unregisteredType));
    }

    private JavaType typeFromClass(Class<?> clazz) {
        return AbstractTypeResolver.constructType(clazz);
    }
}