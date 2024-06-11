import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CaverphoneTest {

    private static final String[] TEST_CASES = {
            "apple", "banana", "cherry", "orange", "grape", "kiwi", "mango", "peach", "pineapple",
            "caversham", "algorithm", "university", "otago", "newzealand",
            "Caverphone2.0", "algorithm", "Caverphone", "Implementation",
            "null", "", "Caversham Project", "University of Otago",
            "cough", "rough", "tough", "enough", "trough", "gn", "mb",
            "cq", "ci", "ce", "cy", "tch", "c", "q", "x", "v", "dg", "tio", "tia", "d", "ph", "b", "sh", "z",
            "j", "y", "w3", "wh3", "w", "h", "r3", "r", "l3", "l", "3gh3", "gh", "g", "s+", "t+", "p+", "k+", "f+", "m+", "n+"};

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(null);
    }

    @Test
    public void testEmptyInput() {
        StringEncoder encoder = new Caverphone();
        String result = encoder.encode("");
        String expected = "1111111111";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSimpleWords() {
        StringEncoder encoder = new Caverphone();
        String[] words = {"apple", "banana", "cherry", "orange", "grape", "kiwi", "mango", "peach", "pineapple"};
        String[] expectedEncodings = {"appl3", "banan3", "cherry", "orang3", "grape", "kiwi", "mang0", "peach", "pin3app"};
        for (int i = 0; i < words.length; i++) {
            String result = encoder.encode(words[i]);
            Assert.assertEquals(expectedEncodings[i], result);
        }
    }

    @Test
    public void testComplexWords() {
        StringEncoder encoder = new Caverphone();
        String[] words = {"caversham", "algorithm", "university", "otago", "newzealand"};
        String[] expectedEncodings = {"c4v3rsh4m", "algor3ithm", "universit2", "otag2", "n2wzl4nd"};
        for (int i = 0; i < words.length; i++) {
            String result = encoder.encode(words[i]);
            Assert.assertEquals(expectedEncodings[i], result);
        }
    }

    @Test
    public void testSpecialCases() {
        StringEncoder encoder = new Caverphone();
        String[] specialCases = {"Caverphone2.0", "algorithm", "Caverphone", "Implementation"};
        String[] expectedEncodings = {"C4v3rph0n320", "algor3ithm", "C4v3rph0n3", "Impl3ment4t10n"};
        for (int i = 0; i < specialCases.length; i++) {
            String result = encoder.encode(specialCases[i]);
            Assert.assertEquals(expectedEncodings[i], result);
        }
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        StringEncoder encoder = new Caverphone();
        String input = "Caversham Project@University of Otago";
        encoder.encode(input);
    }

    @Test
    public void testEdgeCases() {
        StringEncoder encoder = new Caverphone();
        String[] edgeCases = {"null", "", "Caversham Project", "University of Otago", "Caversham", "Project",
                "University", "of", "Otago"};
        String[] expectedEncodings = {"1111111111", "", "c4v3rsh4m", "universit2", "c4v3rsh4m", "pr3j3ct",
                "universit2", "u1viv3rs3t2", "otag2", "otag2"};
        for (int i = 0; i < edgeCases.length; i++) {
            String result = encoder.encode(edgeCases[i]);
            Assert.assertEquals(expectedEncodings[i], result);
        }
    }

    @Test
    public void testErrorScenarios() {
        StringEncoder encoder = new Caverphone();
        String[] errorScenarios = {"cough", "rough", "tough", "enough", "trough", "gn", "mb", "cq", "ci",
                "ce", "cy", "tch", "c", "q", "x", "v", "dg", "tio", "tia", "d", "ph", "b", "sh", "z",
                "j", "y", "w3", "wh3", "w", "h", "r3", "r", "l3", "l", "3gh3", "gh", "g", "s+", "t+", "p+",
                "k+", "f+", "m+", "n+"};
        String[] expectedEncodings = {"cou2f", "r3ough", "t3ough", "enou2f", "trou2f", "2n", "m2", "2q", "si",
                "se", "sy", "2ch", "k", "k", "k", "f", "2g", "sio", "sia", "t", "fh", "p", "s2", "s", "k",
                "s", "k", "F", "M", "N", "W3", "Wh3", "2", "A", "R3", "3", "L3", "3"};
        for (int i = 0; i < errorScenarios.length; i++) {
            String result = encoder.encode(errorScenarios[i]);
            Assert.assertEquals(expectedEncodings[i], result);
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        StringEncoder encoder = new Caverphone();
        String[] strings = {"apple", "banana", "cherry", "orange", "grape", "kiwi", "mango", "peach",
                "pineapple", "caversham", "algorithm", "university", "otago", "newzealand",
                "Caverphone2.0", "algorithm", "Caverphone", "Implementation", "apple", "banana", "cherry",
                "orange", "grape", "kiwi", "mango", "peach", "pineapple"};
        for (int i = 0; i < strings.length; i += 2) {
            boolean result = encoder.isCaverphoneEqual(strings[i], strings[i + 1]);
            boolean expected = Arrays.equals(encoder.encode(strings[i]), encoder.encode(strings[i + 1]));
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testIsCaverphoneEqualWithNull() {
        StringEncoder encoder = new Caverphone();
        String[] strings = {"apple", null, "banana", "cherry", "orange", "grape", "kiwi", "mango",
                "peach", "pineapple", "caversham", "algorithm", "university", "otago", "newzealand",
                "Caverphone2.0", "algorithm", "Caverphone", "Implementation", "apple", "banana",
                "cherry", "orange", "grape", "kiwi", "mango", "peach", "pineapple"};
        for (int i = 0; i < strings.length; i += 2) {
            boolean result = encoder.isCaverphoneEqual(strings[i], strings[i + 1]);
            if (strings[i + 1] == null) {
                Assert.assertFalse(result);
            } else {
                boolean expected = Arrays.equals(encoder.encode(strings[i]), encoder.encode(strings[i + 1]));
                Assert.assertEquals(expected, result);
            }
        }
    }

    @Test(expected = DecoderException.class)
    public void testIsCaverphoneEqualWithDifferentLength() {
        StringEncoder encoder = new Caverphone();
        String[] strings = {"apple", "banana", "cherry", "orange", "grape", "kiwi", "mango", "peach",
                "pineapple", "caversham", "algorithm", "university", "otago", "newzealand",
                "Caverphone2.0", "algorithm", "Caverphone", "Implementation", "apple", "banana",
                "cherry", "orange", "grape", "kiwi", "mango", "peach", "pineapple"};
        String string1 = strings[0];
        String string2 = strings[strings.length - 1] + "a";
        boolean result = encoder.isCaverphoneEqual(string1, string2);
        Assert.assertFalse(result);
    }
}