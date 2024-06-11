import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, result);

        String str = "12345";
        int result2 = NumberInput.parseInt(str);
        assertEquals(12345, result2);

        // Edge cases
        char[] edgeCase1 = {'1', '2'};
        int result3 = NumberInput.parseInt(edgeCase1, 0, 2);
        assertEquals(12, result3);

        char[] edgeCase2 = {'9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int result4 = NumberInput.parseInt(edgeCase2, 0, 9);
        assertEquals(999999999, result4);

        // Exception handling
        char[] exceptionCase = {'a', 'b', 'c'};
        try {
            NumberInput.parseInt(exceptionCase, 0, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(1234567890L, result);

        String str = "1234567890";
        long result2 = NumberInput.parseLong(str);
        assertEquals(1234567890L, result2);

        // Edge cases
        char[] edgeCase1 = {'1', '2', '3'};
        long result3 = NumberInput.parseLong(edgeCase1, 0, 3);
        assertEquals(123L, result3);

        char[] edgeCase2 = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        long result4 = NumberInput.parseLong(edgeCase2, 0, 11);
        assertEquals(999999999999L, result4);

        // Exception handling
        String exceptionCase = "abcdefg";
        try {
            NumberInput.parseLong(exceptionCase.toCharArray(), 0, exceptionCase.length());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // Add more tests for other methods as needed
}