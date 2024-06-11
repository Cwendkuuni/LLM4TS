import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello World";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 11; // Calculate the expected length based on the given sequence
        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthEmptySequence() {
        CharSequence sequence = "";
        int length = Utf8.encodedLength(sequence);
        fail(); // This test should throw an IllegalArgumentException
    }

    @Test
    public void testEncodedLengthSingleCharacter() {
        CharSequence sequence = "A";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 1;
        assertEquals(expectedLength, length);
    }

    @Test
    public void testEncodedLengthCharacterAbove7f() {
        CharSequence sequence = "☺";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 2; // 1 byte for the '?' character and 1 byte for the trailing byte with value 0x80
        assertEquals(expectedLength, length);
    }

    @Test
    public void testEncodedLengthSurrogateCharacters() {
        CharSequence sequence = "\uD840\uDC00";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 3; // 2 bytes for the surrogate characters
        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthInvalidSequence() {
        CharSequence sequence = "你好"; // Chinese characters
        int length = Utf8.encodedLength(sequence);
        fail(); // This test should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthNullSequence() {
        CharSequence sequence = null;
        int length = Utf8.encodedLength(sequence);
        fail(); // This test should throw an IllegalArgumentException
    }
}