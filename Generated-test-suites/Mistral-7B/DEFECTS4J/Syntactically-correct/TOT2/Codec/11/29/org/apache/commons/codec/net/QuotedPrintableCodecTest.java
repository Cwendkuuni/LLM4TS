import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeBinary() throws IOException, DecoderException {
        byte[] input = {0x61, 0x62, 0x63};
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        Assert.assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws UnsupportedEncodingException, DecoderException {
        String input = "abc";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeWhitespaceAtEndOfLine() throws IOException {
        byte[] input = {0x0a, 0x20, 0x61, 0x62, 0x63, 0x0d, 0x0a};
        byte[] encoded = codec.encode(input);
        String expected = "\r\n=\r\n d=\r\n";
        String actual = new String(encoded, "US-ASCII");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHex() throws IOException, DecoderException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR, 'A', ESCAPE_CHAR, 'B', ESCAPE_CHAR, 'C', ESCAPE_CHAR, 'Z'};
        codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTooFewBytes() throws IOException, DecoderException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR};
        codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingEquals() throws IOException, DecoderException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR, 'A'};
        codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingCR() throws IOException, DecoderException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR, 'A', 0x0d};
        codec.decode(input);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeTrailingLF() throws IOException, DecoderException {
        byte[] input = {ESCAPE_CHAR, ESCAPE_CHAR, 'A', 0x0a};
        codec.decode(input);
    }

    @Test
    public void testEncodeDecodeStringWithControlCharacters() throws UnsupportedEncodingException, DecoderException {
        String input = "\u0001\u0002\u0003";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithLineBreaks() throws UnsupportedEncodingException, DecoderException {
        String input = "\r\nHello\r\nWorld\r\n";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithWhitespaces() throws UnsupportedEncodingException, DecoderException {
        String input = " Hello World ";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeLongString() throws UnsupportedEncodingException, DecoderException {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor semper turpis eget fermentum. Sed eget magna at tellus fringilla congue. Donec auctor, velit nec bibendum rhoncus, nulla lacus tristique magna, vel scelerisque urna odio vitae mauris.";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testDecodeStringWithInvalidHex() throws IOException, DecoderException {
        // This test should throw an exception since the input string contains an invalid hex value
        String input = "=AB=CD=EF=G\u0001";
        Assert.assertThrows(DecoderException.class, () -> codec.decode(StringUtils.getBytesUsAscii(input)));
    }

    @Test
    public void testDecodeEmptyString() throws IOException, DecoderException {
        String input = "";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testDecodeStringWithEmptyLineBreaks() throws IOException, DecoderException {
        String input = "\r\n";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testDecodeStringWithTrailingEquals() throws IOException, DecoderException {
        String input = "foo=bar=";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testDecodeStringWithTrailingCR() throws IOException, DecoderException {
        String input = "foo=\r";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testDecodeStringWithTrailingLF() throws IOException, DecoderException {
        String input = "foo=\n";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testDecodeStringWithTrailingCRAndLF() throws IOException, DecoderException {
        String input = "foo=\r\n";
        byte[] decoded = codec.decode(StringUtils.getBytesUsAscii(codec.encode(input)));
        Assert.assertEquals(input, new String(decoded, "US-ASCII"));
    }

    @Test
    public void testEncodeDecodeStringWithControlCharactersAndWhitespaces() throws UnsupportedEncodingException, DecoderException {
        String input = "\u0001 Hello World \u0020";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithLineBreaksAndWhitespaces() throws UnsupportedEncodingException, DecoderException {
        String input = "\r\nHello\r\nWorld\r\n";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithMixedAsciiAndPrintableCharacters() throws UnsupportedEncodingException, DecoderException {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor semper turpis eget fermentum. Sed eget magna at tellus fringilla congue. Donec auctor, velit nec bibendum rhoncus, nulla lacus tristique magna, vel scelerisque urna odio vitae mauris.\r\nTest=\r\nString with control characters: !@#$%^&*()_+|~`{}[]:;\"<>,.?/\\\r\n";
        String encoded = codec.encode(input);
        String decoded = codec.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBitSet() throws IOException, DecoderException {
        BitSet input = new BitSet();
        input.set(0, 256);
        byte[] encodedBytes = codec.encode(input.toByteArray());
        BitSet decoded = new BitSet();
        decoded.decode(new ByteArrayInputStream(codec.decode(encodedBytes)));
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBitSetWithPrintableCharacters() throws IOException, DecoderException {
        BitSet input = new BitSet();
        input.set(0, 32);
        input.set(33, 126);
        byte[] encodedBytes = codec.encode(input.toByteArray());
        BitSet decoded = new BitSet();
        decoded.decode(new ByteArrayInputStream(codec.decode(encodedBytes)));
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeBitSetWithUnprintableCharacters() throws IOException, DecoderException {
        BitSet input = new BitSet();
        input.set(0, 0);
        input.set(128, 1);
        byte[] encodedBytes = codec.encode(input.toByteArray());
        BitSet decoded = new BitSet();
        decoded.decode(new ByteArrayInputStream(codec.decode(encodedBytes)));
        Assert.assertEquals(input, decoded);
    }
}