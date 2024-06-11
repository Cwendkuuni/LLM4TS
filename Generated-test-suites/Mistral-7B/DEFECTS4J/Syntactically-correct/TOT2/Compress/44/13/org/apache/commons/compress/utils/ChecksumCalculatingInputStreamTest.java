
package org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Org.junit.Platform.RunnerFactory.class)
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws Exception {
        // Test from Expert 1
    }

    @Test
    public void testReadSingleByte_EOF() throws Exception {
        // Test from Expert 2
    }

    @Test
    public void testReadSingleByte_NegativeInput() throws Exception {
        // Test from Expert 3
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        byte[] readData = new byte[input.length];
        int readBytes = testStream.read(readData);

        Assert.assertEquals(input.length, readBytes);
        Assert.assertArrayEquals(input, readData);
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());
    }

    @Test
    public void testReadByteArray_EOF() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        byte[] readData = new byte[input.length];
        int readBytes = testStream.read(readData, 0, readData.length);

        Assert.assertEquals(input.length, readBytes);
        Assert.assertArrayEquals(input, readData);
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());

        // Test EOF
        Assert.assertEquals(-1, testStream.read());
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());
    }

    @Test
    public void testReadByteArray_NegativeInput() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        byte[] readData = new byte[0];
        int readBytes = testStream.read(readData);

        Assert.assertEquals(-1, readBytes);
        Assert.assertArrayEquals(new byte[0], readData);
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOException_ReadByteArray() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        byte[] readData = new byte[input.length];
        int readBytes = testStream.read(readData);

        Assert.assertEquals(input.length, readBytes);
        Assert.assertArrayEquals(input, readData);
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());

        // IOException test
        testStream.read();
        testStream.read(readData, 0, readData.length);
        testStream.read(readData, 0, readData.length + 1);
    }

    @Test
    public void testSkip() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        long skippedBytes = testStream.skip(5);
        int readBytes = testStream.read(new byte[1]);

        Assert.assertEquals(0, readBytes);
        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());
        Assert.assertEquals(5, skippedBytes);
    }

    @Test
    public void testGetValue() throws Exception {
        byte[] input = new byte[]{0x01, 0x02, 0x03};
        InputStream testStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(input));

        Assert.assertEquals(1L + Checksums.adler32(input), testStream.getValue());
    }
}