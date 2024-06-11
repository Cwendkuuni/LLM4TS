import static org.junit.Assert.assertSame;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        Coder expectedDecoder = new CopyDecoder();
        Coder coder = new CopyDecoder();
        byte[] password = new byte[0];
        InputStream actualInputStream = Coders.addDecoder(testInputStream, coder, password);
        assertSame(expectedDecoder.getClass(), actualInputStream.getClass());
    }
}