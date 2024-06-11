import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test true cases
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("const"));

        // Test false cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("a"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("myVar"));
        assertFalse(TokenStream.isKeyword("my_var"));
        assertFalse(TokenStream.isKeyword("myVar_123"));
        assertFalse(TokenStream.isKeyword("MyVar"));
        assertFalse(TokenStream.isKeyword("My_Var"));
        assertFalse(TokenStream.isKeyword("MyVar_123"));
        assertFalse(TokenStream.isKeyword("MyVar_123_"));
        assertFalse(TokenStream.isKeyword("MyVar_123_abc"));
        assertFalse(TokenStream.isKeyword("MyVar_123_abc_def"));
        assertFalse(TokenStream.isKeyword("MyVar_123_abc_def_ghi"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test true cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        assertTrue(TokenStream.isJSIdentifier("my_var"));
        assertTrue(TokenStream.isJSIdentifier("myVar_123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar"));
        assertTrue(TokenStream.isJSIdentifier("My_Var"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_abc"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_abc_def"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123_abc_def_ghi"));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("_abc"));
        assertTrue(TokenStream.isJSIdentifier("abc_"));
        assertTrue(TokenStream.isJSIdentifier("abc_def"));
        assertTrue(TokenStream.isJSIdentifier("abc_def_ghi"));

        // Test false cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("myVar123"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123_"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123_abc_def_ghi_"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123_abc_def_ghi_1"));
    }
}