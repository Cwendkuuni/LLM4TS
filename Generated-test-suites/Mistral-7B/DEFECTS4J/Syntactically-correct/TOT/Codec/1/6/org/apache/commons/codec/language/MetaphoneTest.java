package org.apache.commons.codec.language;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * MetaphoneTest class.
 */
public class MetaphoneTest {

    /**
     * Instance of Metaphone class.
     */
    private Metaphone metaphone = new Metaphone();

    /**
     * Test metaphone encoding for simple words.
     */
    @Test
    public void testSimpleWords() {
        String[] words = {"apple", "banana", "cat", "dog", "elephant", "father", "girl", "hour", "idea", "juice"};
        String[] expectedCodes = {"APE", "A01", "KAT", "DOK", "I0101", "F1RTH", "G1RL", "H1UR", "I01", "I01U01C"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with double consonants.
     */
    @Test
    public void testDoubleConsonants() {
        String[] words = {"appellative", "bananabread", "cataclysm", "doggedly", "elephants", "fatherland", "hollowed", "islander", "jukebox", "kangaroo"};
        String[] expectedCodes = {"APELPELFAV", "A01001", "KATKATSM", "DOKDGDLY", "I010101", "F1RTHF1RTH", "HOLLOWLD", "I01S01LND", "I01U01KBOKS", "K01NGAROO"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with initial X.
     */
    @Test
    public void testInitialX() {
        String[] words = {"xylophone", "xenophobia", "xyloid"};
        String[] expectedCodes = {"X01L0F0N", "Z01N0F0BIA", "X01L0ID"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with silent G.
     */
    @Test
    public void testSilentG() {
        String[] words = {"beggar", "degage", "doughnut", "ghetto"};
        String[] expectedCodes = {"BIGAR", "D00G", "D00NUT", "H01T0"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with initial capital letter.
     */
    @Test
    public void testInitialCapitalLetter() {
        String[] words = {"Apple", "Banana", "Cat", "Dog", "Elephant", "Father", "Girl", "Hour", "Idea", "Juice"};
        String[] expectedCodes = {"APE", "A01", "KAT", "DOK", "I0101", "F1RTH", "G1RL", "H1UR", "I01", "I01U01C"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for words with trailing punctuation.
     */
    @Test
    public void testTrailingPunctuation() {
        String[] words = {"apple.", "banana!", "cat?", "dog!"};
        String[] expectedCodes = {"APE.", "A01", "KAT", "DOK!"};

        for (int i = 0; i < words.length; i++) {
            String code = metaphone.metaphone(words[i]);
            Assert.assertEquals(expectedCodes[i], code);
        }
    }

    /**
     * Test metaphone encoding for empty string.
     */
    @Test(expected = EncoderException.class)
    public void testEmptyString() {
        String emptyString = "";
        metaphone.metaphone(emptyString);
    }

    /**
     * Test metaphone encoding for null string.
     */
    @Test(expected = EncoderException.class)
    public void testNullString() {
        String nullString = null;
        metaphone.metaphone(nullString);
    }

    /**
     * Test metaphone encoding for non-String object.
     */
    @Test(expected = EncoderException.class)
    public void testNonStringObject() {
        Object nonStringObject = new Object();
        metaphone.metaphone(nonStringObject);
    }

    /**
     * Test metaphone encoding with different locale.
     */
    @Test
    public void testDifferentLocale() {
        Locale differentLocale = Locale.forLanguageTag("fr-FR");
        Metaphone metaphoneFR = new Metaphone();
        metaphoneFR.setLocale(differentLocale);
        String wordsFR[] = {"pomme", "banane", "chat", "chien", "éléphant", "père", "fille", "heure", "idée", "jus"};
        String codesFR[] = {"P0M0M", "A01", "SHAT", "SHIEN", "I0101", "P011R", "FILL", "H01UR", "I01", "I01U01C"};

        for (int i = 0; i < wordsFR.length; i++) {
            String code = metaphoneFR.metaphone(wordsFR[i]);
            Assert.assertEquals(codesFR[i], code);
        }
    }
}