import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

 private InputStream input;
 private OutputStream output;
 private ByteArrayOutputStream byteArrayOutputStream;

 @Before
 public void setUp() {
 input = IOUtils.class.getResourceAsStream("/test.txt");
 output = new ByteArrayOutputStream();
 byteArrayOutputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(input);
 IOUtils.closeQuietly(output);
 IOUtils.closeQuietly(byteArrayOutputStream);
 }

 @Test
 public void testCopy() throws IOException {
 byte[] expected = {"Test String".getBytes()};
 byte[] result = new byte[expected.length];
 long count = IOUtils.copy(input, output);
 IOUtils.copy(output, byteArrayOutputStream);
 byte[] actual = byteArrayOutputStream.toByteArray();
 assertArrayEquals(expected, actual);
 assertEquals(expected.length, count);
 }

 @Test
 public void testCopyWithBufferSize() throws IOException {
 byte[] expected = {"Test String with Buffer Size".getBytes()};
 byte[] result = new byte[expected.length];
 long count = IOUtils.copy(input, output, 1024);
 IOUtils.copy(output, byteArrayOutputStream);
 byte[] actual = byteArrayOutputStream.toByteArray();
 assertArrayEquals(expected, actual);
 assertEquals(expected.length, count);
 }

 @Test
 public void testSkip() throws IOException {
 InputStream inputTest = new ByteArrayInputStream("Test String".getBytes());
 long numToSkip = 3;
 long count = IOUtils.skip(inputTest, numToSkip);
 assertEquals("Test String".length(), inputTest.available());
 assertEquals(3, count);
 }

 @Test
 public void testReadFully() throws IOException {
 byte[] expected = {"Test String Read Fully".getBytes()};
 byte[] result = new byte[expected.length];
 IOUtils.readFully(input, result);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testReadFullyWithBufferSize() throws IOException {
 byte[] expected = {"Test String Read Fully With Buffer Size".getBytes()};
 byte[] result = new byte[expected.length];
 IOUtils.readFully(input, result, 1024);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testToByteArray() throws IOException {
 byte[] expected = {"Test String to Byte Array".getBytes()};
 byte[] result = IOUtils.toByteArray(input);
 assertArrayEquals(expected, result);
 }
}