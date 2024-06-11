import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {
    @Test
    public void testJDOMResultConstructor() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }
}