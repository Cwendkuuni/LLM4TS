package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        // Test empty stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream emptyChecksumStream = new ChecksumCalculatingInputStream(Checksums.create(), emptyInputStream);
        long expectedChecksum = 0L;
        assertEquals(expectedChecksum, emptyChecksumStream.getValue());

        // Test stream with data
        InputStream dataInputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'Stream'});
        ChecksumCalculatingInputStream dataChecksumStream = new ChecksumCalculatingInputStream(Checksums.create(), dataInputStream);
        long expectedChecksumData = 1492617783452447339L; // Checksum for "TestStream" calculated with MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] data = new byte[5];
        int len;
        while ((len = dataInputStream.read(data, 0, data.length)) != -1) {
            md.update(data, 0, len);
        }
        expectedChecksumData = md.digest()[0] & 0xFFL |
                ((md.digest()[1] & 0xFFL) << 8) |
                ((md.digest()[2] & 0xFFL) << 16) |
                ((md.digest()[3] & 0xFFL) << 24) |
                ((md.digest()[4] & 0xFFL) << 32) |
                ((md.digest()[5] & 0xFFL) << 40) |
                ((md.digest()[6] & 0xFFL) << 48) |
                ((md.digest()[7] & 0xFFL) << 56);
        assertEquals(expectedChecksumData, dataChecksumStream.getValue());

        // Test stream with exception
        InputStream exceptionInputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream exceptionChecksumStream = new ChecksumCalculatingInputStream(Checksums.create(), exceptionInputStream);
        try {
            exceptionChecksumStream.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertEquals(0L, exceptionChecksumStream.getValue());

        // Test stream with EOF
        InputStream eofInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream eofChecksumStream = new ChecksumCalculatingInputStream(Checksums.create(), eofInputStream);
        assertEquals(0L, eofChecksumStream.getValue());
    }

}