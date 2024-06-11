import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.exception.ArchiveFormatException;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.io.IOExceptionContent;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveFormatException {
        // Test creating Zip Archive InputStream from a byte array
        byte[] zipBytes = getZipBytes();
        InputStream inputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        TarEntry tarEntry = archiveInputStream.getNextEntry();
        assertEquals("test1.txt", tarEntry.getName());
        archiveInputStream.close();

        // Test creating Zip Archive InputStream from a File
        File zipFile = new File("test.zip");
        FileInputStream fileInputStream = new FileInputStream(zipFile);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, fileInputStream);
        tarEntry = archiveInputStream.getNextEntry();
        assertEquals("test1.txt", tarEntry.getName());
        archiveInputStream.close();

        // Test creating Tar Archive InputStream from a byte array
        byte[] tarBytes = getTarBytes();
        inputStream = new ByteArrayInputStream(tarBytes);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        tarEntry = (TarEntry) archiveInputStream.getNextEntry();
        assertEquals("test1.txt", tarEntry.getName());
        archiveInputStream.close();

        // Test creating Tar Archive InputStream from a File
        File tarFile = new File("test.tar");
        fileInputStream = new FileInputStream(tarFile);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, fileInputStream);
        tarEntry = (TarEntry) archiveInputStream.getNextEntry();
        assertEquals("test1.txt", tarEntry.getName());
        archiveInputStream.close();

        // Test creating unknown archive type
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            //pass
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test creating Zip Archive OutputStream to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new ZipEntry("test.txt"));
        archiveOutputStream.write(new byte[10], 0, 10);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        assertEquals("PK\u0005\u0002\u0014\u0000\u0004\u0007test.txt\u0000\u0002\u0014\u0000\u0000\u0000\u0000local\u0000test.txt\u0001\u0000\u0000\u0000\u0000\u0001test.txt\u0000\u0000\u0000\u0003\u0004\u0014test.txt\u0014\u0000\u0000\u0000\u0000\u0001\u0000\u0001\u0001", outputStream.toString());
        archiveOutputStream.close();

        // Test creating Tar Archive OutputStream to a File
        File tarFile = new File("test.tar");
        FileOutputStream fileOutputStream = new FileOutputStream(tarFile);
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, fileOutputStream);
        archiveOutputStream.putNextEntry(new TarEntry("test.txt"));
        archiveOutputStream.write(new byte[10], 0, 10);
        archiveOutputStream.closeEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Test creating unknown archive type
        ByteArrayOutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownOutputStream);
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            //pass
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() throws IOException {
        // Test creating an unknown archive type from a byte array with an invalid signature
        byte[] invalidSignatureBytes = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(invalidSignatureBytes);
        try {
            ArchiveStreamFactory.ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            //pass
        }
    }

    private byte[] getZipBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        os.putArchiveEntry(new ZipEntry("test1.txt"));
        os.write("This is a test file".getBytes(), 0, "This is a test file".length());
        os.closeArchiveEntry();
        os.finish();
        return outputStream.toByteArray();
    }

    private byte[] getTarBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        TarEntry tarEntry = new TarEntry("test1.txt");
        tarEntry.setSize(10);
        os.putNextEntry(tarEntry);
        os.write(new byte[10], 0, 10);
        os.closeEntry();
        os.finish();
        return outputStream.toByteArray();
    }
}