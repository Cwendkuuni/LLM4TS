package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * Test for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream() with known archive types
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithKnownArchiveTypes() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertEquals("AR ArchiveInputStream", archiveInputStream.getClass().getName());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertEquals("Zip ArchiveInputStream", archiveInputStream.getClass().getName());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertEquals("Jar ArchiveInputStream", archiveInputStream.getClass().getName());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertEquals("Cpio ArchiveInputStream", archiveInputStream.getClass().getName());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertEquals("Dump ArchiveInputStream", archiveInputStream.getClass().getName());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertEquals("Tar ArchiveInputStream", archiveInputStream.getClass().getName());
    }

    /**
     * Test createArchiveInputStream() with unknown archive type
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveType() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", inputStream);
    }

    /**
     * Test createArchiveOutputStream() with known archive types
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithKnownArchiveTypes() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertEquals("AR ArchiveOutputStream", archiveOutputStream.getClass().getName());

        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertEquals("Zip ArchiveOutputStream", archiveOutputStream.getClass().getName());

        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertEquals("Jar ArchiveOutputStream", archiveOutputStream.getClass().getName());

        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertEquals("Cpio ArchiveOutputStream", archiveOutputStream.getClass().getName());

        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertEquals("Dump ArchiveOutputStream", archiveOutputStream.getClass().getName());

        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertEquals("Tar ArchiveOutputStream", archiveOutputStream.getClass().getName());
    }

    /**
     * Test createArchiveOutputStream() with unknown archive type
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiveType() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", outputStream);
    }

    /**
     * Test createArchiveInputStream() with no signature
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNoSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
    }

    /**
     * Test createArchiveOutputStream() autodetection
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamAutoDetection() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = null;

        // check AR
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertEquals("AR ArchiveInputStream", archiveInputStream.getClass().getName());
        archiveInputStream.close();

        // check AR again, it should return null
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertNull(archiveInputStream);

        // check ZIP
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
        archiveOutputStream.putNextEntry(jarEntry);
        OutputStream entryOutputStream = archiveOutputStream.createEntryOutput();
        entryOutputStream.write("test".getBytes());
        entryOutputStream.close();
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertEquals("Zip ArchiveInputStream", archiveInputStream.getClass().getName());
        archiveInputStream.close();

        // check JAR
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        JarArchiveEntry jarEntry2 = new JarArchiveEntry("test.txt");
        archiveOutputStream.putNextEntry(jarEntry2);
        entryOutputStream = archiveOutputStream.createEntryOutput();
        entryOutputStream.write("test".getBytes());
        entryOutputStream.close();
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertEquals("Jar ArchiveInputStream", archiveInputStream.getClass().getName());
        archiveInputStream.close();

        // check AR with bigger buffer
        inputStream = new ByteArrayInputStream(new byte[1024]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertNull(archiveInputStream);

        // check TAR
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        archiveOutputStream.putNextEntry(tarEntry);
        entryOutputStream = archiveOutputStream.createEntryOutput();
        entryOutputStream.write("test".getBytes());
        entryOutputStream.close();
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertEquals("Tar ArchiveInputStream", archiveInputStream.getClass().getName());
        archiveInputStream.close();
    }

    /**
     * Test createArchiveInputStream() with empty input stream
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithEmptyInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertEquals("Empty ArchiveInputStream", archiveInputStream.getClass().getName());
        archiveInputStream.close();
    }
}