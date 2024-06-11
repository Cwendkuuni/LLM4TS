import org.junit.Test;
import org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result.toString());
    }
}