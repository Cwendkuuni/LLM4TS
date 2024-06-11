import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        codec = new QuotedPrintableCodec(StandardCharsets.UTF_8.name());
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void cleanup() throws IOException {
        outputStream.close();
    }

    @Test
    public void testEncodeDecodeByteArray() throws DecoderException, EncoderException {
        byte[] input = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] output = codec.encode(input);
        byte[] decoded = codec.decode(output);

        assertEquals(input.length, decoded.length);
        for (int i = 0; i < input.length; i++) {
            assertEquals(input[i], decoded[i]);
        }
    }

    @Test
    public void testEncodeDecodeString() throws DecoderException, EncoderException, UnsupportedEncodingException {
        String input = "Hello World!";
        String output = codec.encode(input, StandardCharsets.UTF_8.name());
        String decoded = codec.decode(output, StandardCharsets.UTF_8.name());

        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeSingleByte() throws IOException {
        byte input = 0x61; // 'a'
        byte output = codec.encode(input)[0];
        assertEquals(ESCAPE_CHAR, output);
        assertEquals('61', output + (byte) 0x30);
        assertEquals('61', output + (byte) 0x31);
    }

    @Test
    public void testEncodeMultipleBytes() throws IOException {
        byte[] input = {0x61, 0x62, 0x63}; // 'abc'
        byte[] output = codec.encode(input);

        int index = 0;
        for (byte b : input) {
            assertEquals(ESCAPE_CHAR, output[index++]);
            assertEquals('61' + (index > 0 ? (index % 3 == 0 ? '+' : '') : ''), output[index++]);
            assertEquals('61' + (index > 0 ? (index % 3 == 1 ? '+' : '') : ''), output[index++]);
            assertEquals('61' + (index > 0 ? (index % 3 == 2 ? '+' : '') : ''), output[index++]);
        }

        assertEquals(input.length * 3, output.length);
    }

    @Test
    public void testDecodeByteArray() throws DecoderException, IOException {
        byte[] input = {ESCAPE_CHAR, '6', '1', '3', ESCAPE_CHAR, '6', '2', '3'};
        byte[] decoded = codec.decode(input);

        byte[] expected = "abc".getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < decoded.length; i++) {
            assertEquals(expected[i], decoded[i]);
        }
    }

    @Test
    public void testDecodeString() throws DecoderException, IOException, UnsupportedEncodingException {
        String input = "=%61%62%63";
        String decoded = codec.decode(input, StandardCharsets.UTF_8.name());

        assertEquals("abc", decoded);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalid() throws DecoderException, IOException {
        codec.decode(new byte[]{ESCAPE_CHAR, '6', '4'}, StandardCharsets.UTF_8.name());
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalid() throws EncoderException, IOException {
        codec.encode(new byte[]{ESCAPE_CHAR}, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testEncodeLargeString() throws EncoderException, IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024 * 1024; i++) {
            sb.append(String.format("%c", (char) (i % 128 + '!')));
        }
        byte[] input = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] output = codec.encode(input);

        BitSet printableChars = codec.PRINTABLE_CHARS;
        for (byte b : input) {
            if (!printableChars.get(b & 0xFF)) {
                for (int i = 0; i < 3; i++) {
                    assertEquals(ESCAPE_CHAR, output[output.length - 3 - i]);
                    assertEquals((b & 0xF0) >>> 4, output[output.length - 2 - i]);
                    assertEquals(b & 0x0F, output[output.length - 1 - i]);
                }
            }
        }
    }

    @Test
    public void testEncodeDecodeBinaryDecoder() throws DecoderException, EncoderException, IOException {
        BinaryDecoder decoder = new StringDecoder();
        decoder.setDecoder(codec);
        decoder.setInput("=%61%62%63");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        BinaryEncoder encoder = new StringEncoder();
        encoder.setEncoder(codec);
        encoder.setOutput(buffer);

        decoder.writeTo(encoder);

        assertEquals("abc", buffer.toString(StandardCharsets.UTF_8.name()));
    }
}