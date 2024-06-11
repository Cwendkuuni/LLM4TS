import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Metaphone;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("A");
        assertEquals("A", result);
    }

    @Test
    public void testMetaphoneSingleCharacterUppercase() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("A");
        assertEquals("A", result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone(null);
    }

    @Test
    public void testMetaphoneTwoCharacters() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("KN");
        assertEquals("K0", result);
    }

    @Test
    public void testMetaphoneTwoVowels() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("AE");
        assertEquals("A0", result);
    }

    @Test
    public void testMetaphoneInitialK() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("KIT");
        assertEquals("K0T", result);
    }

    @Test
    public void testMetaphoneInitialW() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("WHISKEY");
        assertEquals("W2S2K2E", result);
    }

    @Test
    public void testMetaphoneInitialX() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("XYZ");
        assertEquals("SXSX", result);
    }

    @Test
    public void testMetaphoneInitialCapitalLetter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CAT");
        assertEquals("C2T", result);
    }

    @Test
    public void testMetaphoneMultipleVowels() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("AIEIOU");
        assertEquals("A0E0U0", result);
    }

    @Test
    public void testMetaphoneB() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("BABY");
        assertEquals("B2B", result);
    }

    @Test
    public void testMetaphoneC() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CITY");
        assertEquals("S2T2", result);
    }

    @Test
    public void testMetaphoneCInitialCapitalLetter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CITy");
        assertEquals("K2T2", result);
    }

    @Test
    public void testMetaphoneCIA() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CIA");
        assertEquals("X0", result);
    }

    @Test
    public void testMetaphoneCH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CHAIR");
        assertEquals("K2R", result);
    }

    @Test
    public void testMetaphoneCHConsontant() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("CHAT");
        assertEquals("K2T", result);
    }

    @Test
    public void testMetaphoneD() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("DADDY");
        assertEquals("D2D", result);
    }

    @Test
    public void testMetaphoneDGE() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("DGEEGE");
        assertEquals("J2J", result);
    }

    @Test
    public void testMetaphoneDGI() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("DGIFEE");
        assertEquals("J2J", result);
    }

    @Test
    public void testMetaphoneDGY() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("DGYEE");
        assertEquals("J2J", result);
    }

    @Test
    public void testMetaphoneG() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("GRAPE");
        assertEquals("K2P", result);
    }

    @Test
    public void testMetaphoneGH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("GHOTI");
        assertEquals("F2T2", result);
    }

    @Test
    public void testMetaphoneGHsilent() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("GHOTIH");
        assertEquals("F2", result);
    }

    @Test
    public void testMetaphoneH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("HAPPY");
        assertEquals("H2P", result);
    }

    @Test
    public void testMetaphoneF() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("FLOWER");
        assertEquals("F2W2R", result);
    }

    @Test
    public void testMetaphoneJ() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("JOY");
        assertEquals("J2", result);
    }

    @Test
    public void testMetaphoneK() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("KANGAROO");
        assertEquals("K2N2G2R2W", result);
    }

    @Test
    public void testMetaphoneL() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("LOVE");
        assertEquals("L2F", result);
    }

    @Test
    public void testMetaphoneM() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("MOUSE");
        assertEquals("M2U2S", result);
    }

    @Test
    public void testMetaphoneN() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("NOSE");
        assertEquals("N2S", result);
    }

    @Test
    public void testMetaphoneP() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("PAPER");
        assertEquals("P2R", result);
    }

    @Test
    public void testMetaphoneQ() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("QUICK");
        assertEquals("K2", result);
    }

    @Test
    public void testMetaphoneR() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("RAT");
        assertEquals("R2T", result);
    }

    @Test
    public void testMetaphoneS() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("SILENT");
        assertEquals("S2L2T", result);
    }

    @Test
    public void testMetaphoneSCH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("SCHOOL");
        assertEquals("SK2L", result);
    }

    @Test
    public void testMetaphoneSH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("SHIP");
        assertEquals("P2", result);
    }

    @Test
    public void testMetaphoneSiO() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("SIOUX");
        assertEquals("S2U2X", result);
    }

    @Test
    public void testMetaphoneSiA() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("SIAOU");
        assertEquals("S2O2U", result);
    }

    @Test
    public void testMetaphoneTIA() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("TIA");
        assertEquals("X0", result);
    }

    @Test
    public void testMetaphoneTIO() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("TIO");
        assertEquals("X0", result);
    }

    @Test
    public void testMetaphoneTCH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("TCHOCOLATE");
        assertEquals("T2K2L2T", result);
    }

    @Test
    public void testMetaphoneTH() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("THETA");
        assertEquals("T0", result);
    }

    @Test
    public void testMetaphoneV() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("VOTE");
        assertEquals("F2T2", result);
    }

    @Test
    public void testMetaphoneW() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("WICKET");
        assertEquals("W2K2T", result);
    }

    @Test
    public void testMetaphoneY() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("YELLOW");
        assertEquals("Y2L2W", result);
    }

    @Test
    public void testMetaphoneZero() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("ZERO");
        assertEquals("Z2R2O", result);
    }

    @Test
    public void testMetaphoneTwoStringsEqual() {
        Metaphone metaphone = new Metaphone();
        String str1 = "APPLE";
        String str2 = "APPLE";
        boolean result = metaphone.isMetaphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testMetaphoneTwoStringsNotEqual() {
        Metaphone metaphone = new Metaphone();
        String str1 = "APPLE";
        String str2 = "BANANA";
        boolean result = metaphone.isMetaphoneEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testMetaphoneSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}