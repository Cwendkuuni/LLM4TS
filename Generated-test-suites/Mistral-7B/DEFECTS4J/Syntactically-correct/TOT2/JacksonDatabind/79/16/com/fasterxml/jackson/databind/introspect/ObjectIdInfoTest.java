import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<ObjectIdGenerator<String>> generator = ObjectIdGenerator.StringIdGenerator.class;
        boolean alwaysAsId = false;
        Class<?> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        assertEquals("testPropertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.StringIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
}