import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input, reader.toString());
    }

    // Test methods for pos(), isEmpty(), current(), consume(), unconsume(), advance(), mark(), rewindToMark(), consumeAsString(), nextIndexOf(char), nextIndexOf(CharSequence), consumeTo(char), consumeTo(String), consumeToAny(char[]), consumeToEnd(), consumeLetterSequence(), consumeLetterThenDigitSequence(), consumeHexSequence(), consumeDigitSequence(), matches(char), matches(String), matchesIgnoreCase(String), matchesAny(char[]), matchesLetter(), matchesDigit(), matchConsume(String), matchConsumeIgnoreCase(String), containsIgnoreCase(String), and toString()

    // ...
}