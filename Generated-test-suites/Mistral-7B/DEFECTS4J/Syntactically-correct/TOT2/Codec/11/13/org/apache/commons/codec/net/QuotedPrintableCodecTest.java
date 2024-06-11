import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    /**
     * Test encoding and decoding of ASCII printable characters.
     */
    @Test
    public void testEncodeAndDecodePrintable() throws UnsupportedEncodingException {
        String input = "Hello World!";
        byte[] encoded = codec.encode(input.getBytes("UTF-8"));
        String decoded = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(input, decoded);
    }

    /**
     * Test encoding of non-printable characters.
     */
    @Test
    public void testEncodeNonPrintable() throws UnsupportedEncodingException {
        String input = "Hello\u0001World!";
        byte[] encoded = codec.encode(input.getBytes("UTF-8"));
        String decoded = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(input, decoded);
    }

    /**
     * Test decoding of escaped characters.
     */
    @Test
    public void testDecodeEscaped() {
        byte[] input = new byte[]{'H', 'e', 'l', 'l', 'o', '=', '=', ' ', 'W', 'o', 'r', 'l', 'd', '=', '=', '!'};
        byte[] decoded = codec.decode(input);
        String expected = "Hello World!";
        Assert.assertArrayEquals(decoded, codec.encode(expected.getBytes("UTF-8")));
    }

    /**
     * Test decoding of invalidly encoded data.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() {
        byte[] input = new byte[]{'H', 'e', 'l', 'l', 'o', '=', '=', ' ', 'W', 'o', 'r', 'l', 'd', '=', '=', '!', '='};
        codec.decode(input);
    }

    /**
     * Test encoding of long input lines.
     */
    @Test
    public void testEncodeLongLines() throws UnsupportedEncodingException {
        String longLine = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, " +
                "pulvinar sit amet, lacinia quis, fostus eget, ultrices nulla. Cras porttitor metus";
        byte[] encoded = codec.encode(longLine.getBytes("UTF-8"));
        String decoded = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(longLine, decoded);
    }

    /**
     * Test decoding of long input lines.
     */
    @Test
    public void testDecodeLongLines() throws UnsupportedEncodingException, DecoderException {
        String longLine = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\r\n" +
                "Sed non risus.\r\n" +
                "Suspendisse lectus tortor,\r\n" +
                "pulvinar sit amet, lacinia quis,\r\n" +
                "fostus eget,\r\n" +
                "ultrices nulla.\r\n" +
                "Cras porttitor metus";
        byte[] encoded = codec.encode(longLine.getBytes("UTF-8"));
        String decoded = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(longLine, decoded);
    }
}