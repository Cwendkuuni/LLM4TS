import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);

        File expected = testFile;
        File actual = result.getFile();

        assertEquals(expected, actual);
        assertNotNull(actual);
    }
}