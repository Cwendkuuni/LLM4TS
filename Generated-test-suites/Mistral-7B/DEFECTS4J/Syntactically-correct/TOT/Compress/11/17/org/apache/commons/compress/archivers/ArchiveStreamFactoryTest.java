import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        byte[] zipBytes = getZipFileContent("testData.zip").readAllBytes();
        ByteArrayInputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);

        // Tests typical use case: reading a valid ZIP archive
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testFile.txt", entry.getName());
        InputStream inputStream = archiveInputStream;
        byte[] fileContent = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(fileContent)) != -1) {
            // read the content of the file
        }
        archiveInputStream.close();

        // Tests edge case: reading an empty archive
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // Tests error scenario: providing an invalid archive format
        InputStream invalidInputStream = new ByteArrayInputStream("Invalid archive content".getBytes());
        ArchiveInputStream invalidArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, invalidInputStream);
        Assert.assertThrows(ArchiveException.class, () -> invalidArchiveInputStream.getNextEntry());
        invalidArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        File outputFile = new File("testOutput.zip");
        OutputStream outputStream = new FileOutputStream(outputFile);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);

        // Tests typical use case: writing a valid ZIP archive
        archiveOutputStream.putArchiveEntry(new ZipEntry("testFile.txt"));
        InputStream fileInputStream = new FileInputStream("testFileToBeArchived.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            archiveOutputStream.write(buffer, 0, bytesRead);
        }
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ZipFile zipFile = new ZipFile(outputFile);
        ArchiveEntry archiveEntry = zipFile.getEntry("testFile.txt");
        Assert.assertNotNull(archiveEntry);
        InputStream archiveInputStream = zipFile.getInputStream(archiveEntry);
        byte[] expectedFileContent = new byte[1024]; // the content of testFileToBeArchived.txt
        int bytesReadFromArchive = 0;
        while ((bytesReadFromArchive = archiveInputStream.read(expectedFileContent)) != -1) {
            Assert.assertArrayEquals(expectedFileContent, 0, bytesReadFromArchive, fileContent);
        }
        zipFile.close();

        // Tests edge case: writing an empty archive
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Tests error scenario: providing an invalid archive format
        OutputStream invalidOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, invalidOutputStream);
        Assert.assertThrows(ArchiveException.class, () -> archiveOutputStream.putArchiveEntry(new JarEntry("testFile.txt")));
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        byte[] zipBytes = getZipFileContent("testData.zip").readAllBytes();
        ByteArrayInputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);

        // Tests typical use case: reading a valid ZIP archive
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testFile.txt", entry.getName());
        InputStream inputStream = archiveInputStream;
        byte[] fileContent = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(fileContent)) != -1) {
            // read the content of the file
        }
        archiveInputStream.close();

        // tests edge case: providing an empty input stream
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    private File getZipFileContent(String fileName) throws IOException {
        File testDataFile = new File("testData/" + fileName);
        byte[] fileContent = Files.readAllBytes(testDataFile.toPath());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream output = outputStream;
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        archiveOutputStream.putNextEntry(new ZipEntry(fileName));
        output = archiveOutputStream;
        output.write(fileContent);
        output.close();
        return new File(fileName);
    }
}