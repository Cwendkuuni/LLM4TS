import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoWithFourArguments() {
        PropertyName propName = new PropertyName("test");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> gen = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, gen, false, resolver);
        assertEquals("test", objIdInfo.getPropertyName().getName());
        assertEquals(String.class, objIdInfo.getScope());
        // Add more assertions as needed
    }
}