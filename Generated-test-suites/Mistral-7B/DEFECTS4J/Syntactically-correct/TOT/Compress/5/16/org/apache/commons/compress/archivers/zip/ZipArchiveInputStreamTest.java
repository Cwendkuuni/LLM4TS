import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    @Test
    public void testReadValidZipFile() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
            ZipArchiveEntry entry;
            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                Assert.assertNotNull(entry.getName());
                byte[] bytes = new byte[1024];
                int length = zipArchiveInputStream.read(bytes);
                Assert.assertNotEquals(-1, length);
                Assert.assertEquals(entry.getSize(), length);
                Assert.assertArrayEquals(IOUtils.toByteArray(entry.getInputStream()), bytes, 0, length);
            }
            zipArchiveInputStream.close();
        }
    }

    @Test
    public void testReadEmptyZipFile() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
            ZipArchiveEntry entry;
            Assert.assertNull(entry = zipArchiveInputStream.getNextZipEntry());
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testReadCorruptedZipFile() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4B, 0x50, 0x00, 0x01, 0x00})) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
            zipArchiveInputStream.getNextZipEntry();
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testReadMissingCentralDirectory() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
            ZipArchiveEntry entry;
            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                zipArchiveInputStream.closeEntry();
            }
            zipArchiveInputStream.getNextZipEntry();
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeBytesToRead() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
            byte[] bytes = new byte[1];
            zipArchiveInputStream.read(bytes, -1, 1);
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testReadErrorDuringInflate() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] header = new byte[ZipArchiveInputStream.LFH_SIG.length];
            for (int i = 0; i < header.length; i++) {
                header[i] = (byte) (ZipArchiveInputStream.LFH_SIG[i] + 1);
            }
            byte[] content = new byte[1024];
            byte[] corruptedContent = new byte[1024];
            for (int i = 0; i < content.length; i++) {
                content[i] = (byte) (i % 50 + 1);
            }
            for (int i = 0; i < corruptedContent.length; i++) {
                if (i % 5 == 0) {
                    corruptedContent[i] = 0xFF;
                } else {
                    corruptedContent[i] = content[i];
                }
            }

            try (InputStream is = new ByteArrayInputStream(header)) {
                ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
                zipArchiveInputStream.putNextEntry(new ZipArchiveEntry("test.txt"), new ByteArrayInputStream(content));
                zipArchiveInputStream.write(content, 0, content.length);
                zipArchiveInputStream.closeEntry();
                zipArchiveInputStream.close();
            }

            try (InputStream is = new ByteArrayInputStream(bos.toByteArray())) {
                ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(is);
                byte[] bytes = new byte[content.length];
                int length = zipArchiveInputStream.read(bytes);
                Assert.assertArrayEquals(corruptedContent, bytes, 0, length);
                zipArchiveInputStream.close();
            }
        }
    }
}