import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertEquals(0, jsState.size());
    }
}