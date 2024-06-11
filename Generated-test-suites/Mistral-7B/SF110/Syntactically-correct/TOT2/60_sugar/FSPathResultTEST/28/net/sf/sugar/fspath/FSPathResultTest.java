import static org.junit.Assert.assertNotNull;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {
    @Test
    public void testGetFile() {
        File testFile = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        File result = fsPathResult.getFile();
        assertNotNull(result);
        assertEquals(testFile.getAbsolutePath(), result.getAbsolutePath());
    }
}