import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstruction() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(null, null);
        Constructor<?> ctor = new Object() {}.getClass().getDeclaredConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNotNull(innerClassProperty._creator);
    }
}