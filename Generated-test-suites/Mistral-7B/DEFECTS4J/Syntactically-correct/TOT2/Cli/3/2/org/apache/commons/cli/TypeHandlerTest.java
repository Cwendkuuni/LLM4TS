import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeHandlerTest {
    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);

        str = "123";
        obj = Integer.class;
        result = TypeHandler.createValue(str, obj);
        assertEquals(123, result);

        str = "45.6";
        obj = Double.class;
        result = TypeHandler.createValue(str, obj);
        assertEquals(45.6, result);

        str = "2022-01-01";
        obj = Date.class;
        result = TypeHandler.createValue(str, obj);
        assertEquals(new Date(2022, 0, 1), result);

        str = "com.example.MyClass";
        obj = Class.class;
        result = TypeHandler.createValue(str, obj);
        assertNotNull(result);

        str = "";
        obj = File.class;
        result = TypeHandler.createValue(str, obj);
        assertNull(result);

        str = "abc";
        obj = URL.class;
        result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
}