import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = "Hello World!".getBytes();
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World!";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeByteArrayWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        byte[] input = "Hello World!".getBytes("UTF-8");
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        String input = "Hello World!";
        String encoded = codec.encode(input, "UTF-8");
        String decoded = codec.decode(encoded, "UTF-8");

        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeByteArrayWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        byte[] input = "Hello World!".getBytes("UTF-8");
        byte[] encoded = codec.encode(input);

        String decodedString = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(new String("Hello World!", "UTF-8"), decodedString);
    }

    @Test
    public void testEncodeStringWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");

        String input = "Hello World!";
        byte[] encoded = codec.encode(input.getBytes(), "UTF-8");

        String decodedString = new String(codec.decode(encoded), "UTF-8");
        Assert.assertEquals(input, decodedString);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'=', '='};
        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeMissingHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'=', '=', '=', '='};
        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHex() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'=', 'A', 'B', '='};
        codec.decode(encoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeUnsupportedCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = "Hello World!".getBytes();
        codec.decode(encoded, "ISO-8859-15");
    }
}