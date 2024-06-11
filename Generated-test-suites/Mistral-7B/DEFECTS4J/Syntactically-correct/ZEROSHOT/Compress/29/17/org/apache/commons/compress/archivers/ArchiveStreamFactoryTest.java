package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;

/**
 * Test for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream() method with correct parameters.
     */
    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR archive
        InputStream arInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.AR_ARCHIVE_DATA);
        ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.AR, arInputStream);
        assertEquals(ArArchiveInputStream.class, arArchiveInputStream.getClass());
        arArchiveInputStream.close();

        // Test ARJ archive
        InputStream arjInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.ARJ_ARCHIVE_DATA);
        ArchiveInputStream arjArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.ARJ, arjInputStream);
        assertEquals(ArjArchiveInputStream.class, arjArchiveInputStream.getClass());
        arjArchiveInputStream.close();

        // Test CPIO archive
        InputStream cpioInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.CPIO_ARCHIVE_DATA);
        ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.CPIO, cpioInputStream);
        assertEquals(CpioArchiveInputStream.class, cpioArchiveInputStream.getClass());
        cpioArchiveInputStream.close();

        // Test JAR archive
        InputStream jarInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.JAR_ARCHIVE_DATA);
        ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.JAR, jarInputStream);
        assertEquals(JarArchiveInputStream.class, jarArchiveInputStream.getClass());
        jarArchiveInputStream.close();

        // Test TAR archive
        InputStream tarInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.TAR_ARCHIVE_DATA);
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.TAR, tarInputStream);
        assertEquals(TarArchiveInputStream.class, tarArchiveInputStream.getClass());
        tarArchiveInputStream.close();

        // Test ZIP archive
        InputStream zipInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.ZIP_ARCHIVE_DATA);
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                ArchiveStreamFactory.ZIP, zipInputStream);
        assertEquals(ZipArchiveInputStream.class, zipArchiveInputStream.getClass());
        zipArchiveInputStream.close();

        // Test SEVEN_Z archive
        InputStream sevenZInputStream = new ByteArrayInputStream(
                ArchiveStreamFactoryTestConstants.SEVEN_Z_ARCHIVE_DATA);
        try {
            ArchiveInputStream sevenZArchiveInputStream = ArchiveStreamFactory.createArchiveInputStream(
                    ArchiveStreamFactory.SEVEN_Z, sevenZInputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        sevenZInputStream.close();
    }

    /**
     * Test createArchiveInputStream() method with null inputStream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    /**
     * Test createArchiveInputStream() method with unknown archiver name.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        InputStream unknownArchiveInputStream = new ByteArrayInputStream(
                new byte[0]);
        ArchiveStreamFactory.createArchiveInputStream("unknown", unknownArchiveInputStream);
    }

    /**
     * Test createArchiveOutputStream() method with correct parameters.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archive
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.AR, out);
        assertEquals(ArArchiveOutputStream.class, arArchiveOutputStream.getClass());
        arArchiveOutputStream.finish();
        out.close();

        // Test ARJ archive
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.ARJ, arjOutputStream);
        assertEquals(ArjArchiveOutputStream.class, arjArchiveOutputStream.getClass());
        arjArchiveOutputStream.finish();
        arjOutputStream.close();

        // Test CPIO archive
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertEquals(CpioArchiveOutputStream.class, cpioArchiveOutputStream.getClass());
        cpioArchiveOutputStream.finish();
        cpioOutputStream.close();

        // Test JAR archive
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.JAR, jarOutputStream);
        assertEquals(JarArchiveOutputStream.class, jarArchiveOutputStream.getClass());
        jarArchiveOutputStream.finish();
        jarOutputStream.close();

        // Test TAR archive
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.TAR, tarOutputStream);
        assertEquals(TarArchiveOutputStream.class, tarArchiveOutputStream.getClass());
        tarArchiveOutputStream.finish();
        tarOutputStream.close();

        // Test ZIP archive
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                ArchiveStreamFactory.ZIP, zipOutputStream);
        assertEquals(ZipArchiveOutputStream.class, zipArchiveOutputStream.getClass());
        zipArchiveOutputStream.finish();
        zipOutputStream.close();

        // Test SEVEN_Z archive
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream sevenZArchiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(
                    ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        sevenZOutputStream.close();
    }

    /**
     * Test createArchiveOutputStream() method with null outputStream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    /**
     * Test createArchiveOutputStream() method with unknown archiver name.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        OutputStream unknownArchiveOutputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.createArchiveOutputStream("unknown", unknownArchiveOutputStream);
    }

}