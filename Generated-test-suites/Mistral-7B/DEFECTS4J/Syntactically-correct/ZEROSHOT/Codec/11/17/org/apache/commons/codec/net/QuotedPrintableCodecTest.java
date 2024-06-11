import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of single bytes.
     */
    @Test
    public void testEncodeDecodeSingleBytes() throws EncoderException, DecoderException, UnsupportedEncodingException {
        for (byte b = 0; b <= 0xFF; b++) {
            byte[] encoded = CODEC.encode(new byte[]{b});
            byte decoded[] = CODEC.decode(encoded);
            Assert.assertArrayEquals("Byte encoding/decoding failed for byte: " + b, new byte[]{b}, decoded);
        }
    }

    /**
     * Test encoding and decoding of arrays of bytes.
     */
    @Test
    public void testEncodeDecodeArrays() throws EncoderException, DecoderException, UnsupportedEncodingException {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] encoded = CODEC.encode(input);
        byte[] decoded = CODEC.decode(encoded);
        Assert.assertArrayEquals("Byte encoding/decoding failed", input, decoded);
    }

    /**
     * Test encoding and decoding of strings.
     */
    @Test
    public void testEncodeDecodeStrings() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "Test input";
        String encoded = CODEC.encode(input.getBytes("UTF-8"));
        String decoded = new String(CODEC.decode(encoded.getBytes("UTF-8")), "UTF-8");
        Assert.assertEquals("String encoding/decoding failed", input, decoded);
    }

    /**
     * Test decoding of malformed quoted-printable text.
     */
    @Test(expected = DecoderException.class)
    public void testMalformedDecoding() throws DecoderException, UnsupportedEncodingException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR, ESCAPE_CHAR};
        CODEC.decode(input);
    }

    /**
     * Test encoding and decoding of whitespace characters.
     */
    @Test
    public void testEncodeDecodeWhitespace() throws EncoderException, DecoderException, UnsupportedEncodingException {
        String input = "\t\t Test input \t\t";
        String encoded = CODEC.encode(input.getBytes("UTF-8"));
        String decoded = new String(CODEC.decode(encoded.getBytes("UTF-8")), "UTF-8");
        Assert.assertEquals("Whitespace encoding/decoding failed", input, decoded);
    }
}