package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * Unit tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        InputStream input = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInput = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, input);
        assertSame(ZipArchiveInputStream.class, archiveInput.getClass());
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamWithUnknownFormat() throws Exception {
        String archiveName = "unknown";
        InputStream input = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
            ArchiveInputStream archiveInput = factory.createArchiveInputStream(archiveName, input);
            // Should not reach here
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("Archiver: unknown not found."));
        }
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.JAR;
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, output);
        assertSame(JarArchiveOutputStream.class, archiveOutput.getClass());
    }

    @org.junit.Test
    public void testCreateArchiveOutputStreamWithOutputStreamClosed() throws Exception {
        String archiveName = ArchiveStreamFactory.AR;
        OutputStream output = new ByteArrayOutputStream();
        output.close();

        try {
            ArchiveOutputStream archiveOutput = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, output);
            // Should not reach here
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("Archiver OutputStream is closed."));
        }
    }

    @org.junit.Test
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws Exception {
        String archiveName = "unknown";
        OutputStream output = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOutput = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, output);
            // Should not reach here
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("Archiver: unknown not found."));
        }
    }

    @org.junit.Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        OutputStream output = null;
        try {
            ArchiveOutputStream archiveOutput = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, output);
            // Should not reach here
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("OutputStream must not be null."));
        }
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamFromInputSignature() throws Exception {
        String archiveName = "tar";
        InputStream input = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInput = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, input);
        assertSame(TarArchiveInputStream.class, archiveInput.getClass());
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamFromUnknownInputSignature() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveInputStream archiveInput = ArchiveStreamFactory.newInstance().createArchiveInputStream(null, input);
            // Should not reach here
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("No Archiver found for the stream signature"));
        }
    }

}