import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException, ArchiveException {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws IOException, ArchiveException {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknownFormat", inputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect() throws IOException, ArchiveException {
        byte[] signature = new byte[12];
        inputStream.mark(signature.length);
        IOUtils.readFully(inputStream, signature);
        inputStream.reset();

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_7z() throws IOException, ArchiveException {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, inputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException, ArchiveException {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownFormat() throws IOException, ArchiveException {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknownFormat", outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException, ArchiveException {
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(archiveInputStream);

        ZipArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStream_ZIP_InvalidSignature() throws IOException, ArchiveException {
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        zipInputStream.mark(512);
        byte[] tarheader = new byte[512];
        IOUtils.readFully(zipInputStream, tarheader);
        zipInputStream.reset();

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException, ArchiveException {
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        archiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        archiveOutputStream.write(new byte[0], 0, 0);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        Assert.assertEquals("test.txt", zipOutputStream.toString());
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException, ArchiveException {
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertNotNull(archiveInputStream);

        TarEntry entry = (TarEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStream_TAR_InvalidSignature() throws IOException, ArchiveException {
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        tarInputStream.mark(512);
        byte[] tarheader = new byte[512];
        IOUtils.readFully(tarInputStream, tarheader);
        tarInputStream.reset();

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        archiveInputStream.close();
    }
}