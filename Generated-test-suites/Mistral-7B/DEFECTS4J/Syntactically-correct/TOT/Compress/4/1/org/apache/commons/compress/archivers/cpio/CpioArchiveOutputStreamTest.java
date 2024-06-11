package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream archiveOutputStream;
    private CpioArchiveEntry archiveEntry;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(outputStream,
                CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntryWithValidCpioArchiveEntry() throws Exception {
        // Given
        File fileToAdd = mock(File.class);
        when(fileToAdd.getName()).thenReturn("testfile.txt");
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);

        // When
        archiveOutputStream.putArchiveEntry(archiveEntry);
        archiveOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        archiveOutputStream.closeArchiveEntry();

        // Then
        assertEquals("testfile.txt\012" + ArchiveUtils.toAsciiBytes(
                CpioConstants.MAGIC_NEW) + "\000" + ArchiveUtils.toAsciiBytes(
                Long.toByteArray(archiveEntry.getInode(), 8)).concat(
                ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getMode(), 8)))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getUID(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getGID(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getNumberOfLinks(), 8))
                + Long.toByteArray(System.currentTimeMillis()) // time
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getSize(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getDeviceMaj(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getDeviceMin(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getRemoteDeviceMaj(), 8))
                + ArchiveUtils.toAsciiBytes(Long.toByteArray(archiveEntry.getRemoteDeviceMin(), 8))
                + Long.toByteArray(archiveEntry.getName().length() + 1)
                + Long.toByteArray(archiveEntry.getChksum(), 8) + "\0",
                outputStream.toByteArray(), 0, outputStream.size());
    }

    @Test
    public void testPutArchiveEntryWithInvalidCpioArchiveEntryFormat() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setFormat((short) 123);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        assertThrows(IOException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });

        // Then
        assertEquals("duplicate entry: testfile.txt",
                archiveOutputStream.getMessage(), "Stream closed");
    }

    @Test
    public void testPutArchiveEntryWithNullCpioArchiveEntry() throws Exception {
        // Given
        archiveEntry = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithEmptyName() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithEmptyFileSize() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setFileSize(0);

        // When
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // Then
        // Empty file is not written to the output stream
        byte[] output = outputStream.toByteArray();
        assertNull(ArchiveUtils.findEntryWithName("", output, 0, output.length));
    }

    @Test
    public void testPutArchiveEntryWithNegativeFileSize() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setFileSize(-10);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithBigFileSize() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setFileSize(Long.MAX_VALUE);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithNullOutputStream() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new CpioArchiveOutputStream(null, archiveEntry.getFormat());
            archiveOutputStream = new CpioArchiveOutputStream(null,
                    archiveEntry.getFormat());
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithInvalidOutputFormat() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream = new CpioArchiveOutputStream(outputStream,
                    (short) 123);
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testPutArchiveEntryWithNullName() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    @Test
    public void testWriteWithValidData() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        archiveOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));

        // Then
        archiveOutputStream.closeArchiveEntry();
        assertEquals("Test data", new String(archiveOutputStream.getBuffer()
                .array(), 0, archiveOutputStream.size(), StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteWithNullData() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        archiveOutputStream.write(null);

        // Then
        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testWriteWithEmptyData() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(0);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        archiveOutputStream.write(new byte[0]);

        // Then
        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testWriteWithNegativeDataLength() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(10);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.write(new byte[(-10)]);
        });

        // Then
        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testWriteWithBigDataLength() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName("testfile.txt");
        archiveEntry.setFileSize(Long.MAX_VALUE);
        archiveEntry.setMode(CpioConstants.C_ISREG);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            archiveOutputStream.write(new byte[Long.MAX_VALUE + 1]);
        });
    }

    @Test
    public void testFinish() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(CpioConstants.CPIO_TRAILER);
        archiveEntry.setNumberOfLinks(1);
        archiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        archiveOutputStream.finish();

        // Then
        assertEquals(CpioConstants.MAGIC_NEW_CRC,
                archiveOutputStream.getBuffer(), ArchiveUtils.getMagicNumber(
                        CpioConstants.FORMAT_NEW_CRC, 2), 0);
        assertEquals(CpioConstants.MAGIC_TRAILER,
                archiveOutputStream.getBuffer(), ArchiveUtils.getMagicNumber(
                        CpioConstants.CPIO_TRAILER, 1), 0);
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testFinishWithError() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(CpioConstants.CPIO_TRAILER);
        archiveEntry.setNumberOfLinks(1);
        archiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        assertThrows(IOException.class, () -> {
            archiveOutputStream.write("Error data".getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.finish();
        });

        // Then
        assertFalse(archiveOutputStream.closed());
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testClose() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(CpioConstants.CPIO_TRAILER);
        archiveEntry.setNumberOfLinks(1);
        archiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        archiveOutputStream.close();

        // Then
        assertTrue(archiveOutputStream.closed());
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testCloseWithError() throws Exception {
        // Given
        archiveEntry = new CpioArchiveEntry();
        archiveEntry.setName(CpioConstants.CPIO_TRAILER);
        archiveEntry.setNumberOfLinks(1);
        archiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // When
        assertThrows(IOException.class, () -> {
            archiveOutputStream.write("Error data".getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.close();
        });

        // Then
        assertFalse(archiveOutputStream.closed());
        assertNull(archiveOutputStream.getBuffer());
    }

    @Test
    public void testCreateArchiveEntryWithValidFile() throws Exception {
        // Given
        Path filePath = Paths.get("testfile.txt");
        archiveEntry = archiveOutputStream.createArchiveEntry(filePath.toFile(),
                "testfile.txt");

        // When
        assertEquals("testfile.txt", archiveEntry.getName());
        assertEquals(Files.size(filePath), archiveEntry.getSize());
        assertEquals(CpioConstants.C_ISREG, archiveEntry.getMode());

        // Then
        archiveOutputStream.putArchiveEntry(archiveEntry);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        byte[] archiveData = outputStream.toByteArray();

        // verify that the archive entry has the correct size and checksum
        byte[] archiveEntryData = ArchiveUtils.findEntryWithName("testfile.txt",
                archiveData, 0, archiveData.length);
        assertEquals(archiveEntry.getSize(), new Long(CpioUtil.toLong(archiveEntryData, 0, 8)));
        assertEquals(CpioUtil.adler32(archiveEntryData), CpioUtil.adler32(Files.readAllBytes(filePath)));
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws Exception {
        // Given
        archiveEntry = archiveOutputStream.createArchiveEntry(null, "testfile.txt");

        // When & Then
        assertNull(archiveEntry);
    }

    @Test
    public void testCreateArchiveEntryWithNullName() throws Exception {
        // Given
        archiveEntry = archiveOutputStream.createArchiveEntry(new File("testfile.txt"), null);

        // When & Then
        assertNull(archiveEntry);
    }

    @Test
    public void testCreateArchiveEntryWithEmptyName() throws Exception {
        // Given
        archiveEntry = archiveOutputStream.createArchiveEntry(new File("testfile.txt"), "");

        // When & Then
        assertNotNull(archiveEntry);
        assertEquals("", archiveEntry.getName());
    }
}