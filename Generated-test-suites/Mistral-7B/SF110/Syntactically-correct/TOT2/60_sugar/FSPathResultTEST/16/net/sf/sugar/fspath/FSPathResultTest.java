import static org.junit.Assert.assertEquals;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {
    @Test
    public void testFSPathResultFileConstructor() {
        File file = new File("path/to/file");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }
}