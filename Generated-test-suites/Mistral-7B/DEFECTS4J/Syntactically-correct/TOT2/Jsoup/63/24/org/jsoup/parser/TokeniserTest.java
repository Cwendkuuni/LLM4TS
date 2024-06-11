import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokeniserTest {

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new StubCharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndOfInput, token.type);
    }

    @Test
    public void testReadSingleCharacter() {
        CharacterReader reader = new StubCharacterReader("A");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("A", token.data);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new StubCharacterReader("<img/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();
        assertNotNull(startTag);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("img", startTag.tagName);
        assertNotNull(endTag);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("img", endTag.tagName);
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidInput() {
        CharacterReader reader = new StubCharacterReader("invalid input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read();
    }

    // Add test cases for other methods and edge cases here
}

class StubCharacterReader implements CharacterReader {
    private int index = 0;
    private String input;

    StubCharacterReader(String input) {
        this.input = input;
    }

    @Override
    public char current() {
        return input.charAt(index);
    }

    @Override
    public void advance() {
        index++;
    }

    @Override
    public boolean isEmpty() {
        return index >= input.length();
    }

    @Override
    public void mark(int readlimit) {
        // Not used in this test
    }

    @Override
    public void rewindToMark() {
        // Not used in this test
    }

    @Override
    public int consumeDigitSequence() {
        int num = 0;
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            num = num * 10 + Character.isDigit(input.charAt(index)) ? Character.digit(input.charAt(index)) : num;
            index++;
        }
        return num;
    }

    @Override
    public int consumeLetterThenDigitSequence() {
        int letter = index < input.length() ? Character.toLowerCase(input.charAt(index++)) : 0;
        return consumeDigitSequence();
    }

    @Override
    public boolean matchesAny(char[] chars) {
        return Arrays.binarySearch(chars, current()) >= 0;
    }

    @Override
    public boolean matchesAnySorted(char[] chars) {
        return Arrays.binarySearch(chars, current()) >= 0;
    }

    @Override
    public boolean matches(String regex) {
        // Not used in this test
        return false;
    }

    @Override
    public boolean matchesConsume(String regex) {
        // Not used in this test
        return false;
    }

    @Override
    public boolean matchesIgnoreCase(String regex) {
        // Not used in this test
        return false;
    }

    @Override
    public boolean matchesLetter() {
        return Character.isLetter(current());
    }

    @Override
    public boolean matchesDigit() {
        return Character.isDigit(current());
    }

    @Override
    public boolean matches(char c) {
        return current() == c;
    }

    @Override
    public boolean matchesConsumeIgnoreCase(String regex) {
        // Not used in this test
        return false;
    }

    @Override
    public boolean matchesConsume(char c) {
        if (current() == c) {
            advance();
            return true;
        }
        return false;
    }

    @Override
    public boolean matchesConsume(String regex) {
        // Not used in this test
        return false;
    }

    @Override
    public int consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
        // Not used in this test
        return null;
    }
}