import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "aGVsbG8gV29ybGQh";
        byte[] expectedOutput = {97, 112, 101, 116, 103, 111, 110, 100, 105, 118, 101, 114, 103, 116, 116, 108, 111, 113, 101, 116, 111, 113, 105, 110, 103};
        byte[] output = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArray_EmptyString() {
        String input = "";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_IllegalCharacter() {
        String input = "aGVsbG8gV29ybGQhX";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_StringLengthNotMultipleOfFour() {
        String input = "aGVsbG8gV29ybGQh==";
        byte[] output = Base64.altBase64ToByteArray(input);
        byte[] expectedOutput = {97, 112, 101, 116, 103, 111, 110, 100, 105, 118, 101, 114, 103, 116, 116, 108, 111, 113, 101, 116, 111, 113, 105, 110, 103, 61, 61};
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8sIG15IGZvdWwgdGhpbmdzIGZyb20gVGltZXMg";
        byte[] expectedOutput = {83, 116, 117, 114, 114, 101, 115, 116, 111, 118, 101, 114, 116, 117, 108, 105, 111, 110, 115, 115, 105, 116, 108, 101};
        byte[] output = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray_EmptyString() {
        String input = "";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {97, 112, 101, 116, 103, 111, 110, 100, 105, 118, 101, 114, 103, 116, 116, 108, 111, 113, 101, 116, 111, 113, 105, 110, 103};
        String output = Base64.byteArrayToAltBase64(input);
        assertEquals("aGVsbG8gV29ybGQh", output);
    }

    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        byte[] input = {};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToAltBase64_IllegalByteValue() {
        byte[] input = {-1};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {83, 116, 117, 114, 114, 101, 115, 116, 111, 118, 101, 114, 116, 117, 108, 105, 111, 110, 115, 115, 105, 116, 108, 101};
        String output = Base64.byteArrayToBase64(input);
        assertEquals("SGVsbG8sIG15IGZvdWwgdGhpbmdzIGZyb20gVGltZXMg", output);
    }

    @Test
    public void testByteArrayToBase64_EmptyArray() {
        byte[] input = {};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
    }

    @Test
    public void testByteArrayToBase64_IllegalByteValue() {
        byte[] input = {-1};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
    }

    @Test
    public void testAltBase64ToByteArray_InputLengthIsNotMultipleOfFour() {
        String input = "aGVsbG8gV29ybGQh==";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_InputLengthIsNotMultipleOfThree() {
        String input = "SGVsbG8sIG15IGZvdWwgdGhpbmdzIGZyb20gVGltZXMg=";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray_InvalidCharacter() {
        String input = "SGVsbG8sIG15IGZvdWwgdGhpbmdzIGZyb20gVGltZXMgX";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArray_InputLengthIsMultipleOfFourButHasExtraPadding() {
        String input = "aGVsbG8gV29ybGQh====";
        byte[] output = Base64.altBase64ToByteArray(input);
        byte[] expectedOutput = {97, 112, 101, 116, 103, 111, 110, 100, 105, 118, 101, 114, 103, 116, 116, 108, 111, 113, 101, 116, 111, 113, 105, 110, 103, 61, 61, 61, 61};
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray_InputLengthIsMultipleOfThreeButHasExtraPadding() {
        String input = "SGVsbG8sIG15IGZvdWwgdGhpbmdzIGZyb20gVGltZXMg===";
        byte[] output = Base64.base64ToByteArray(input);
        byte[] expectedOutput = {83, 116, 117, 114, 114, 101, 115, 116, 111, 118, 101, 114, 116, 117, 108, 105, 111, 110, 115, 115, 105, 116, 108, 101, 116, 116, 116, 116, 116, 116, 116};
        assertArrayEquals(expectedOutput, output);
    }
}