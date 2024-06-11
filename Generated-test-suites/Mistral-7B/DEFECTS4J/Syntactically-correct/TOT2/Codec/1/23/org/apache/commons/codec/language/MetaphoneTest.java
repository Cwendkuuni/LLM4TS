
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.*;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private static Metaphone encoder;

    @BeforeClass
    public static void setup() {
        encoder = new Metaphone();
    }

    @Test
    public void testMetaphoneSimple() {
        assertEquals("KNIGHT", encoder.metaphone("knight"));
        assertEquals("APPLE", encoder.metaphone("apple"));
        assertEquals("BAT", encoder.metaphone("bat"));
    }

    @Test
    public void testMetaphoneEmptyString() {
        assertEquals("", encoder.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        assertEquals("A", encoder.metaphone("a"));
        assertEquals("B", encoder.metaphone("b"));
    }

    @Test
    public void testMetaphoneSpecialCases() {
        assertEquals("KNIGHT", encoder.metaphone("knighthood"));
        assertEquals("A0", encoder.metaphone("theathre"));
        assertEquals("A0", encoder.metaphone("thor"));
        assertEquals("J", encoder.metaphone("doughnut"));
        assertEquals("X", encoder.metaphone("ciao"));
        assertEquals("A0E", encoder.metaphone("theappleathre"));
        assertEquals("A0E", encoder.metaphone("thorapple"));
        assertEquals("JE", encoder.metaphone("doughnutapple"));
        assertEquals("XE", encoder.metaphone("ciaoapple"));
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullInput() {
        encoder.metaphone(null);
    }

    @Test
    public void testMetaphoneEdgeCasesWithVowels() {
        assertEquals("A0E", encoder.metaphone("theappleathre"));
        assertEquals("A0E", encoder.metaphone("thorapple"));
        assertEquals("JE", encoder.metaphone("doughnutapple"));
        assertEquals("XE", encoder.metaphone("ciaoapple"));
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        assertEquals("A0", encoder.metaphone("antidisestablishmentarianism"));
    }

    @After
    public void tearDown() {
        encoder = null;
    }
}
