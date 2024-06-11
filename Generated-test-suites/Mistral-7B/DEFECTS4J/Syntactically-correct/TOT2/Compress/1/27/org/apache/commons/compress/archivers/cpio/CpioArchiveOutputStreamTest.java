import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setSize(10);
        outputStream.putNextEntry(entry);
        Assert.assertEquals("testFile", outputStream.getCpioEntry().getName());
        Assert.assertEquals(10, outputStream.getCpioEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        outputStream.putNextEntry(entry);
        outputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        String testData = "Test data";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setSize(testData.length());
        outputStream.putNextEntry(entry);
        outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        byte[] outputData = byteArrayOutputStream.toByteArray();
        Assert.assertArrayEquals(testData.getBytes(StandardCharsets.UTF_8),
                CpioUtil.subByteArray(outputData, 0, testData.length()));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        outputStream.close();
        outputStream.write(0);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setSize(10);
        outputStream.putNextEntry(entry);
        outputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        outputStream.finish();
        byte[] outputData = byteArrayOutputStream.toByteArray();
        HashMap map = (HashMap) outputStream.getNames();
        Assert.assertEquals("testFile", map.keySet().toArray()[0].toString());
        Assert.assertEquals(10, CpioUtil.longValue(CpioUtil.subByteArray(outputData, 16, 8)[0] & 0xFFL) << 8 | CpioUtil.longValue(CpioUtil.subByteArray(outputData, 16, 8)[1] & 0xFFL));
    }
}