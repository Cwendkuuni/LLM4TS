import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    
    @Test
    public void testFilterNormalText() {
         String message = "This is a test message.";
         String expected = "This is a test message.";
         String actual = HTMLFilter.filter(message);
         assertEquals(expected, actual);
     }
     
}