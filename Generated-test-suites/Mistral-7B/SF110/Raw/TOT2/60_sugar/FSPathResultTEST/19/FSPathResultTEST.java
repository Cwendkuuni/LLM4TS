import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }
}