import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        assertEquals(String.class, javaType.getRawClass());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithTypeHandlers() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        Object valueHandler = new Object() {};
        JavaType typedJavaType = javaType.withValueHandler(valueHandler);
        Object typeHandler = new Object() {};
        JavaType typedJavaTypeWithTypeHandler = typedJavaType.withTypeHandler(typeHandler);
        assertSame(javaType, typedJavaType);
        assertSame(typedJavaType, typedJavaTypeWithTypeHandler);
        assertEquals(valueHandler, typedJavaTypeWithTypeHandler.getValueHandler());
        assertEquals(typeHandler, typedJavaTypeWithTypeHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalid() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        Class<Integer> subClass = Integer.class;
        JavaType narrowedType = javaType.narrowBy(subClass);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        Class<StringBuffer> subClass = StringBuffer.class;
        JavaType narrowedType = javaType.narrowBy(subClass);
        assertEquals(StringBuffer.class, narrowedType.getRawClass());
        assertSame(narrowedType, javaType.narrowBy(StringBuffer.class));
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        Class<Object> superClass = Object.class;
        JavaType widenedType = javaType.widenBy(superClass);
        assertEquals(Object.class, widenedType.getRawClass());
        assertSame(widenedType, javaType.widenBy(Object.class));
        assertNull(widenedType.getValueHandler());
        assertNull(widenedType.getTypeHandler());
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        assertTrue(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        List<List<String>> containerType = new ArrayList<>();
        JavaType containerTypeJavaType = new TypeFactory().constructType(containerType.getClass());
        assertTrue(javaType.isContainerType());
        assertTrue(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertSame(containerTypeJavaType, javaType.narrowContentsBy(List.class));
    }

    @Test
    public void testJavaTypeIsMapLikeType() {
        Class<Map<String, List<String>>> rawType = Map.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        Map<String, Map<String, List<String>>> containerType = new HashMap<>();
        JavaType containerTypeJavaType = new TypeFactory().constructType(containerType.getClass());
        assertTrue(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertTrue(javaType.isMapLikeType());
        assertSame(containerTypeJavaType, javaType.narrowContentsBy(Map.class));
    }

    @Test
    public void testJavaTypeUseStaticType() {
        Class<String> rawType = String.class;
        JavaType javaType = new TypeFactory().constructType(rawType);
        JavaType staticJavaType = javaType.withStaticTyping();
        assertTrue(staticJavaType.useStaticType());
        assertSame(staticJavaType, javaType.withStaticTyping());
    }
}