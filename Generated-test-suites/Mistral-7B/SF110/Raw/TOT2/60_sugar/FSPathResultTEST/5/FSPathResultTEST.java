import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @org.junit.Test
    public void testFSPathResultFileConstructor() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
    }

    @org.junit.Test
    public void testFSPathResultStringConstructor() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    // Similar test cases for other constructors and getter methods

    @org.junit.Test
    public void testFSPathResultToString() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }
}