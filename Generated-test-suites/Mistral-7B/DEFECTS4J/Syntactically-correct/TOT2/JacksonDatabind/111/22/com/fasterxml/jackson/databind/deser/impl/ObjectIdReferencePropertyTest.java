import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstructor() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
}