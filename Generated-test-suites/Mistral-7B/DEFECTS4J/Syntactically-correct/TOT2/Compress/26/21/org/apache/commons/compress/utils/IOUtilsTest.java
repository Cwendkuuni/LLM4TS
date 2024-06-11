
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        Expert1.testCopy();
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        Expert1.testCopyWithBufferSize();
    }

    @Test
    public void testSkip() throws IOException {
        Expert1.testSkip();
    }

    @Test
    public void testSkipEdgeCasesAndExceptions() throws IOException {
        Expert1.testSkipEdgeCasesAndExceptions();
    }

    @Test
    public void testReadFully() throws IOException {
        Expert2.testReadFully();
    }

    @Test
    public void testReadFullyPartial() throws IOException {
        Expert2.testReadFullyPartial();
    }

    @Test
    public void testReadFullyEdgeCasesAndExceptions() throws IOException {
        Expert2.testReadFullyEdgeCasesAndExceptions();
    }

    @Test
    public void testCloseQuietly() {
        Expert3.testCloseQuietly();
    }

    @Test
    public void testToByteArray() throws IOException {
        Expert1.testToByteArray();
    }
}