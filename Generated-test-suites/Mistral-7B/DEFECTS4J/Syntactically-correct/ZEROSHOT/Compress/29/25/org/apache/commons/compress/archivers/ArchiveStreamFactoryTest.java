package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
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
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private byte[] arData;
    private byte[] cpioData;
    private byte[] tarData;
    private byte[] zipData;

    @Before
    public void setUp() throws IOException {
        factory = new ArchiveStreamFactory();
        arData = generateARData();
        cpioData = generateCPIOData();
        tarData = generateTarData();
        zipData = generateZipData();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        verifyCreateArchiveOutputStream(ArchiveStreamFactory.AR, ArArchiveOutputStream.class, arData);
        verifyCreateArchiveOutputStream(ArchiveStreamFactory.CPIO, CpioArchiveOutputStream.class, cpioData);
        verifyCreateArchiveOutputStream(ArchiveStreamFactory.TAR, TarArchiveOutputStream.class, tarData);
        verifyCreateArchiveOutputStream(ArchiveStreamFactory.ZIP, ZipArchiveOutputStream.class, zipData);
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        verifyCreateArchiveInputStream(ArchiveStreamFactory.AR, ArArchiveInputStream.class, arData);
        verifyCreateArchiveInputStream(ArchiveStreamFactory.CPIO, CpioArchiveInputStream.class, cpioData);
        verifyCreateArchiveInputStream(ArchiveStreamFactory.TAR, TarArchiveInputStream.class, tarData);
        verifyCreateArchiveInputStream(ArchiveStreamFactory.ZIP, ZipArchiveInputStream.class, zipData);
    }

    @Test
    public void testCreateArchiveInputStreamFromAutoDetect() throws IOException, ArchiveException {
        verifyCreateArchiveInputStreamFromAutoDetect(ArchiveStreamFactory.class, ArArchiveInputStream.class, arData);
        verifyCreateArchiveInputStreamFromAutoDetect(ArchiveStreamFactory.class, CpioArchiveInputStream.class, cpioData);
        verifyCreateArchiveInputStreamFromAutoDetect(ArchiveStreamFactory.class, TarArchiveInputStream.class, tarData);
        verifyCreateArchiveInputStreamFromAutoDetect(ArchiveStreamFactory.class, ZipArchiveInputStream.class, zipData);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateUnknownArchiveInputStream() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream("unknown", is);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateUnknownArchiveOutputStream() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", os);
    }

    private void verifyCreateArchiveOutputStream(String archiverName, Class<ArchiveOutputStream> expectedClass, byte[] data)
            throws IOException, ArchiveException {
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(archiverName, os);
        assertNotNull(outputStream);
        assertEquals(expectedClass, outputStream.getClass());
        ArArchiveOutputStream arArchiveOutputStream = (ArArchiveOutputStream) outputStream;
        arArchiveOutputStream.putArchiveEntry(new ArArchiveEntry("testEntry"));
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(data));
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("testEntry", entry.getName());
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(outputStream);
    }

    private void verifyCreateArchiveInputStream(String archiverName, Class<ArchiveInputStream> expectedClass, byte[] data)
            throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new ArArchiveEntry("testEntry"));
        archiveOutputStream.writeTo(new ByteArrayInputStream(data), 0, data.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(expectedClass, archiveInputStream.getClass());
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("testEntry", entry.getName());
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    private void verifyCreateArchiveInputStreamFromAutoDetect(Class<ArchiveStreamFactory> factoryClass,
                                                             Class<ArchiveInputStream> expectedClass, byte[] data)
            throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(expectedClass, archiveInputStream.getClass());
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    private byte[] generateARData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arArchiveOutputStream = new ArArchiveOutputStream(outputStream, null);
        arArchiveOutputStream.putArchiveEntry(new ArArchiveEntry("testEntry"));
        arArchiveOutputStream.writeTo(new ByteArrayInputStream(new byte[10]), 0, 10);
        arArchiveOutputStream.closeArchiveEntry();
        arArchiveOutputStream.finish();
        IOUtils.closeQuietly(arArchiveOutputStream);
        return outputStream.toByteArray();
    }

    private byte[] generateCPIOData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, null);
        cpioArchiveOutputStream.putNextHeader(new String[]{"Name", "testEntry"}, null, 0, 1, 0);
        cpioArchiveOutputStream.writeTo(new ByteArrayInputStream(new byte[10]), 0, 10);
        cpioArchiveOutputStream.closeEntry();
        cpioArchiveOutputStream.finish();
        IOUtils.closeQuietly(cpioArchiveOutputStream);
        return outputStream.toByteArray();
    }

    private byte[] generateTarData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, null);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
        tarArchiveOutputStream.writeTo(new ByteArrayInputStream(new byte[10]), 0, 10);
        tarArchiveOutputStream.closeEntry();
        tarArchiveOutputStream.finish();
        IOUtils.closeQuietly(tarArchiveOutputStream);
        return outputStream.toByteArray();
    }

    private byte[] generateZipData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream, null);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testEntry.txt");
        zipArchiveOutputStream.putNextEntry(zipArchiveEntry);
        zipArchiveOutputStream.writeTo(new ByteArrayInputStream(new byte[10]), 0, 10);
        zipArchiveOutputStream.closeEntry();
        zipArchiveOutputStream.finish();
        IOUtils.closeQuietly(zipArchiveOutputStream);
        return outputStream.toByteArray();
    }

}