import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass); // normal case
        assertEquals(String.class, javaType.getRawClass());

        try {
            JavaType javaType2 = new JavaType(null, 0, null, null, false); // null class argument
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        JavaType javaType3 = new JavaType(int[].class, 1, null, null, false); // array type
        assertEquals(int[].class, javaType3.getRawClass());
    }

    @Test
    public void testGetHashCode() {
        Class<String> stringClass = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(stringClass);
        assertEquals(javaType1.hashCode(), javaType2.hashCode());

        String substringClass = String.class.getComponentType();
        JavaType javaType3 = TypeFactory.defaultInstance().constructType(substringClass);
        assertNotEquals(javaType1.hashCode(), javaType3.hashCode());
    }

    @Test
    public void testIsContainerType() {
        Class<List<String>> listClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        assertTrue(javaType.isContainerType());

        Class<String> stringClass = String.class;
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(stringClass);
        assertFalse(javaType2.isContainerType());
    }

    @Test
    public void testUseStaticType() {
        Class<List<String>> listClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        assertFalse(javaType.useStaticType());

        JavaType javaType2 = javaType.withStaticTyping();
        assertTrue(javaType2.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        Class<String> stringClass = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaType2 = javaType1.narrowBy(StringBuilder.class);
        assertTrue(javaType2 instanceof JavaType);
        assertEquals(StringBuilder.class, javaType2.getRawClass());

        try {
            JavaType javaType3 = javaType1.narrowBy(Integer.class); // not compatible types
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWidenBy() {
        Class<List<String>> listClass = new ArrayList<List<String>>().getClass();
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(listClass);
        JavaType javaType2 = javaType1.widenBy(Collection.class);
        assertTrue(javaType2 instanceof JavaType);
        assertTrue(javaType2.isInstanceOf(List.class)); // not exact type, but superclass is compatible
    }
}