import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws Exception {
        JavaType baseType = null;
        TypeFactory typeFactory = null;
        ClassNameIdResolver classResolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new Object();
        Class<?> valueClass = value.getClass();
        String expectedId = valueClass.getName();

        String id = classResolver.idFromValue(value);
        assertEquals(expectedId, id);
    }
}