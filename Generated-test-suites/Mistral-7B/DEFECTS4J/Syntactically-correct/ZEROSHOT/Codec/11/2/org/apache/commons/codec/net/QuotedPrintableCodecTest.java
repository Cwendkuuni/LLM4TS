import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] source = "Hello World!".getBytes();
        byte[] encoded = codec.encode(source);
        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(source, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String source = "Hello World!";
        String encoded = codec.encode(source, CharEncoding.US_ASCII);
        String decoded = codec.decode(encoded, CharEncoding.US_ASCII);

        Assert.assertEquals(source, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String source = "Hello World!";
        Object encoded = codec.encode(source);
        Object decoded = codec.decode(encoded);

        Assert.assertEquals(source, decoded);
    }

    @Test
    public void testEncodeDecodeBytesWithCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] source = "\r".getBytes();
        byte[] encoded = new byte[3];
        encoded[0] = '=';
        encoded[1] = '0';
        encoded[2] = 'D';

        byte[] decoded = codec.decode(encoded);

        Assert.assertArrayEquals(new byte[]{'\r'}, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String source = "\n";
        String encoded = "=\r\n";

        String decoded = codec.decode(encoded.getBytes(CharEncoding.US_ASCII), CharEncoding.US_ASCII);

        Assert.assertEquals(source, decoded);
    }

    @Test
    public void testEncodeDecodeBytesWithCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] source = "\r\n".getBytes();
        byte[] encoded = new byte[3];
        encoded[0] = '=';
        encoded[1] = '0';
        encoded[2] = 'D';
        byte[] encodedCR = new byte[]{ESCAPE_CHAR, 'D'};
        byte[] encodedLF = new byte[]{ESCAPE_CHAR, 'D', ESCAPE_CHAR, '0'};

        byte[] decoded = codec.decode(new byte[]{ESCAPE_CHAR, '0', ESCAPE_CHAR, 'D'}, CharEncoding.US_ASCII);

        Assert.assertArrayEquals(new byte[]{"\r", "\n"}, decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidBytes() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = new byte[]{'=', 'A', 'B', 'C'};
        codec.decode(encoded);
    }
}