package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test cases for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Tests creating archive input streams from valid archiver names and input streams.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromValidStreams() throws Exception {
        final String testDataDir = "testdata/";
        final String archiveName = "test-archive.tar";

        try (OutputStream out = new FileOutputStream(archiveName)) {
            final ArchiveOutputStream tarOs = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            tarOs.putNextEntry(new TarArchiveEntry("testdata/test1.txt"));
            IOUtils.copy(new FileInputStream(testDataDir + "test1.txt"), tarOs);
            tarOs.closeArchiveEntry();
            tarOs.close();
        }

        try (InputStream is = new FileInputStream(archiveName)) {
            final ArchiveInputStream tarIs = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, is);
            TarArchiveEntry entry = (TarArchiveEntry) tarIs.getNextEntry();
            assertNotNull(entry);
            assertEquals("testdata/test1.txt", entry.getName());
            assertNotNull(tarIs.getInputStream());
            String expectedContent = "Test content.";
            byte[] buffer = new byte[256];
            int length;
            while ((length = tarIs.getInputStream().read(buffer)) != -1) {
                String content = new String(buffer, 0, length);
                assertEquals(expectedContent, content);
            }
            tarIs.close();
        }

        IOUtils.deleteQuietly(new File(archiveName));

        String sevenZArchiveName = "test-archive.7z";
        try (OutputStream out = new FileOutputStream(sevenZArchiveName)) {
            CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
            SevenZFile sevenZFile = new SevenZFile(compressorStreamFactory.createCompressorOutputStream(SevenZFile.Crc, SevenZFile.Gzip, out));
            sevenZFile.putNextEntry("testdata/test1.txt");
            IOUtils.copy(new FileInputStream(testDataDir + "test1.txt"), sevenZFile.getOutputStream());
            sevenZFile.closeEntry();
            sevenZFile.finish();
            sevenZFile.close();
        }

        try (InputStream is = new FileInputStream(sevenZArchiveName)) {
            assertThrows(StreamingNotSupportedException.class, () -> {
                new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, is);
            });
            IOUtils.deleteQuietly(new File(sevenZArchiveName));
        }
    }

    /**
     * Tests creating archive input streams from an unknown archiver name and input stream.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromUnknownArchiverName() {
        final String testDataDir = "testdata/";
        final InputStream is = new FileInputStream(testDataDir + "test1.txt");

        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", is);
        });
    }

    /**
     * Tests creating archive input streams from an input stream that does not support mark.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromInputStreamWithoutMarkSupport() {
        final InputStream is = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, is);
        });
    }

    /**
     * Tests creating archive input streams from an invalid archiver signature.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromInvalidArchiveSignature() throws Exception {
        final byte[] invalidSignature = new byte[12];
        IOUtils.fill(new ByteArrayOutputStream(invalidSignature), (byte) 0xFF);

        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(invalidSignature));
        });
    }

    /**
     * Tests creating archive output streams from valid archiver names and output streams.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamFromValidStreams() throws Exception {
        String testDataDir = "testdata/";
        String archiveName = "test-archive.tar";

        try (OutputStream out = new FileOutputStream(archiveName)) {
            final ArchiveOutputStream tarOs = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            tarOs.putNextEntry(new TarArchiveEntry("testdata/test1.txt"));
            IOUtils.copy(new FileInputStream(testDataDir + "test1.txt"), tarOs);
            tarOs.closeArchiveEntry();
            tarOs.close();
        }

        try (InputStream is = new FileInputStream(archiveName)) {
            TarArchiveEntry entry = new TarArchiveEntry("testdata/test1.txt");
            entry.setName("testdata/test1_modified.txt");
            final ArchiveOutputStream tarOs = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
            tarOs.putNextEntry(entry);
            IOUtils.copy(is, tarOs);
            tarOs.closeArchiveEntry();
            tarOs.close();
            byte[] expectedContent = "Test content modified.";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(new ByteArrayInputStream(expectedContent.getBytes()), bos);
            assertEquals(bos.toByteArray(), IOUtils.toByteArray(new FileInputStream(testDataDir + "test1_modified.txt")));
        }

        IOUtils.deleteQuietly(new File(archiveName));
    }

    /**
     * Tests creating archive output streams from an unknown archiver name and output stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamFromUnknownArchiverName() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
        });
    }

    /**
     * Tests creating archive output streams to a stream that does not support write.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamToOutputStreamWithoutWriteSupport() {
        assertThrows(IOException.class, () -> {
            new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                            throw new UnsupportedOperationException();
                        }
                    });
        });
    }
}