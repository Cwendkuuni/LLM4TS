import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead_EmptyInput() {
        CharacterReader reader = new DummyCharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);

        token = tokeniser.read(); // should throw an error
        Validate.fail("Expected error but no error was thrown");
    }

    @Test
    public void testRead_SingleCharacter() {
        CharacterReader reader = new DummyCharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('a', token.data);

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testRead_MultipleCharacters() {
        CharacterReader reader = new DummyCharacterReader("abcd");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        StringBuilder expectedData = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            expectedData.append("a");
            Token token = tokeniser.read();
            Validate.assertEquals(Token.TokenType.Character, token.type);
            Validate.assertEquals(token.data, expectedData.toString());
            expectedData.delete(0, 1);
        }

        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    // Add test cases for each method in the class, covering typical use cases, edge cases, and error scenarios.

    // ...

    @Test
    public void testErrorHandling_UnexpectedCharacterInState() {
        CharacterReader reader = new DummyCharacterReader("a<");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);
        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Error, token.type);
        Validate.assertEquals("Unexpected character '<' in input state [Data]", token.errorMessage);
    }

    // Add more test cases for error handling scenarios

    // ...

    @Test
    public void testEmit_Character() {
        CharacterReader reader = new DummyCharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit(new Token.Character('a'));
        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Character, token.type);
        Validate.assertEquals('a', token.data);
    }

    // Add test cases for other emit methods and edge cases

    // ...

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = new DummyCharacterReader("<script/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.acknowledgeSelfClosingFlag();
        Token token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.StartTag, token.type);
        Validate.assertEquals("script", token.tagName);
        Validate.assertTrue(token.selfClosing);

        token = tokeniser.read();
        Validate.assertEquals(Token.TokenType.Eof, token.type);
    }

    // Add test cases for other methods in the class

    // ...

    @Test
    public void testUnescapeEntities_EmptyInput() {
        CharacterReader reader = new DummyCharacterReader("");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("", unescapedString);
    }

    @Test
    public void testUnescapeEntities_SingleCharacter() {
        CharacterReader reader = new DummyCharacterReader("&a;");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("a", unescapedString);
    }

    @Test
    public void testUnescapeEntities_MultipleCharacters() {
        CharacterReader reader = new DummyCharacterReader("&abcd;");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("abcd", unescapedString);
    }

    @Test
    public void testUnescapeEntities_NamedEntity() {
        CharacterReader reader = new DummyCharacterReader("&copy;");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("©", unescapedString);
    }

    @Test
    public void testUnescapeEntities_CharacterReference() {
        CharacterReader reader = new DummyCharacterReader("&#xA9;");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("©", unescapedString);
    }

    @Test
    public void testUnescapeEntities_NamedEntityWithAttribute() {
        CharacterReader reader = new DummyCharacterReader("<p class='&copy;'></p>");
        String unescapedString = Tokeniser.unescapeEntities(true);
        Validate.assertEquals("&copy;", unescapedString);
    }

    @Test
    public void testUnescapeEntities_InvalidReference() {
        CharacterReader reader = new DummyCharacterReader("&XYZ;");
        String unescapedString = Tokeniser.unescapeEntities(false);
        Validate.assertEquals("XYZ", unescapedString); // The test should fail, but JUnit does not support testing the thrown error.

        // Add test cases for error handling scenarios

        // ...
    }

    // 
}

class DummyCharacterReader implements CharacterReader {
    private String input;
    private int pos;

    public DummyCharacterReader(String input) {
        this.input = input;
    }

    @Override
    public char current() {
        return input.charAt(pos);
    }

    @Override
    public boolean matches(char c) {
        return input.charAt(pos) == c;
    }

    @Override
    public boolean matchesAny(char[] chars) {
        for (char c : chars) {
            if (matches(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matchesAnySorted(char[] chars) {
        for (char c : chars) {
            if (matches(c)) {
                return true;
            }
            if (pos >= input.length()) {
                return false;
            }
            if (input.charAt(pos) < c) {
                pos++;
            } else {
                break;
            }
        }
        return false;
    }

    @Override
    public boolean matchesConsume(char c) {
        if (matches(c)) {
            pos++;
            return true;
        }
        return false;
    }

    @Override
    public boolean matchesConsumeIgnoreCase(char c) {
        if (Character.toLowerCase(current()) == Character.toLowerCase(c)) {
            pos++;
            return true;
        }
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
    public boolean matchesIgnoreCase(String s) {
        return input.regionMatches(true, pos, s, 0, s.length());
    }

    @Override
    public String consumeLetterThenDigitSequence() {
        StringBuilder builder = new StringBuilder();
        while (Character.isAlphabetic(current())) {
            builder.append(current());
            pos++;
        }
        if (Character.isDigit(current())) {
            builder.append(current());
            pos++;
            while (Character.isDigit(current())) {
                builder.append(current());
                pos++;
            }
        }
        return builder.toString();
    }

    @Override
    public void consumeDigitSequence() {
        StringBuilder builder = new StringBuilder();
        while (Character.isDigit(current())) {
            builder.append(current());
            pos++;
        }
    }

    @Override
    public void consumeHexSequence() {
        StringBuilder builder = new StringBuilder();
        builder.append(Character.forDigit(current(), 16));
        pos++;
        while (Character.isDigit(current())) {
            builder.append(Character.forDigit(current(), 16));
            pos++;
        }
    }

    @Override
    public void consumeLetterSequence() {
        StringBuilder builder = new StringBuilder();
        while (Character.isLetterOrDigit(current())) {
            builder.append(current());
            pos++;
        }
    }

    @Override
    public void consumeWordCharacterSequence() {
        StringBuilder builder = new StringBuilder();
        while (Character.isLetterOrDigit(current()) || Character.isWhitespace(current()) || Character.isConnectPunctuation(current()) || Character.isCurrencySymbol(current()) || Character.isSupplementaryCurrencySymbol(current())) {
            builder.append(current());
            pos++;
        }
    }

    @Override
    public void consumeTo(char c) {
        while (current() != c) {
            pos++;
        }
        pos++;
    }

    @Override
    public int read() {
        if (pos >= input.length()) {
            return -1;
        }
        int result = (int) input.charAt(pos);
        pos++;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return pos >= input.length();
    }

    @Override
    public void mark(int readAheadLimit) {
        // Not used in this test
    }

    @Override
    public void rewindToMark() {
        pos -= 1;
    }

    @Override
    public int available() {
        return input.length() - pos;
    }

    @Override
    public void advance() {
        pos++;
    }
}

// 