import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test true cases
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("get"));
        assertTrue(TokenStream.isKeyword("set"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("yield"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("as"));
        assertTrue(TokenStream.isKeyword("from"));

        // Test false cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword(" "));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("!@#$%^&*()_+{}|:<>?"));
        assertFalse(TokenStream.isKeyword("abcdEfGhIjKlMnOpQrStUvWxYz"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test true cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("_bar"));
        assertTrue(TokenStream.isJSIdentifier("fooBar"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar"));
        assertTrue(TokenStream.isJSIdentifier("foo123"));
        assertTrue(TokenStream.isJSIdentifier("foo_123"));
        assertTrue(TokenStream.isJSIdentifier("foo-bar"));
        assertTrue(TokenStream.isJSIdentifier("foo_123-bar"));
        assertTrue(TokenStream.isJSIdentifier("foo$bar"));
        assertTrue(TokenStream.isJSIdentifier("foo_$bar"));
        assertTrue(TokenStream.isJSIdentifier("foo__bar"));
        assertTrue(TokenStream.isJSIdentifier("foo___bar"));

        // Test false cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("abc"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("!@#$%^&*()_+{}|:<>?"));
        assertFalse(TokenStream.isJSIdentifier("abcdEfGhIjKlMnOpQrStUvWxYz"));
        assertFalse(TokenStream.isJSIdentifier("foo bar"));
        assertFalse(TokenStream.isJSIdentifier("foo Bar"));
        assertFalse(TokenStream.isJSIdentifier("foo Bar "));
        assertFalse(TokenStream.isJSIdentifier("foo bar "));
        assertFalse(TokenStream.isJSIdentifier("foo bar123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar-123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_123-"));
        assertFalse(TokenStream.isJSIdentifier("foo bar$123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_$123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar__123"));
        assertFalse(TokenStream.isJSIdentifier("foo bar__123_"));
        assertFalse(TokenStream.isJSIdentifier("foo bar123 "));
        assertFalse(TokenStream.isJSIdentifier("foo bar123_"));
        assertFalse(TokenStream.isJSIdentifier("foo bar123-"));
        assertFalse(TokenStream.isJSIdentifier("foo bar123_"));
    }

    @Test
    public void testIsKeywordWithEmptyString() {
        assertNull(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifierWithNull() {
        assertNull(TokenStream.isJSIdentifier(null));
    }
}