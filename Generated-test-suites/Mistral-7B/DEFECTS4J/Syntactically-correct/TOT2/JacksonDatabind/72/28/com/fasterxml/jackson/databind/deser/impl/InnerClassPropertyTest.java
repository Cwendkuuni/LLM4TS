import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.*;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstruction() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(null, null);
        Constructor<Object> ctor = Object.class.getDeclaredConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertNotNull(innerClassProperty);
    }
}