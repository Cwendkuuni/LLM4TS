import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {
    SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        resolver.addMapping(JavaType.getType(listClass), linkedListClass);
        Class<?> resolvedType = resolver._mappings.get(new ClassKey(listClass.getRawType()));
        assertNotNull(resolvedType);
        assertEquals(linkedListClass, resolvedType);
    }

    // Test when the type is already mapped
    @Test
    public void testFindTypeMappingAlreadyMapped() {
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(JavaType.getType(listClass), linkedListClass);
        JavaType javaType = JavaType.construct(List.class, new TypeFactory());
        JavaType expectedType = JavaType.constructParameterizedType(LinkedList.class, Object.class);
        JavaType actualType = resolver.findTypeMapping(null, javaType);
        assertEquals(expectedType, actualType);
    }

    // Test when the type is not abstract
    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingNonAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(Integer.class, new TypeFactory());
        resolver.findTypeMapping(null, javaType);
    }

    // Test when the superclass and subclass have no subtype relationship
    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingNoSubtypeRelationship() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        resolver.addMapping(JavaType.getType(stringClass), integerClass);
        JavaType javaType = JavaType.construct(String.class, new TypeFactory());
        resolver.findTypeMapping(null, javaType);
    }

    // Test when the deserialization config is null
    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingNullConfig() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(List.class, new TypeFactory());
        resolver.findTypeMapping(null, javaType);
    }

    // Test when the type is null
    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;
        resolver.findTypeMapping(null, javaType);
    }

    // Test when the deserialization config is null for resolveAbstractType
    @Test
    public void testResolveAbstractTypeNullConfig() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(List.class, new TypeFactory());
        JavaType expectedType = null;
        JavaType actualType = resolver.resolveAbstractType(null, javaType);
        assertEquals(expectedType, actualType);
    }

    // Test when the type is null for resolveAbstractType
    @Test
    public void testResolveAbstractTypeNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;
        JavaType expectedType = null;
        JavaType actualType = resolver.resolveAbstractType(null, javaType);
        assertEquals(expectedType, actualType);
    }
}