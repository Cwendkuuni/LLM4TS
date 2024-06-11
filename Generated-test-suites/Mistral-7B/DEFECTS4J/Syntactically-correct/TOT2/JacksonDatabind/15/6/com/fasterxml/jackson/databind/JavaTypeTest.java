import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<?> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        assertEquals(rawClass, javaType.getRawClass());
        assertNotNull(javaType._class);
        assertEquals(Integer.TYPE, javaType.narrowBy(Integer.class).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeConstructionWithNullRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeConstructionWithIncompatibleRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Double.class);
        JavaType incompatibleJavaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowedIncompatibleJavaType = incompatibleJavaType.narrowBy(Double.class);

        assertNotEquals(Double.TYPE, javaType.getRawClass());
        assertNotEquals(Double.TYPE, narrowedIncompatibleJavaType.getRawClass());
    }

    @Test
    public void testJavaTypeConstructionWithInterface() {
        Class<?> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        assertTrue(javaType.isInterface());
    }
}