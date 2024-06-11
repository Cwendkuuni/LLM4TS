package org.apache.commons.compress.test.archivers;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(factory);
    }

    /**
     * Test creation of archive input streams from known archive types.
     */
    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test with a byte array input stream
        final byte[] testData = new byte[1024];
        final InputStream in = new ByteArrayInputStream(testData);
        final ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertEquals(ZipArchiveInputStream.class, ais.getClass());
        IOUtils.closeQuietly(ais);

        // Test with a file input stream
        final InputStream fileIn = new FileInputStream("test.zip");
        final ArchiveInputStream aisFile = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, fileIn);
        assertEquals(ZipArchiveInputStream.class, aisFile.getClass());
        IOUtils.closeQuietly(aisFile);
    }

    /**
     * Test creating archive input streams with unknown archive types.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException, ArchiveException {
        final InputStream is = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("UnknownFormat", is);
    }

    /**
     * Test creating archive output streams from known archive types.
     */
    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        final OutputStream os = new ByteArrayOutputStream();
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);
        assertEquals(ZipArchiveOutputStream.class, aos.getClass());
        IOUtils.closeQuietly(aos);

        final File file = new File("test.zip");
        final ArchiveOutputStream aosFile = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, file);
        assertEquals(ZipArchiveOutputStream.class, aosFile.getClass());
        IOUtils.closeQuietly(aosFile);
    }

    /**
     * Test creating archive output streams with unknown archive types.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws IOException, ArchiveException {
        final OutputStream os = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("UnknownFormat", os);
    }

    /**
     * Test creating archive input streams from an unknown archive format detected from the first few bytes of the input stream.
     */
    @Test
    public void testCreateArchiveInputStreamAutodetection() throws IOException, ArchiveException {
        final byte[] testData = new byte[1024];
        final InputStream is = new ByteArrayInputStream(testData);
        final ArchiveInputStream ais = factory.createArchiveInputStream(is);
        // The actual archive type is not known at this point
        assertNull(ais);
    }

    /**
     * Test creating archive input streams from an unknown archive format with a known signature.
     */
    @Test
    public void testCreateArchiveInputStreamAutodetectionWithSignature() throws IOException, ArchiveException {
        final byte[] testData = new byte[1024];
        final InputStream is = new ByteArrayInputStream(testData);
        // Provide the signature for a known archive type
        final byte[] signature = new byte[12];
        IOUtils.readFully(is, signature);
        is.reset();

        // Set the signature for the factory
        factory.setSignature(signature);

        final ArchiveInputStream ais = factory.createArchiveInputStream(is);
        // The actual archive type is now known
        assertTrue(ais instanceof TarArchiveInputStream);
        IOUtils.closeQuietly(ais);
    }

    /**
     * Test creating archive output streams with the entry encoding.
     */
    @Test
    public void testCreateArchiveOutputStreamWithEntryEncoding() throws IOException, ArchiveException {
        final OutputStream os = new ByteArrayOutputStream();
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, os, "UTF-8");
        assertEquals("UTF-8", aos.getEncoding());
        IOUtils.closeQuietly(aos);
    }

    /**
     * Test creating archive output streams without the entry encoding.
     */
    @Test
    public void testCreateArchiveOutputStreamWithoutEntryEncoding() throws IOException, ArchiveException {
        final OutputStream os = new ByteArrayOutputStream();
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);
        assertNull(aos.getEncoding());
        IOUtils.closeQuietly(aos);
    }

    /**
     * Test creating archive input streams with the entry encoding.
     */
    @Test
    public void testCreateArchiveInputStreamWithEntryEncoding() throws IOException, ArchiveException {
        final byte[] testData = new byte[1024];
        final InputStream is = new ByteArrayInputStream(testData);
        final ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, is, "UTF-8");
        assertEquals("UTF-8", ais.getEncoding());
        IOUtils.closeQuietly(ais);
    }

    /**
     * Test creating archive input streams without the entry encoding.
     */
    @Test
    public void testCreateArchiveInputStreamWithoutEntryEncoding() throws IOException, ArchiveException {
        final byte[] testData = new byte[1024];
        final InputStream is = new ByteArrayInputStream(testData);
        final ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
        assertNull(ais.getEncoding());
        IOUtils.closeQuietly(ais);
    }

    /**
     * Test that creating an archive output stream with a null output stream throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException, ArchiveException {
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    /**
     * Test that creating an archive output stream with a null factory throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullFactory() throws IOException, ArchiveException {
        final OutputStream os = new ByteArrayOutputStream();
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(null, os);
    }

    /**
     * Test that creating an archive input stream with a null input stream throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException, ArchiveException {
        final ArchiveInputStream ais = factory.createArchiveInputStream(null);
    }

    /**
     * Test that creating an archive input stream with a null factory throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullFactory2() throws IOException, ArchiveException {
        final InputStream is = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream ais = factory.createArchiveInputStream(null, is);
    }

    /**
     * Test creating archive input streams from different archive types with different signatures.
     */
    @Test
    public void testCreateArchiveInputStreamWithDifferentSignatures() throws IOException, ArchiveException {
        final byte[] testData1 = new byte[1024];
        final byte[] testData2 = new byte[1024];
        final InputStream is1 = new ByteArrayInputStream(testData1);
        final InputStream is2 = new ByteArrayInputStream(testData2);
        final ArchiveInputStream ais1 = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, is1);
        final ArchiveInputStream ais2 = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, is2);
        assertFalse(ais1.getClass().equals(ais2.getClass()));
        IOUtils.closeQuietly(ais1);
        IOUtils.closeQuietly(ais2);
    }

    /**
     * Test creating archive output streams from different archive types with different signatures.
     */
    @Test
    public void testCreateArchiveOutputStreamWithDifferentSignatures() throws IOException, ArchiveException {
        final byte[] testDataOut1 = new byte[1024];
        final byte[] testDataOut2 = new byte[1024];
        final OutputStream os1 = new ByteArrayOutputStream();
        final OutputStream os2 = new ByteArrayOutputStream();
        final ArchiveOutputStream aos1 = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, os1);
        final ArchiveOutputStream aos2 = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, os2);
        assertFalse(aos1.getClass().equals(aos2.getClass()));
        IOUtils.closeQuietly(aos1);
        IOUtils.closeQuietly(aos2);
    }

    /**
     * Test creating archive input streams from a different archive type than the one specified with the signature.
     */
    @Test
    public void testCreateArchiveInputStreamWithMismatchedSignatures() throws IOException, ArchiveException {
        final byte[] testData = new byte[1024];
        final InputStream is = new ByteArrayInputStream(testData);
        final byte[] zipSignature = new byte[12];
        IOUtils.readFully(is, zipSignature);
        is.reset();

        // Set the signature for a ZIP archive
        factory.setSignature(zipSignature);

        // Try to create a Tar archive input stream
        final ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, is);
        assertNull(ais);
    }

    /**
     * Test creating archive output streams from a different archive type than the one specified with the signature.
     */
    @Test
    public void testCreateArchiveOutputStreamWithMismatchedSignatures() throws IOException, ArchiveException {
        final byte[] testDataOut = new byte[1024];
        final OutputStream os = new ByteArrayOutputStream();
        final byte[] zipSignature = new byte[12];
        final ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);

        // Set the signature for a Tar archive
        factory.setSignature(new byte[12]);

        // Try to write a ZIP archive
        aos.putNextEntry(new JarArchiveEntry("test.xml"));
        aos.write(testDataOut, 0, testDataOut.length);
        aos.closeArchiveEntry();
        aos.finish();

        // Check that the output stream still contains the original test data
        assertSame(testDataOut, os.toByteArray());

        IOUtils.closeQuietly(aos);
    }
}