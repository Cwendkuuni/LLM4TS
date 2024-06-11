package org.apache.commons.compress.test.archivers;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.*;
import org.apache.commons.compress.archivers.arj.*;
import org.apache.commons.compress.archivers.cpio.*;
import org.apache.commons.compress.archivers.dump.*;
import org.apache.commons.compress.archivers.jar.*;
import org.apache.commons.compress.archivers.sevenz.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

/**
 * Test for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateInputStream() throws Exception {
        // Test AR
        InputStream in = new ByteArrayInputStream(ArchiveStreamFactory.AR.getTestData());
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.AR, ais.getFormat());
        ais.close();

        // Test ARJ
        in = new ByteArrayInputStream(ArchiveStreamFactory.ARJ.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.ARJ, ais.getFormat());
        ais.close();

        // Test CPIO
        in = new ByteArrayInputStream(ArchiveStreamFactory.CPIO.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.CPIO, ais.getFormat());
        ais.close();

        // Test Dump
        in = new ByteArrayInputStream(ArchiveStreamFactory.DUMP.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.DUMP, ais.getFormat());
        ais.close();

        // Test JAR
        in = new ByteArrayInputStream(ArchiveStreamFactory.JAR.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.JAR, ais.getFormat());
        ais.close();

        // Test TAR
        in = new ByteArrayInputStream(ArchiveStreamFactory.TAR.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.TAR, ais.getFormat());
        ais.close();

        // Test ZIP
        in = new ByteArrayInputStream(ArchiveStreamFactory.ZIP.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        assertEquals(ArchiveStreamFactory.ZIP, ais.getFormat());
        ais.close();

        // Test 7z
        in = new ByteArrayInputStream(ArchiveStreamFactory.SEVEN_Z.getTestData());
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, in);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test unknown format
        in = new ByteArrayInputStream("Test data".getBytes());
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test null input stream
        in = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test an input stream without mark support
        in = new ByteArrayInputStream(ArchiveStreamFactory.ZIP.getTestData());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateOutputStream() throws Exception {
        // Test AR
        final OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        aos.putArchiveEntry(new ArArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeArchiveEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.AR, aos.getFormat());

        // Test ARJ
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, out);
        aos.putArchiveEntry(new ArjArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeArchiveEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.ARJ, aos.getFormat());

        // Test CPIO
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        aos.putArchiveEntry(new CpioArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeArchiveEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.CPIO, aos.getFormat());

        // Test Dump
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        aos.putArchiveEntry(new DumpArchiveEntry("testdata/test1.xml", "testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeArchiveEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.DUMP, aos.getFormat());

        // Test JAR
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        aos.putNextEntry(new JarArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.JAR, aos.getFormat());

        // Test TAR
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        aos.putNextEntry(new TarArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.TAR, aos.getFormat());

        // Test ZIP
        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        aos.putNextEntry(new ZipArchiveEntry("testdata/test1.xml"));
        IOUtils.copy(new FileInputStream(new File("testdata/test1.xml")), aos);
        aos.closeEntry();
        aos.finish();
        aos.close();
        assertEquals(ArchiveStreamFactory.ZIP, aos.getFormat());

        // Test 7z
        out = new ByteArrayOutputStream();
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test unknown format
        out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test null output stream
        out = null;
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateInputStreamFromSignature() throws Exception {
        // Test AR
        InputStream in = new ByteArrayInputStream(ArchiveStreamFactory.AR.getTestData());
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.AR, ais.getFormat());
        ais.close();

        // Test ARJ
        in = new ByteArrayInputStream(ArchiveStreamFactory.ARJ.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.ARJ, ais.getFormat());
        ais.close();

        // Test CPIO
        in = new ByteArrayInputStream(ArchiveStreamFactory.CPIO.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.CPIO, ais.getFormat());
        ais.close();

        // Test Dump
        in = new ByteArrayInputStream(ArchiveStreamFactory.DUMP.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.DUMP, ais.getFormat());
        ais.close();

        // Test JAR
        in = new ByteArrayInputStream(ArchiveStreamFactory.JAR.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.JAR, ais.getFormat());
        ais.close();

        // Test TAR
        in = new ByteArrayInputStream(ArchiveStreamFactory.TAR.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.TAR, ais.getFormat());
        ais.close();

        // Test ZIP
        in = new ByteArrayInputStream(ArchiveStreamFactory.ZIP.getTestData());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertEquals(ArchiveStreamFactory.ZIP, ais.getFormat());
        ais.close();

        // Test 7z
        in = new ByteArrayInputStream(ArchiveStreamFactory.SEVEN_Z.getTestData());
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(in);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test unknown format
        in = new ByteArrayInputStream("Test data".getBytes());
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ais);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(ais);
        }

        // Test null input stream
        in = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ais);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static byte[] getTestData(String format) {
        switch (format) {
            case ArchiveStreamFactory.AR:
                return ArchiveStreamFactory.AR.getTestData();
            case ArchiveStreamFactory.ARJ:
                return ArchiveStreamFactory.ARJ.getTestData();
            case ArchiveStreamFactory.CPIO:
                return ArchiveStreamFactory.CPIO.getTestData();
            case ArchiveStreamFactory.DUMP:
                return ArchiveStreamFactory.DUMP.getTestData();
            case ArchiveStreamFactory.JAR:
                return ArchiveStreamFactory.JAR.getTestData();
            case ArchiveStreamFactory.TAR:
                return ArchiveStreamFactory.TAR.getTestData();
            case ArchiveStreamFactory.ZIP:
                return ArchiveStreamFactory.ZIP.getTestData();
            case ArchiveStreamFactory.SEVEN_Z:
                return ArchiveStreamFactory.SEVEN_Z.getTestData();
            default:
                throw new IllegalArgumentException("Format: " + format + " not supported.");
        }
    }
}