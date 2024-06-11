import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals("path/to/file.txt", fsPathResult.toString());
    }
}