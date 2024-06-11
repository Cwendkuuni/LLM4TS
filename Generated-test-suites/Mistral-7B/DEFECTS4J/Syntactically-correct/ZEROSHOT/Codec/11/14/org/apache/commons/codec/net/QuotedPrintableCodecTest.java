import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] bytes = "Hello World!".getBytes();
        byte[] encodedBytes = codec.encode(bytes);
        byte[] decodedBytes = codec.decode(encodedBytes);

        Assert.assertArrayEquals(bytes, decodedBytes);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World!";
        String encodedString = codec.encode(input);
        String decodedString = codec.decode(encodedString);

        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeDecodeByteArrayWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");

        byte[] bytes = "Hello World!".getBytes("ISO-8859-1");
        byte[] encodedBytes = codec.encode(bytes);
        byte[] decodedBytes = codec.decode(encodedBytes);

        Assert.assertArrayEquals(bytes, decodedBytes);
    }

    @Test
    public void testEncodeDecodeStringWithCustomCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");

        String input = "Hello World!";
        String encodedString = codec.encode(input, "ISO-8859-1");
        String decodedString = codec.decode(encodedString);

        Assert.assertEquals(input, decodedString);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(new Object());
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidObject() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(new byte[0]);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeInvalidCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("InvalidCharset".getBytes(), "InvalidCharset");
    }
}