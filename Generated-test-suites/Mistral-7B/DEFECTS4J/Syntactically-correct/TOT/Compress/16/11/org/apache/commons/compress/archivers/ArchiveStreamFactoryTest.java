import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.ZipArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (archiveInputStream != null) {
            archiveInputStream.close();
        }
        if (archiveOutputStream != null) {
            archiveOutputStream.close();
        }
        if (outputStream instanceof AutoCloseable) {
            ((AutoCloseable) outputStream).close();
        }
        if (inputStream instanceof AutoCloseable) {
            ((AutoCloseable) inputStream).close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        ZipArchiveEntry zipEntry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(zipEntry);
        Assert.assertEquals("Test data", zipEntry.getName());
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        TarEntry tarEntry = (TarEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(tarEntry);
        Assert.assertEquals("Test data", tarEntry.getName());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiver() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("Test data"));
        IOUtils.copy(inputStream, archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(outputStream.toByteArray()));
        ZipArchiveEntry zipEntry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(zipEntry);
        Assert.assertEquals("Test data", zipEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(outputStream);
        archiveOutputStream.putArchiveEntry(new TarEntry("Test data"));
        IOUtils.copy(inputStream, archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TarEntry tarEntry = (TarEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(tarEntry);
        Assert.assertEquals("Test data", tarEntry.getName());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiverOutputStream() throws Exception {
        new ArchiveStreamFactory()
                .createArchiveOutputStream("unknown", outputStream);
    }
}