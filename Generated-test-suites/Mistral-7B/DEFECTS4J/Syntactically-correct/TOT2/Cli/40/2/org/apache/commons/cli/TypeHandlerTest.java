import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "testValue";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result.toString());
    }
}