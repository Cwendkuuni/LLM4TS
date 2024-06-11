import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() throws IOException {
        // Test empty constructor
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);

        // Test constructor with empty input stream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStreamEmpty = new DeflateCompressorInputStream(emptyInput);
        Assert.assertEquals(-1, deflateCompressorInputStreamEmpty.read());

        // Test constructor with valid input stream and default parameters
        byte[] inputData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x2e, (byte) 0x03, (byte) 0x42, (byte) 0x60, (byte) 0x86, (byte) 0x57, (byte) 0x66, (byte) 0x55, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x6d};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStreamDefault = new DeflateCompressorInputStream(inputStream);
        byte[] outputDataDefault = new byte[inputData.length];
        int readDefault = IOUtils.readFully(deflateCompressorInputStreamDefault, outputDataDefault);
        Assert.assertEquals(inputData.length, readDefault);
        Assert.assertArrayEquals(inputData, outputDataDefault);

        // Test constructor with custom parameters
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setCompressionLevel(DeflateCompressorOutputStream.DEFAULT_COMPRESSION);
        InputStream inputStreamCustom = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStreamCustom = new DeflateCompressorInputStream(inputStreamCustom, deflateParameters);
        byte[] outputDataCustom = new byte[inputData.length];
        int readCustom = IOUtils.readFully(deflateCompressorInputStreamCustom, outputDataCustom);
        Assert.assertEquals(inputData.length, readCustom);
        Assert.assertArrayEquals(inputData, outputDataCustom);

        // Test read() method
        byte[] readBuffer = new byte[1];
        byte testByte = (byte) 0xAB;
        deflateCompressorInputStreamDefault.write(testByte);
        int read = deflateCompressorInputStreamDefault.read();
        Assert.assertEquals(testByte, read);

        // Test read(byte[]) method
        byte[] buffer = new byte[3];
        int readLength = deflateCompressorInputStreamDefault.read(buffer);
        Assert.assertEquals(3, readLength);
        Assert.assertArrayEquals(new byte[]{inputData[0], inputData[1], inputData[2]}, buffer);

        // Test skip() method
        long skipAmount = 3L;
        long skipped = deflateCompressorInputStreamDefault.skip(skipAmount);
        Assert.assertEquals(skipAmount, skipped);

        // Test available() method
        long available = deflateCompressorInputStreamDefault.available();
        Assert.assertEquals(inputData.length - skipAmount, available);

        // Test close() method
        deflateCompressorInputStreamDefault.close();
        int readClosed = deflateCompressorInputStreamDefault.read();
        Assert.assertEquals(-1, readClosed);
    }

    @Test
    public void testDeflateCompressorInputStreamWithZlibHeader() throws IOException {
        // Test empty constructor
        InputStream inputStreamWithZlibHeader = new ByteArrayInputStream(new byte[]{0x78, 0x49, 0x44, 0x37, 0x6b});
        DeflateCompressorInputStream deflateCompressorInputStreamWithZlibHeader = new DeflateCompressorInputStream(inputStreamWithZlibHeader);
        Assert.assertNotNull(deflateCompressorInputStreamWithZlibHeader);

        // Test read() method
        byte[] readBufferWithZlibHeader = new byte[1];
        byte testByteWithZlibHeader = (byte) 0xAB;
        deflateCompressorInputStreamWithZlibHeader.write(testByteWithZlibHeader);
        int readWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.read();
        Assert.assertEquals(testByteWithZlibHeader, readWithZlibHeader);

        // Test read(byte[]) method
        byte[] bufferWithZlibHeader = new byte[3];
        int readLengthWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.read(bufferWithZlibHeader);
        Assert.assertEquals(3, readLengthWithZlibHeader);
        Assert.assertArrayEquals(new byte[]{inputData[1], inputData[2], inputData[3]}, bufferWithZlibHeader);

        // Test skip() method
        long skipAmountWithZlibHeader = 3L;
        long skippedWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.skip(skipAmountWithZlibHeader);
        Assert.assertEquals(skipAmountWithZlibHeader, skippedWithZlibHeader);

        // Test available() method
        long availableWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.available();
        Assert.assertEquals(inputData.length - 5 - skipAmountWithZlibHeader, availableWithZlibHeader);

        // Test close() method
        deflateCompressorInputStreamWithZlibHeader.close();
        int readClosedWithZlibHeader = deflateCompressorInputStreamWithZlibHeader.read();
        Assert.assertEquals(-1, readClosedWithZlibHeader);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamException() throws IOException {
        // Test constructor with null input stream
        InputStream nullInputStream = null;
        DeflateCompressorInputStream deflateCompressorInputStreamNull = new DeflateCompressorInputStream(nullInputStream);

        // Test read() method with closed stream
        byte[] readBufferException = new byte[1];
        deflateCompressorInputStreamNull.close();
        int read = deflateCompressorInputStreamNull.read(readBufferException);

        // Test read(byte[]) method with closed stream
        byte[] bufferException = new byte[3];
        int readLengthException = deflateCompressorInputStreamNull.read(bufferException);

        // Test skip() method with closed stream
        long skipAmountException = 3L;
        long skippedException = deflateCompressorInputStreamNull.skip(skipAmountException);

        // Test available() method with closed stream
        long availableException = deflateCompressorInputStreamNull.available();
    }

    @Test
    public void testCount() throws IOException {
        // Test empty constructor
        InputStream inputStreamEmpty = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStreamEmpty = new DeflateCompressorInputStream(inputStreamEmpty);
        deflateCompressorInputStreamEmpty.write(123);
        deflateCompressorInputStreamEmpty.write(456);
        deflateCompressorInputStreamEmpty.write(789);
        Assert.assertEquals(3, deflateCompressorInputStreamEmpty.getBytesRead());

        // Test constructor with valid input stream and default parameters
        InputStream inputStreamValid = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});
        DeflateCompressorInputStream deflateCompressorInputStreamValid = new DeflateCompressorInputStream(inputStreamValid);
        deflateCompressorInputStreamValid.write(123);
        deflateCompressorInputStreamValid.write(456);
        deflateCompressorInputStreamValid.write(789);
        Assert.assertEquals(3, deflateCompressorInputStreamValid.getBytesRead());

        // Test constructor with custom parameters
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setCompressionLevel(DeflateCompressorOutputStream.DEFAULT_COMPRESSION);
        InputStream inputStreamCustom = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});
        DeflateCompressorInputStream deflateCompressorInputStreamCustom = new DeflateCompressorInputStream(inputStreamCustom, deflateParameters);
        deflateCompressorInputStreamCustom.write(123);
        deflateCompressorInputStreamCustom.write(456);
        deflateCompressorInputStreamCustom.write(789);
        Assert.assertEquals(3, deflateCompressorInputStreamCustom.getBytesRead());
    }
}