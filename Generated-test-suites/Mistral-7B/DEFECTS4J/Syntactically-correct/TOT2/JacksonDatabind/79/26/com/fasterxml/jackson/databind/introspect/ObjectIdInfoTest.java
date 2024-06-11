import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    @Test
    public void testObjectIdInfoConstructionWithPropertyName() {
        PropertyName propName = new PropertyName("testPropName");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> generator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test other methods with similar structure
}