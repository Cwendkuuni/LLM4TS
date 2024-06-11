import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);

        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);

        assertEquals(collectionClass, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
        assertFalse(collectionType.isAsStatic());
    }

    @Test
    public void testConstructWithBogusSuperClass() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        JavaType elemType = Mockito.mock(JavaType.class);

        CollectionType collectionType = CollectionType.construct(collectionClass, null,
                PowerMockito.mock(JavaType.class), null, elemType);

        assertEquals(collectionClass, collectionType.getRawClass());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayIsNull(collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
        assertFalse(collectionType.isAsStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        CollectionType baseType = PowerMockito.mock(CollectionType.class);
        JavaType elemType = Mockito.mock(JavaType.class);

        when(baseType.getRawClass()).thenReturn(collectionClass);
        when(baseType.getElementType()).thenReturn(elemType);

        CollectionType newType = baseType.narrow(Collection.class);

        assertEquals(Collection.class, newType.getRawClass());
        assertEquals(baseType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(baseType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(baseType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(elemType, newType.getElementType());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);
        JavaType contentType = Mockito.mock(JavaType.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        CollectionType newType = collectionType.withContentType(contentType);

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(contentType, newType.getElementType());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);
        Object typeHandler = Mockito.mock(Object.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        CollectionType newType = collectionType.withTypeHandler(typeHandler);

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemType, newType.getElementType());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);
        Object contentTypeHandler = Mockito.mock(Object.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        JavaType contentType = new TypeFactory().constructType(new TypeFactory.DefaultTypeResolver());
        CollectionType newType = collectionType.withContentTypeHandler(contentTypeHandler);

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemType.withTypeHandler(contentType), newType.getElementType());
        assertEquals(contentTypeHandler, newType.getContentTypeHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);
        Object valueHandler = Mockito.mock(Object.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        CollectionType newType = collectionType.withValueHandler(valueHandler);

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemType, newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);
        Object contentValueHandler = Mockito.mock(Object.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        JavaType contentType = new TypeFactory().constructType(new TypeFactory.DefaultTypeResolver());
        CollectionType newType = collectionType.withContentValueHandler(contentValueHandler);

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemType.withValueHandler(contentType), newType.getElementType());
        assertEquals(contentValueHandler, newType.getContentValueHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        CollectionType newType = collectionType.withStaticTyping();

        assertEquals(collectionClass, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemType.withStaticTyping(), newType.getElementType());
        assertFalse(newType.isAsStatic());

        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> collectionClass = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = Mockito.mock(JavaType.class);

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemType);

        Class<?> subClass = Mockito.mock(Class.class);
        TypeBindings newBindings = Mockito.mock(TypeBindings.class);
        JavaType newSuperClass = Mockito.mock(JavaType.class);
        JavaType[] newSuperInterfaces = new JavaType[0];

        when(subClass.getCanonicalName()).thenReturn("com.sub.package.Collection");

        CollectionType newCollectionType = collectionType.refine(subClass, bindings, newSuperClass, newSuperInterfaces);

        assertEquals(subClass, newCollectionType.getRawClass());
        assertEquals(newBindings, newCollectionType.getTypeBindings());
        assertEquals(newSuperClass, newCollectionType.getSuperClass());
        assertArrayEquals(newSuperInterfaces, newCollectionType.getSuperInterfaces());
        assertEquals(elemType, newCollectionType.getElementType());
        assertFalse(newCollectionType.isAsStatic());
    }

    private void assertArrayIsNull(JavaType[] types) {
        assertNull(types);
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (expected.length != actual.length) {
            fail("Unexpected number of elements");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}