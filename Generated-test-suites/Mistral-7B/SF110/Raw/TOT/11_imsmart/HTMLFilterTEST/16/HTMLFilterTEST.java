import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    
    @Test
    public void testFilterWithEmptyString() {
        String message = "";
        String expected = "";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterWithSimpleMessage() {
        String message = "Hello, World!";
        String expected = "Hello, World!";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterWithSpecialCharacters() {
        String message = "<>&\"";
        String expected = "&lt;&gt;&amp; &quot;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
}