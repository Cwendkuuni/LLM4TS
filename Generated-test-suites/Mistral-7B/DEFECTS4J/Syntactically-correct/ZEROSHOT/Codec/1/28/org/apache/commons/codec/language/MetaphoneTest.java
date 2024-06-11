import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        String[] testCases = {
                "apple", "appel", "appleau", "apfel", "appl", "aple",
                "banana", "bananen", "banane", "banane", "banana", "banan",
                "cat", "kat", "chat", "catt", "cats", "catten",
                "dog", "dogge", "dogg", "doggo", "dackel", "dogo",
                "elephant", "elefant", "elephant", "elephans", "elefans",
                "fish", "fisch", "fis", "fishe", "fishe", "fisk",
                "giraffe", "girafe", "giraff", "giraffe", "giraph",
                "helicopter", "helikopter", "helicopt", "helicopt",
                "ignore", "ignore", "ignore", "ignore", "ignores",
                "juxtapose", "juxtapoz", "juxapos", "juxapos", "juxapos",
                "kangaroo", "kangaroo", "kangarou", "kangarou", "kangaruu",
                "metaphone", "metaphon", "metaphones", "metaphon", "metaphon",
                "noodle", "nudel", "noodel", "nuddel", "nudel", "noodel",
                "octopus", "oktapus", "oktapoda", "oktapoda", "oktapoda",
                "quagga", "quagga", "quagga", "quagga", "quagga",
                "rabbit", "rabbit", "rabbit", "rabbit", "rabbit",
                "six", "six", "six", "six", "six",
                "tomato", "tomate", "tomate", "tomate", "tomato", "tomato"
        };

        for (String testCase : testCases) {
            String metaphoneCode1 = metaphone.metaphone(testCase);
            for (String testCase2 : testCases) {
                if (!testCase.equals(testCase2)) {
                    String metaphoneCode2 = metaphone.metaphone(testCase2);
                    assertEquals(metaphoneCode1, metaphoneCode2);
                }
            }
        }
    }

    @Test(expected = EncoderException.class)
    public void testExceptionWithNullInput() {
        Metaphone metaphone = new Metaphone();
        String nullInput = null;
        metaphone.encode(nullInput);
    }

    @Test(expected = EncoderException.class)
    public void testExceptionWithEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String emptyInput = "";
        metaphone.encode(emptyInput);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());

        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        String testString = "test";
        String expectedMetaphoneCode = "TSX";
        String actualMetaphoneCode = metaphone.encode(testString);
        assertEquals(expectedMetaphoneCode, actualMetaphoneCode);
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        String testString = "test";
        Object testObject = testString;
        String expectedMetaphoneCode = "TSX";
        String actualMetaphoneCode = (String) metaphone.encode(testObject);
        assertEquals(expectedMetaphoneCode, actualMetaphoneCode);
    }

    @Test
    public void testIsMetaphoneEqualTrue() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "test";
        String testString2 = "test";
        boolean expectedResult = true;
        boolean actualResult = metaphone.isMetaphoneEqual(testString1, testString2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsMetaphoneEqualFalse() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "test";
        String testString2 = "test1";
        boolean expectedResult = false;
        boolean actualResult = metaphone.isMetaphoneEqual(testString1, testString2);
        assertEquals(expectedResult, actualResult);
    }
}