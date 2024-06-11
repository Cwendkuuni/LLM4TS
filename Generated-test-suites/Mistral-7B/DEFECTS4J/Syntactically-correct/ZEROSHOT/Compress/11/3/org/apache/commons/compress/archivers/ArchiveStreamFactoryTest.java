package org.apache.commons.compress.archivers.test;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        // Test AR
        ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(arInputStream);
        assertEquals(ArArchiveInputStream.class, arInputStream.getClass());
        arInputStream.close();

        // Test CPIO
        ArchiveInputStream cpioInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(cpioInputStream);
        assertEquals(CpioArchiveInputStream.class, cpioInputStream.getClass());
        cpioInputStream.close();

        // Test DUMP
        ArchiveInputStream dumpInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(dumpInputStream);
        assertEquals(DumpArchiveInputStream.class, dumpInputStream.getClass());
        dumpInputStream.close();

        // Test JAR
        ArchiveInputStream jarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(jarInputStream);
        assertEquals(JarArchiveInputStream.class, jarInputStream.getClass());
        jarInputStream.close();

        // Test TAR
        ArchiveInputStream tarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(tarInputStream);
        assertEquals(TarArchiveInputStream.class, tarInputStream.getClass());
        tarInputStream.close();

        // Test ZIP
        ArchiveInputStream zipInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(zipInputStream);
        assertEquals(ZipArchiveInputStream.class, zipInputStream.getClass());
        zipInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test AR
        ArchiveOutputStream arOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(arOutputStream);
        assertEquals(ArArchiveOutputStream.class, arOutputStream.getClass());
        arOutputStream.close();

        // Test CPIO
        ArchiveOutputStream cpioOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(cpioOutputStream);
        assertEquals(CpioArchiveOutputStream.class, cpioOutputStream.getClass());
        cpioOutputStream.close();

        // Test DUMP
        ArchiveOutputStream dumpOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertNotNull(dumpOutputStream);
        assertEquals(DumpArchiveOutputStream.class, dumpOutputStream.getClass());
        dumpOutputStream.close();

        // Test JAR
        ArchiveOutputStream jarOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(jarOutputStream);
        assertEquals(JarArchiveOutputStream.class, jarOutputStream.getClass());
        jarOutputStream.close();

        // Test TAR
        ArchiveOutputStream tarOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(tarOutputStream);
        assertEquals(TarArchiveOutputStream.class, tarOutputStream.getClass());
        tarOutputStream.close();

        // Test ZIP
        ArchiveOutputStream zipOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(zipOutputStream);
        assertEquals(ZipArchiveOutputStream.class, zipOutputStream.getClass());
        zipOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInvalidArchiveInputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInvalidArchiveOutputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalid", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws ArchiveException, IOException {
        // Test AR
        byte[] arBytes = new byte[1024];
        InputStream arInputStream = new ByteArrayInputStream(arBytes);
        ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertNotNull(arArchiveInputStream);
        assertEquals(ArArchiveInputStream.class, arArchiveInputStream.getClass());
        arArchiveInputStream.close();

        // Test CPIO
        byte[] cpioBytes = new byte[1024];
        InputStream cpioInputStream = new ByteArrayInputStream(cpioBytes);
        ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertNotNull(cpioArchiveInputStream);
        assertEquals(CpioArchiveInputStream.class, cpioArchiveInputStream.getClass());
        cpioArchiveInputStream.close();

        // Test DUMP
        byte[] dumpBytes = new byte[1024];
        InputStream dumpInputStream = new ByteArrayInputStream(dumpBytes);
        ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertNotNull(dumpArchiveInputStream);
        assertEquals(DumpArchiveInputStream.class, dumpArchiveInputStream.getClass());
        dumpArchiveInputStream.close();

        // Test JAR
        byte[] jarBytes = new byte[1024];
        InputStream jarInputStream = new ByteArrayInputStream(jarBytes);
        ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertNotNull(jarArchiveInputStream);
        assertEquals(JarArchiveInputStream.class, jarArchiveInputStream.getClass());
        jarArchiveInputStream.close();

        // Test TAR
        byte[] tarBytes = new byte[1024];
        InputStream tarInputStream = new ByteArrayInputStream(tarBytes);
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertNotNull(tarArchiveInputStream);
        assertEquals(TarArchiveInputStream.class, tarArchiveInputStream.getClass());
        tarArchiveInputStream.close();

        // Test ZIP
        byte[] zipBytes = new byte[1024];
        InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(zipArchiveInputStream);
        assertEquals(ZipArchiveInputStream.class, zipArchiveInputStream.getClass());
        zipArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromBytes() throws ArchiveException, IOException {
        // Test AR
        byte[] arBytes = new byte[1024];
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertNotNull(arArchiveOutputStream);
        assertEquals(ArArchiveOutputStream.class, arArchiveOutputStream.getClass());
        arArchiveOutputStream.finish();
        arArchiveOutputStream.close();
        byte[] arOutputBytes = arOutputStream.toByteArray();
        assertEquals(arBytes.length, arOutputBytes.length);

        // Test CPIO
        byte[] cpioBytes = new byte[1024];
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertNotNull(cpioArchiveOutputStream);
        assertEquals(CpioArchiveOutputStream.class, cpioArchiveOutputStream.getClass());
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        byte[] cpioOutputBytes = cpioOutputStream.toByteArray();
        assertEquals(cpioBytes.length, cpioOutputBytes.length);

        // Test DUMP
        byte[] dumpBytes = new byte[1024];
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        assertNotNull(dumpArchiveOutputStream);
        assertEquals(DumpArchiveOutputStream.class, dumpArchiveOutputStream.getClass());
        dumpArchiveOutputStream.finish();
        dumpArchiveOutputStream.close();
        byte[] dumpOutputBytes = dumpOutputStream.toByteArray();
        assertEquals(dumpBytes.length, dumpOutputBytes.length);

        // Test JAR
        byte[] jarBytes = new byte[1024];
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertNotNull(jarArchiveOutputStream);
        assertEquals(JarArchiveOutputStream.class, jarArchiveOutputStream.getClass());
        jarArchiveOutputStream.finish();
        jarArchiveOutputStream.close();
        byte[] jarOutputBytes = jarOutputStream.toByteArray();
        assertEquals(jarBytes.length, jarOutputBytes.length);

        // Test TAR
        byte[] tarBytes = new byte[1024];
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertNotNull(tarArchiveOutputStream);
        assertEquals(TarArchiveOutputStream.class, tarArchiveOutputStream.getClass());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        byte[] tarOutputBytes = tarOutputStream.toByteArray();
        assertEquals(tarBytes.length, tarOutputBytes.length);

        // Test ZIP
        byte[] zipBytes = new byte[1024];
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertNotNull(zipArchiveOutputStream);
        assertEquals(ZipArchiveOutputStream.class, zipArchiveOutputStream.getClass());
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();
        byte[] zipOutputBytes = zipOutputStream.toByteArray();
        assertEquals(zipBytes.length, zipOutputBytes.length);
    }
}