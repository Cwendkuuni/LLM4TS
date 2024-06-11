
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Adler32Checksum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(org.apache.commons.compress.utils.test.functional.FunctionalTest.class)
public class ChecksumCalculatingInputStreamTest {
    // Test cases for each method
    // ... (Copy and paste the test cases for each method)

    // Test for getValue() method when the underlying InputStream has been read completely
    @Test
    public void testGetValueReadComplete() throws Exception {
        byte[] testData = {0x01, 0x02, 0x03};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new Adler32Checksum(), new ByteArrayInputStream(testData));
        inputStream.close();
        long result = inputStream.getValue();
        long expected = 1L; // Adler-32 checksum for testData
        Assert.assertEquals(expected, result);
    }

    // Test for getValue() method when the underlying InputStream is closed before calling getValue()
    @Test(expected = IOException.class)
    public void testGetValueClosedBeforeGetValue() throws Exception {
        byte[] testData = {0x01, 0x02, 0x03};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new Adler32Checksum(), new ByteArrayInputStream(testData));
        inputStream.close();
        long result = inputStream.getValue();
        Assert.assertThrows(IOException.class, () -> inputStream.read());
    }
}
