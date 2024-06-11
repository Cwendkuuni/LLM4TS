import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private static final String TEST_DATA = "TestData.ar";
    private ArArchiveInputStream testStream;
    private InputStream inputStream;

    @Before
    public void setup() throws IOException {
        inputStream = getClass().getResourceAsStream(TEST_DATA);
        testStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testReadEntry() throws IOException {
        ArchiveEntry entry = testStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testEntry.txt", entry.getName());
        Assert.assertEquals(10L, entry.getSize());

        String expectedContent = "Test Content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = testStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        String actualContent = outputStream.toString();
        Assert.assertEquals(expectedContent, actualContent);
        IOUtils.closeQuietly(outputStream);
    }

    @Test(expected = IOException.class)
    public void testNoMoreEntry() throws IOException {
        testStream.getNextEntry();
        testStream.getNextEntry();
        testStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        ByteArrayInputStream invalidStream = new ByteArrayInputStream(
                "InvalidData".getBytes());
        ArArchiveInputStream testInvalidStream = new ArArchiveInputStream(
                invalidStream);
        testInvalidStream.getNextArEntry();
    }

    @Test
    public void testClose() throws IOException {
        testStream.close();
        ArchiveEntry entry = testStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[8];
        inputStream.read(signature);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}