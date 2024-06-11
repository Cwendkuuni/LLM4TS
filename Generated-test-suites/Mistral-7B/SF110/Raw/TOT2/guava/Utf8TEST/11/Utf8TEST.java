import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLength(sequence);
        assertEquals(28, length);
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(28, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x67, (byte) 0x68, (byte) 0x69, (byte) 0x6a, (byte) 0x6b, (byte) 0x6c, (byte) 0x6d, (byte) 0x6e, (byte) 0x6f};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormedWithInvalidChar() {
        byte[] bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64, (byte) 0xff, (byte) 0x66, (byte) 0x67, (byte) 0x68, (byte) 0x69, (byte) 0x6a, (byte) 0x6b, (byte) 0x6c, (byte) 0x6d, (byte) 0x6e, (byte) 0x6f};
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse(result);
    }

    @Test
    public void testIsWellFormedWithEmptyString() {
        boolean result = Utf8.isWellFormed(new byte[0]);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormedWithNullBytes() {
        Utf8.isWellFormed(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithNullSequence() {
        Utf8.encodedLength(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithLongSequence() {
        CharSequence sequence = "a".repeat(100_000);
        try {
            Utf8.encodedLength(sequence);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithUnpairedSurrogate() {
        CharSequence sequence = "𝄞";
        try {
            Utf8.encodedLength(sequence);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}