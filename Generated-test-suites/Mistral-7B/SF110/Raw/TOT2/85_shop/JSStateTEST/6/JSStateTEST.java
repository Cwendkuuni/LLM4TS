import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testJSStateDefaultConstructor() {
        JSState jsState = new JSState();
        assertNull(jsState.getLabel());
    }
}