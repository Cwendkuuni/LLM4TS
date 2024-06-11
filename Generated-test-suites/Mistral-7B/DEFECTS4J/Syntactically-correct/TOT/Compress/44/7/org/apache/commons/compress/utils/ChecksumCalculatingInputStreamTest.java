import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChecksumCalculatingInputStreamTest {

 @Test
 void testChecksumCalculatingInputStream() throws IOException {
 Checksum calculator = new Adler32();
 InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(calculator, input);

 byte[] expectedChecksum = calculator.getValue();
 byte[] readData = new byte[3];
 int readNumber = checksumCalculatingInputStream.read(readData);
 assertEquals(readNumber, 3);
 assertArrayEquals(readData, new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
 assertEquals(expectedChecksum[0], checksumCalculatingInputStream.getValue() & 0xFF);
 assertEquals(expectedChecksum[1], (checksumCalculatingInputStream.getValue() >> 8) & 0xFF);
 assertEquals(expectedChecksum[2], (checksumCalculatingInputStream.getValue() >> 16) & 0xFF);
 assertEquals(expectedChecksum[3], (checksumCalculatingInputStream.getValue() >> 24) & 0xFF);
 }

 @Test
 void testChecksumCalculatingInputStreamNegativeInput() {
 byte[] inputData = new byte[]{-1, -2, -3};
 InputStream input = new ByteArrayInputStream(inputData);
 Checksum calculator = new Adler32();
 assertThrows(IOException.class, () -> new ChecksumCalculatingInputStream(calculator, input));
 }

 @Test
 void testChecksumCalculatingInputStreamNullInput() {
 assertThrows(NullPointerException.class, () -> new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[]{0})));
 }

 @Test
 void testChecksumCalculatingInputStreamNullChecksum() {
 assertThrows(NullPointerException.class, () -> new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[]{0})));
 }
}