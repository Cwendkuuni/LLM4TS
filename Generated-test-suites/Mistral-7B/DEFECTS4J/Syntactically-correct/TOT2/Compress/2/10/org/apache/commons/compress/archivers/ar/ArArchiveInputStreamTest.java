import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.test.TestArArchiveFile;
import org.apache.commons.compress.test.utils.TestFileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream testInputStream = new TestArArchiveFile("test.tar.ar").getInputStream();
        ArArchiveInputStream inputStream = new ArArchiveInputStream(testInputStream);

        ArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);

        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("file1.txt", arEntry.getName());
        Assert.assertEquals(10L, arEntry.getSize());

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);

        arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("file2.txt", arEntry.getName());
        Assert.assertEquals(20L, arEntry.getSize());

        entry = inputStream.getNextEntry();
        Assert.assertNull(entry);

        inputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        this.testGetNextArEntry();
    }

    @Test
    public void testClose() throws IOException {
        InputStream testInputStream = new TestArArchiveFile("test.tar.ar").getInputStream();
        ArArchiveInputStream inputStream = new ArArchiveInputStream(testInputStream);

        inputStream.close();

        try {
            inputStream.getNextEntry();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testRead() throws IOException {
        InputStream testInputStream = new TestArArchiveFile("test.tar.ar").getInputStream();
        ArArchiveInputStream inputStream = new ArArchiveInputStream(testInputStream);

        byte[] buffer = new byte[10];

        int read = inputStream.read(buffer);
        Assert.assertEquals(5, read);
        Assert.assertEquals('f', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('l', buffer[2]);
        Assert.assertEquals('e', buffer[3]);
        Assert.assertEquals('1', buffer[4]);

        read = inputStream.read(buffer);
        Assert.assertEquals(5, read);
        Assert.assertEquals('.', buffer[0]);
        Assert.assertEquals('t', buffer[1]);
        Assert.assertEquals('x', buffer[2]);
        Assert.assertEquals('t', buffer[3]);

        read = inputStream.read();
        Assert.assertEquals(-1, read);

        inputStream.close();
    }

    @Test
    public void testReadWithOffset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TestFileUtils.toByteArray(new TestArArchiveFile("test.tar.ar")));
        ArArchiveInputStream inputStream = new ArArchiveInputStream(testInputStream);

        byte[] buffer = new byte[10];

        int read = inputStream.read(buffer, 5, 5);
        Assert.assertEquals(5, read);
        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('x', buffer[1]);
        Assert.assertEquals('t', buffer[2]);
        Assert.assertEquals('.', buffer[3]);
        Assert.assertEquals('.', buffer[4]);

        inputStream.close();
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[16];
        byte[] testSignature = new byte[]{'3', 'c', '2', '1', '7', '2', '6', '1', '6', '8', '3', 'e', '0', 'a', '3', 'e'};

        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
        Assert.assertTrue(ArArchiveInputStream.matches(testSignature, testSignature.length));
    }
}