import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testConstructor() {
        Class<?> raw = mock(Class.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        boolean asStatic = true;

        JavaType javaType = new JavaType(raw, 1, valueHandler, typeHandler, asStatic);

        assertNotNull(javaType);
        assertEquals(raw, javaType.getRawClass());
        assertEquals(asStatic, javaType.useStaticType());
    }
}