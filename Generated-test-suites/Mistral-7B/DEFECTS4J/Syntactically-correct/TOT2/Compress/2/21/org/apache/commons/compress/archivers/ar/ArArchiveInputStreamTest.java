import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_Success() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003etest.txt\u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e\u003c\u0021SIZE\u0020\u003e1234\u003e\u003c\u0021TRAILER\u0020\u003eAR\n"});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(1234, entry.getSize());

        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_NoEntryFound() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003etest.txt\u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e\u003c\u0021SIZE\u0020\u003e1234\u003e\u003c\u0021TRAILER\u0020\u003eAR\n\u003c\u0021TRAILER\u0020\u003eAR"});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_NoDataAfterHeader() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003etest.txt\u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e"});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetNextEntry_Success() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003e test.txt \u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e\u003c\u0021SIZE\u0020\u003e1234\u003e\u003c\u0021TRAILER\u0020\u003eAR\n"});
        final ArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);

        archiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_NoEntryFound() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        archiveInputStream.getNextEntry();
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003e test.txt \u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e\u003c\u0021SIZE\u0020\u003e1234\u003e\u003c\u0021TRAILER\u0020\u003eAR\n"});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testClose_AlreadyClosed() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0020\u003e\u003c\u0021NAME\u0020\u003e test.txt \u003e\u003c\u0021DATE\u0020\u003e1998-05-12\u003e\u003c\u0021UID\u0020\u003e1024\u003e\u003c\u0021GID\u0020\u003e512\u003e\u003c\u0021MODE\u0020\u003e0644\u003e\u003c\u0021SIZE\u0020\u003e1234\u003e\u003c\u0021TRAILER\u0020\u003eAR\n"});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_Success() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int read = arArchiveInputStream.read(outputStream.getBuffer(), 0, 3);
        Assert.assertNotNull(read);
        Assert.assertEquals(3, read);
        Assert.assertEquals(new String(outputStream.getBuffer()), "ABC");
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_EndOfStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int read = arArchiveInputStream.read(outputStream.getBuffer(), 0, 10);
        Assert.assertEquals(-1, read);
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testRead_NullOutput() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.read(null);
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_PartialRead() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int read = arArchiveInputStream.read(outputStream.getBuffer(), 0, 2);
        Assert.assertNotNull(read);
        Assert.assertEquals(2, read);
        Assert.assertEquals(new String(outputStream.getBuffer()), "AB");
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(ArArchiveInputStream.matches(new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'}, 8));
        Assert.assertFalse(ArArchiveInputStream.matches(new byte[]{'A', 'B', 'C'}, 3));
    }
}