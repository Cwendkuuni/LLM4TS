import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] source = new byte[]{'A', 'B', 'C'};
        byte[] target = new byte[3];
        int bytesCopied = IOUtils.copy(new ByteArrayInputStream(source), new ByteArrayOutputStream(target.length));
        byte[] actual = new byte[bytesCopied];
        IOUtils.toByteArray(new ByteArrayInputStream(target.subArray(0, bytesCopied)), actual);
        Assert.assertArrayEquals(source, actual);
    }
}