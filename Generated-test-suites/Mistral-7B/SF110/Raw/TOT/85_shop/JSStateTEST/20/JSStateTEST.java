import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSStateTest {
    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        // An empty state should have an empty label and no elements
        assertEquals("", jsState.getLabel());
        assertEquals(0, jsState.size());
    }
}