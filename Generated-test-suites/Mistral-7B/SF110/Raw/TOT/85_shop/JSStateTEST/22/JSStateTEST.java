// JSStateConstructorTest.java
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSStateConstructorTest {
    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertEquals(0, jsState.size());
    }

    @Test
    public void testJSStateTokenizerConstructor() throws IOException {
        String input = "(:state s)";
        JSState jsState = new JSState(new StringReader(input));
        assertEquals(1, jsState.size());
    }
}