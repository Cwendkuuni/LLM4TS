import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FileTextTest {
    @Test
    public void testGetMethod() {
        // Assuming you have initialized FileText object, say 'fileText'
        String expected = "Expected line text";
        String actual = fileText.get(0); // Get the first line
        assertEquals(expected, actual);
    }
}