import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValueStringClass_InvalidType() {
        String str = "123";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueNullString() {
        Object obj = new Object();
        Object result = TypeHandler.createValue(null, obj.getClass());
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueEmptyString() {
        Object obj = new Object();
        Object result = TypeHandler.createValue("", obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValueStringWithSpecialCharacters() {
        Object obj = new Object();
        Object result = TypeHandler.createValue("test@123", obj.getClass());
        Assert.assertEquals(obj, result);
    }
}