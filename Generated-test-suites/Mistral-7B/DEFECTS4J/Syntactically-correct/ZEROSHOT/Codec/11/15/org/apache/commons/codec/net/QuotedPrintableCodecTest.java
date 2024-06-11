import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World! tab space \t \r \n";
        byte[] encodedBytes = codec.encode(input.getBytes(CharEncoding.UTF_8));
        String decodedString = codec.decode(encodedBytes, CharEncoding.UTF_8);

        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeBinary() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21};
        byte[] encodedBytes = codec.encode(input);

        byte[] decodedBytes = codec.decode(encodedBytes);

        Assert.assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testEncodeString() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World! tab space \t \r \n";
        String encodedString = codec.encode(input, CharEncoding.UTF_8);

        String decodedString = codec.decode(encodedString, CharEncoding.UTF_8);

        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeAndDecodeBinary() throws EncoderException, DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] input = {0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21};
        byte[] encodedBytes = codec.encode(input);
        byte[] decodedBytes = codec.decode(encodedBytes);

        Assert.assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testEncodeAndDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String input = "Hello World! tab space \t \r \n";
        String encodedString = codec.encode(input, CharEncoding.UTF_8);
        String decodedString = codec.decode(encodedString, CharEncoding.UTF_8);

        Assert.assertEquals(input, decodedString);
    }

    @Test
    public void testEncodeBinaryWithBitSet() throws EncoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet printableChars = new BitSet(256);
        printableChars.set(32, 126); // set all printable ASCII characters

        byte[] input = {0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21};
        byte[] encodedBytes = codec.encode(input, printableChars);

        byte[] decodedBytes = codec.decode(encodedBytes, printableChars);

        Assert.assertArrayEquals(input, decodedBytes);
    }

    @Test
    public void testDecodeBinaryWithBitSet() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet printableChars = new BitSet(256);
        printableChars.set(32, 126); // set all printable ASCII characters

        byte[] encodedBytes = {0x2D, (byte) 0x61, (byte) 0x6E, (byte) 0x64, (byte) 0x72, (byte) 0x6F, (byte) 0x63, (byte) 0x75, (byte) 0x5C, (byte) 0x74, (byte) 0x6F, (byte) 0x2D, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, 0x64, 0x2D, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x69, (byte) 0x6E, (byte) 0x67, (byte) 0x3D, (byte) 0x3D, (byte) 0x2D, (byte) 0x64, (byte) 0x61, (byte) 0x74, (byte) 0x65, 0x3D, (byte) 0x31};
        byte[] decodedBytes = codec.decode(encodedBytes, printableChars);

        Assert.assertArrayEquals(new byte[] {0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21}, decodedBytes);
    }

    @Test
    public void testDecodeStringWithBitSet() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        BitSet printableChars = new BitSet(256);
        printableChars.set(32, 126); // set all printable ASCII characters

        String encodedString = "=68=65=6C=6C=6F=77=20=57=6F=72=6C=64=21";
        String decodedString = codec.decode(encodedString, printableChars);

        Assert.assertEquals("Hello World!", decodedString);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecodeWithUnsupportedCharset() throws DecoderException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        String encodedString = "=68=65=6C=6C=6F=77=20=57=6F=72=6C=64=21";
        codec.decode(encodedString, "ISO-8859-1");
    }

    @Test(expected = DecoderException.class)
    public void testDecodeWithInvalidByteSequence() throws DecoderException, UnsupportedEncodingException {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encodedBytes = {0x2D, (byte) 0x61, (byte) 0x6E, (byte) 0x64, (byte) 0x72, (byte) 0x6F, (byte) 0x63, (byte) 0x75, (byte) 0x5C, (byte) 0x74, (byte) 0x6F, (byte) 0x2D, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, 0x3D, (byte) 0x3D};
        codec.decode(encodedBytes, CharEncoding.UTF_8);
    }
}