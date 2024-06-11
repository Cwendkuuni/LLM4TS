import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Metaphone;

public class MetaphoneTest {

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("A");
        assertEquals("A", result);
    }

    @Test
    public void testBasicString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("Banana");
        assertEquals("B321", result);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    @Test(expected = EncoderException.class)
    public void testNonASCIIInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode("MöetChampagne");
    }

    @Test
    public void testMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("AppleSauce");
        assertEquals("A1P1L1E1S1", result);
    }

    @Test
    public void testEdgeCases() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"Apple", "Application", "Consonant", "Identical", "Indistinguishable", "Metaphone"};
        for (String testCase : testCases) {
            String result = metaphone.metaphone(testCase);
            System.out.println(testCase + " -> " + result);
        }
    }
}