import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(), new ArrayList<>());
        char expected = 'a';
        tokeniser.emit('a');
        tokeniser.transition(TokeniserState.Data);
        Token actual = tokeniser.read();
        Assert.assertEquals("Expected character 'a', but got " + actual, TokenType.Character, actual.type);
        Assert.assertEquals("Character read was not equal to expected", expected, ((Token.Character) actual).chars[0]);
    }

    // ... other test cases for the read() method with typical use cases and edge cases

    // ... test cases for other methods like emit(), transition(), acknowledgeSelfClosingFlag(), etc.

    @Test(expected = ParseError.class)
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(), new ArrayList<>());
        tokeniser.error("Character reference error message");
        tokeniser.consumeCharacterReference('x');
    }

    @Test(expected = ParseError.class)
    public void testCharacterReferenceError_invalidNamedReference() {
        Tokeniser tokeniser = new Tokeniser(new MockCharacterReader(), new ArrayList<>());
        tokeniser.error("Invalid named reference error message");
        tokeniser.consumeCharacterReference(null, true);
    }

    // ... test cases for edge cases and exception handling in consumeCharacterReference() method

    // ... test cases for other methods with edge cases and exception handling

    // ... test case for isAppropriateEndTagToken() method

    // ... test case for errors handling

    // 
    public static void main(String[] args) {
        // Set up your test here
        // ...

        // Run your tests
        // ...

        // Check if there are any errors
        if (!tokeniser.errors.isEmpty()) {
            for (ParseError error : tokeniser.errors) {
                System.err.println(error.getMessage());
            }
            Assert.fail("Test failed with errors: " + tokeniser.errors.size());
        }

        // Test passed
        System.out.println("Test passed");

        // 
    }
}

class MockCharacterReader implements CharacterReader {
    private StringBuilder current = new StringBuilder();

    @Override
    public int read() throws IOException {
        if (current.length() > 0) {
            int result = current.charAt(0);
            current.deleteCharAt(0);
            return result;
        } else {
            return -1;
        }
    }

    // ... other methods needed for CharacterReader interface
}