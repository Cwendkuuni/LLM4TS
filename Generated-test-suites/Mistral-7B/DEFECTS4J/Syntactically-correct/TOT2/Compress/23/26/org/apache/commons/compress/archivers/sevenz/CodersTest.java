import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.Test;
import org.tukaani.xz.LZMAInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CodersTest {

    @Test
    public void testAddDecoderCopy() throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03});
        Coder coder = new CopyDecoder();
        byte[] password = new byte[0];
        InputStream result = Coders.addDecoder(inputStream, coder, password);
        byte[] expected = new byte[]{0x00, 0x01, 0x02, 0x03};
        byte[] actual = new byte[expected.length];
        int bytesRead = result.read(actual);
        assertEquals(expected.length, bytesRead);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddDecoderLZMA() throws IOException, GeneralSecurityException {
        InputStream inputStream = new LZMAInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09}));
        Coder coder = new LZMADecoder();
        byte[] password = new byte[0];
        InputStream result = Coders.addDecoder(inputStream, coder, password);
        byte[] expected = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        byte[] actual = new byte[expected.length];
        int bytesRead = result.read(actual);
        assertEquals(expected.length, bytesRead);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddDecoderUnsupportedCompressionMethod() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder coder = new AES256SHA256Decoder();
        byte[] password = new byte[0];
        assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
    }

    @Test
    public void testAddEncoderCopy() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = new byte[0];
        Coder coder = new CopyDecoder();
        OutputStream result = Coders.addEncoder(outputStream, method, password);
        assertEquals(outputStream, result);
    }

    @Test
    public void testAddEncoderLZMA() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = new byte[]{0x00, 0x01, 0x02, 0x03};
        Coder coder = new LZMADecoder();
        OutputStream result = Coders.addEncoder(outputStream, method, password);
        assertEquals(LZMAOutputStream.class, result.getClass());
    }

    @Test
    public void testAddEncoderUnsupportedCompressionMethod() {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.AES256SHA256;
        byte[] password = new byte[0];
        Coder coder = new CopyDecoder();
        assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, method, password));
    }
}