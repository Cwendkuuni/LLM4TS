import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * QuotedPrintableCodecTest
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws EncoderException, DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Test encoding and decoding ASCII characters
        String original = "This is a test.";
        byte[] encoded = codec.encode(original);
        String decoded = new String(codec.decode(encoded), codec.getDefaultCharset());
        Assert.assertEquals(original, decoded);

        // Test encoding and decoding binary data
        byte[] binaryData = {0x01, 0x02, 0x03, 0x04};
        byte[] encodedBinaryData = codec.encode(binaryData);
        byte[] decodedBinaryData = codec.decode(encodedBinaryData);
        Assert.assertArrayEquals(binaryData, decodedBinaryData);

        // Test encoding and decoding special characters
        String specialChars = "\r\n\t =";
        byte[] encodedSpecialChars = codec.encode(specialChars.getBytes(codec.getDefaultCharset()));
        String decodedSpecialChars = new String(codec.decode(encodedSpecialChars), codec.getDefaultCharset());
        Assert.assertEquals(specialChars, decodedSpecialChars);

        // Test encoding and decoding unprintable characters
        String unprintableChars = "\u0001\u007F";
        byte[] encodedUnprintableChars = codec.encode(unprintableChars.getBytes(codec.getDefaultCharset()));
        String decodedUnprintableChars = new String(codec.decode(encodedUnprintableChars), codec.getDefaultCharset());
        Assert.assertEquals(unprintableChars, decodedUnprintableChars);
    }

    @Test
    public void testEncodeDecodeEmptyString() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String original = "";
        byte[] encoded = codec.encode(original.getBytes(codec.getDefaultCharset()));
        String decoded = new String(codec.decode(encoded), codec.getDefaultCharset());
        Assert.assertEquals(original, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String invalidString = "This is an invalid string.";
        codec.encode(invalidString.getBytes(codec.getDefaultCharset()));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeException() throws DecoderException, IOException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] invalidData = {0x01, 0x02, 0x03, 0x7F};
        codec.decode(invalidData);
    }
}