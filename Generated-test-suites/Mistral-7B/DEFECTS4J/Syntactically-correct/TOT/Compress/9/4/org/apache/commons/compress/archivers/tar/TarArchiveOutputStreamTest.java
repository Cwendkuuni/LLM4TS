package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Test class for TarArchiveOutputStream
 */
public class TarArchiveOutputStreamTest {
    /**
     * Test method for {@link TarArchiveOutputStream#TarArchiveOutputStream(OutputStream)}.
     */
    @org.junit.Test
    public void testConstructor() {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            // do nothing
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#setLongFileMode(int)}.
     */
    @org.junit.Test
    public void testSetLongFileMode() {
        try {
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOut.close();
        } catch (IOException e) {
            fail("Exception should not be thrown during setLongFileMode()");
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#finish()}.
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putNextEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.finish();

            byte[] expected = new byte[1024]; // assuming a 512 byte record size
            System.arraycopy(ArchiveUtils.HEADER, 0, expected, 0, ArchiveUtils.HEADER.length);
            System.arraycopy(ArchiveUtils.TAR_MAGIC, 0, expected, ArchiveUtils.HEADER.length, ArchiveUtils.TAR_MAGIC.length);
            System.arraycopy(entry.getHeader().getData(), 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length, entry.getHeader().getData().length);
            System.arraycopy(new byte[] { 0 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 512 - (ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length));
            assertArrayEquals("Finish method did not generate expected output", expected, out.toByteArray());

            // Check if EOF records are written
            byte[] eof = new byte[512];
            Arrays.fill(eof, (byte) 0);
            assertArrayEquals("EOF records not written", eof, new ByteArrayInputStream(new ByteArrayInputStream(out.toByteArray()).readN(512 * 2)));
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#putArchiveEntry(ArchiveEntry)}.
     */
    @org.junit.Test
    public void testPutArchiveEntry() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.closeArchiveEntry();

            byte[] expected = new byte[1024];
            System.arraycopy(ArchiveUtils.HEADER, 0, expected, 0, ArchiveUtils.HEADER.length);
            System.arraycopy(ArchiveUtils.TAR_MAGIC, 0, expected, ArchiveUtils.HEADER.length, ArchiveUtils.TAR_MAGIC.length);
            System.arraycopy(entry.getHeader().getData(), 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length, entry.getHeader().getData().length);
            System.arraycopy(new byte[] { 0 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 512 - (ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length));
            System.arraycopy(new byte[] { 0x01, 0x02, 0x03 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 3);

            assertArrayEquals("putArchiveEntry method did not generate expected output", expected, out.toByteArray());
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#putArchiveEntry(ArchiveEntry, String)}.
     */
    @org.junit.Test
    public void testPutArchiveEntry_String() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            File testFile = new File("test.txt");
            TarArchiveEntry entry = new TarArchiveEntry(testFile, "test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.closeArchiveEntry();
            tarOut.finish();
            File outputFile = new File("output.tar");
            tarOut.writeTo(outputFile);

            byte[] expected = new byte[1024];
            System.arraycopy(ArchiveUtils.HEADER, 0, expected, 0, ArchiveUtils.HEADER.length);
            System.arraycopy(ArchiveUtils.TAR_MAGIC, 0, expected, ArchiveUtils.HEADER.length, ArchiveUtils.TAR_MAGIC.length);
            System.arraycopy(entry.getHeader().getData(), 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length, entry.getHeader().getData().length);
            System.arraycopy(new byte[] { 0 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 512 - (ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length));
            System.arraycopy(new byte[] { 0x01, 0x02, 0x03 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 3);

            byte[] tarFile = Files.readAllBytes(Paths.get(outputFile.getAbsolutePath()));
            assertArrayEquals("putArchiveEntry method with File argument did not generate expected output", expected, tarFile);

            Files.delete(outputFile.toPath());
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#write(byte[], int, int)}.
     */
    @org.junit.Test
    public void testWrite() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.write(new byte[] { 0x04, 0x05, 0x06 });
            tarOut.closeArchiveEntry();

            byte[] expected = new byte[1024];
            System.arraycopy(ArchiveUtils.HEADER, 0, expected, 0, ArchiveUtils.HEADER.length);
            System.arraycopy(ArchiveUtils.TAR_MAGIC, 0, expected, ArchiveUtils.HEADER.length, ArchiveUtils.TAR_MAGIC.length);
            System.arraycopy(entry.getHeader().getData(), 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length, entry.getHeader().getData().length);
            System.arraycopy(new byte[] { 0 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 512 - (ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length));
            System.arraycopy(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 6);

            assertArrayEquals("write method did not generate expected output", expected, out.toByteArray());
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#close()}.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.closeArchiveEntry();
            tarOut.close();
        }
    }

    /**
     * Test method for {@link TarArchiveOutputStream#writeTo(File)}.
     */
    @org.junit.Test
    public void testWriteToFile() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.write(new byte[] { 0x01, 0x02, 0x03 });
            tarOut.closeArchiveEntry();
            tarOut.finish();
            File outputFile = new File("output.tar");
            tarOut.writeTo(outputFile);

            byte[] expected = new byte[1024];
            System.arraycopy(ArchiveUtils.HEADER, 0, expected, 0, ArchiveUtils.HEADER.length);
            System.arraycopy(ArchiveUtils.TAR_MAGIC, 0, expected, ArchiveUtils.HEADER.length, ArchiveUtils.TAR_MAGIC.length);
            System.arraycopy(entry.getHeader().getData(), 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length, entry.getHeader().getData().length);
            System.arraycopy(new byte[] { 0 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 512 - (ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length));
            System.arraycopy(new byte[] { 0x01, 0x02, 0x03 }, 0, expected, ArchiveUtils.HEADER.length + ArchiveUtils.TAR_MAGIC.length + entry.getHeader().getData().length, 3);

            byte[] tarFile = Files.readAllBytes(Paths.get(outputFile.getAbsolutePath()));
            assertArrayEquals("writeTo method did not generate expected output", expected, tarFile);

            Files.delete(outputFile.toPath());
        }
    }
}