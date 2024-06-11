package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.util.TestFiles;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    /**
     * Tests that a {@link DumpArchiveInputStream} can read a simple archive.
     */
    @Test
    public void testSimpleArchive() throws ArchiveException, IOException {
        InputStream is = new FileInputStream(new File(TestFiles.SAMPLE_DUMPFILE));

        try (ArchiveInputStream ais = new DumpArchiveInputStream(is)) {
            DumpArchiveEntry entry;

            // check that we can read all entries
            while ((entry = ais.getNextEntry()) != null) {
                Assert.assertNotNull(entry);
                Assert.assertEquals(entry.getName(), entry.getSimpleName());
            }

            // check that we hit EOF
            Assert.assertNull(ais.getNextEntry());
        }
    }

    /**
     * Tests that reading a malformed archive throws an exception.
     */
    @Test(expected = ArchiveException.class)
    public void testMalformedArchive() throws IOException {
        byte[] malformedBytes = TestFiles.readFileBytes("malformed-dumpfile.dmp");

        try (ByteArrayInputStream bais = new ByteArrayInputStream(malformedBytes)) {
            new DumpArchiveInputStream(bais);
        }
    }

    /**
     * Tests that reading a sparse archive works correctly.
     */
    @Test
    public void testSparseArchive() throws ArchiveException, IOException {
        InputStream is = new FileInputStream(new File(TestFiles.SPARSE_DUMPFILE));

        try (ArchiveInputStream ais = new DumpArchiveInputStream(is)) {
            DumpArchiveEntry entry;

            // check that we can read all non-sparse entries
            while ((entry = ais.getNextEntry()) != null && !entry.isSparseRecord(0)) {
                Assert.assertNotNull(entry);
                Assert.assertEquals(entry.getName(), entry.getSimpleName());
            }

            // check that we hit EOF or a sparse record
            Assert.assertNull(ais.getNextEntry());
        }
    }

    /**
     * Tests that writing an archive and then reading it back works correctly.
     */
    @Test
    public void testRoundTrip() throws ArchiveException, IOException {
        // create an input stream with some data
        InputStream is = new FileInputStream(new File(TestFiles.INPUTFILE));

        try (ArchiveOutputStream aos = new DumpArchiveOutputStream(
                new FileOutputStream(TestFiles.OUTPUTFILE),
                null)) {
            DumpArchiveEntry entry = new DumpArchiveEntry("test", "test", null, 0, 0);
            aos.putNextEntry(entry);

            byte[] data = new byte[1024];
            is.read(data);
            aos.write(data, 0, data.length);
            aos.closeEntry();
        }

        try (ArchiveInputStream ais = new DumpArchiveInputStream(
                new FileInputStream(TestFiles.OUTPUTFILE))) {
            DumpArchiveEntry entry = ais.getNextEntry();

            Assert.assertEquals("test", entry.getName());
            Assert.assertEquals("test", entry.getSimpleName());

            byte[] data = new byte[1024];
            int len = ais.read(data, 0, data.length);
            Assert.assertEquals(data.length, len);
            Assert.assertArrayEquals(data, TestFiles.readFileBytes(TestFiles.INPUTFILE));
        }
    }

    /**
     * Tests that a {@link DumpArchiveInputStream} can read an archive with a
     * custom encoding.
     */
    @Test
    public void testCustomEncodingArchive() throws ArchiveException, IOException {
        InputStream is = new FileInputStream(new File(TestFiles.CUSTOM_ENCODING_DUMPFILE));

        try (ArchiveInputStream ais = new DumpArchiveInputStream(is, "UTF-8")) {
            DumpArchiveEntry entry;

            // check that we can read all entries
            while ((entry = ais.getNextEntry()) != null) {
                Assert.assertNotNull(entry);
                Assert.assertEquals(entry.getName(), entry.getSimpleName());
            }

            // check that we hit EOF
            Assert.assertNull(ais.getNextEntry());
        }
    }

    /**
     * Tests that a {@link DumpArchiveInputStream} can read a zero-length archive.
     */
    @Test
    public void testZeroLengthArchive() throws ArchiveException, IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);

        try (ArchiveInputStream ais = new DumpArchiveInputStream(is)) {
            // check that we hit EOF immediately
            Assert.assertNull(ais.getNextEntry());
        }
    }
}