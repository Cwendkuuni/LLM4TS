import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {
    private ObjectIdInfo objectIdInfo;
    private SettableBeanProperty forward;

    @Rule
    public DeserializationTest deserializationTest = new DeserializationTest();

    @Before
    public void setup() {
        // Initialize forward and objectIdInfo for testing
    }

    @Test
    public void testObjectIdReferencePropertyConstructorWithForwardAndObjectIdInfo() throws Exception {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(objectIdReferenceProperty);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }
}