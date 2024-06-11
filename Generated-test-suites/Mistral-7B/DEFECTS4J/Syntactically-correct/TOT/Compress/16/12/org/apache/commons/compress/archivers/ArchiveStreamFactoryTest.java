package org.apache.commons.compress.archivers.factory;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.exception.ArchiveException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String arName = ArchiveStreamFactory.AR;
        String cpioName = ArchiveStreamFactory.CPIO;
        String zipName = ArchiveStreamFactory.ZIP;
        String unknownName = "unknown";

        // Test with valid archiver names
        for (String archiverName : new String[] {arName, cpioName, zipName}) {
            byte[] testData = new byte[1024];
            InputStream testDataStream = new ByteArrayInputStream(testData);

            ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, testDataStream);
            assertNotNull(in);
            assertEquals(archiverName, in.getFormat());
            in.close();
        }

        // Test with invalid archiver name
        InputStream testDataStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream in = null;
        try {
            in = ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownName, testDataStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String arName = ArchiveStreamFactory.AR;
        String cpioName = ArchiveStreamFactory.CPIO;
        String zipName = ArchiveStreamFactory.ZIP;
        String unknownName = "unknown";

        // Test with valid archiver names
        for (String archiverName : new String[] {arName, cpioName, zipName}) {
            OutputStream outputStream = new ByteArrayOutputStream();

            ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertNotNull(out);
            assertEquals(archiverName, out.getFormat());
            out.finish();
            out.close();
        }

        // Test with invalid archiver name
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = null;
        try {
            out = ArchiveStreamFactory.getInstance().createArchiveOutputStream(unknownName, outputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            if (out != null) {
                try {
                    out.finish();
                    out.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}