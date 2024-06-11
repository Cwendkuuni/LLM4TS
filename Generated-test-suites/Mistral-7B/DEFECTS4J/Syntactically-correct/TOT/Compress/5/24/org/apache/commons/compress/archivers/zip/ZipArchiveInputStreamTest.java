package org.apache.commons.compress.test;

import org.apache.commons.compress.archivers.zip.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testReadLocalFileHeader() throws Exception {
        try (Path filePath = Paths.get("test.zip");
             FileInputStream inputStream = new FileInputStream(filePath.toFile());
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {

            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            assertTrue(entry != null);
            assertEquals("test.txt", entry.getName());

            byte[] data = new byte[10];
            int bytesRead = zipArchiveInputStream.read(data, 0, data.length);
            assertTrue(bytesRead > 0);
            assertArrayEquals(new byte[]{'T', 'e', 's', 't', '.', 't', 'x', 't'}, data);

            zipArchiveInputStream.closeEntry();
            entry = zipArchiveInputStream.getNextZipEntry();
            assertNull(entry);
            zipArchiveInputStream.close();
        }
    }

    @Test
    public void testReadCentralDirectory() throws Exception {
        try (Path filePath = Paths.get("test.zip");
             FileInputStream inputStream = new FileInputStream(filePath.toFile());
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {

            ZipArchiveEntry entry;
            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                // Process each entry here, e.g. check the size, CRC etc.
            }
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = EOFException.class)
    public void testReachEndOfFile() throws Exception {
        try (Path filePath = Paths.get("test.zip");
             FileInputStream inputStream = new FileInputStream(filePath.toFile());
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {

            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            zipArchiveInputStream.getNextZipEntry(); // consume next entry

            byte[] data = new byte[1];
            zipArchiveInputStream.read(data, 0, data.length); // read empty entry

            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidSignature() throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01});
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {

            zipArchiveInputStream.getNextZipEntry(); // consume first entry
            zipArchiveInputStream.close();
        }
    }
}