package org.jsoup.parser.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assertions.assertEquals(TokenType.Doctype, doctypeToken.type);

        doctypeToken.reset();

        doctypeToken.name().equals("Doctype");
        doctypeToken.asDoctype().getName().equals("");
        doctypeToken.asDoctype().getPubSysKey().equals(null);
        doctypeToken.asDoctype().getPublicIdentifier().equals("");
        doctypeToken.asDoctype().getSystemIdentifier().equals("");
        doctypeToken.asDoctype().isForceQuirks().equals(false);

        doctypeToken.getName().equals("Doctype");
        doctypeToken.asDoctype().getName().equals("Doctype");

        doctypeToken.asDoctype().setName("TestDoctype");
        doctypeToken.asDoctype().getName().equals("TestDoctype");

        doctypeToken.asDoctype().getPublicIdentifier().equals("TestPublicIdentifier");
        doctypeToken.asDoctype().getSystemIdentifier().equals("TestSystemIdentifier");

        doctypeToken.asDoctype().setPublicIdentifier("TestPublicIdentifier");
        doctypeToken.asDoctype().getPublicIdentifier().equals("TestPublicIdentifier");

        doctypeToken.asDoctype().setSystemIdentifier("TestSystemIdentifier");
        doctypeToken.asDoctype().getSystemIdentifier().equals("TestSystemIdentifier");

        doctypeToken.asDoctype().setForceQuirks(true);
        doctypeToken.asDoctype().isForceQuirks().equals(true);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Assertions.assertEquals(TokenType.StartTag, startTagToken.type);

        startTagToken.reset();

        startTagToken.name().equals("StartTag");
        startTagToken.asStartTag().normalName().equals("starttag");

        startTagToken.name("TestStartTag");
        startTagToken.asStartTag().name().equals("TestStartTag");
        startTagToken.asStartTag().name("teststarttag");
        startTagToken.asStartTag().normalName().equals("teststarttag");

        startTagTagToken.asStartTag().getAttributes().size().equals(0);

        startTagToken.newAttribute();
        startTagToken.asStartTag().getAttributes().size().equals(1);

        startTagToken.newAttribute().setName("TestAttribute");
        startTagToken.newAttribute().setName("testattribute");
        startTagToken.asStartTag().getAttributes().put(new Attribute("TestAttribute", "TestValue"));
        startTagToken.asStartTag().getAttributes().put(new Attribute("testattribute", "testvalue"));

        startTagToken.newAttribute().setName("TestAttribute");
        startTagToken.newAttribute().setName("testattribute");
        startTagToken.newAttribute().setEmptyAttributeValue();
        startTagToken.asStartTag().getAttributes().put(new Attribute("TestAttribute"));
        startTagToken.asStartTag().getAttributes().put(new Attribute("testattribute"));

        startTagToken.newAttribute().setName("TestAttribute");
        startTagToken.newAttribute().setName("testattribute");
        startTagToken.newAttribute().setPendingAttributeValue("TestValue");
        startTagToken.newAttribute().setHasPendingAttributeValue(true);
        startTagToken.asStartTag().getAttributes().put(new Attribute("TestAttribute", "TestValue"));
        startTagToken.asStartTag().getAttributes().put(new Attribute("testattribute", "testvalue"));

        startTagToken.finaliseTag();
        Assertions.assertTrue(startTagToken.asStartTag().getAttributes().size() > 0);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Assertions.assertEquals(TokenType.EndTag, endTagToken.type);

        endTagToken.reset();

        endTagToken.name().equals("EndTag");
        endTagToken.asEndTag().normalName().equals("endtag");

        endTagToken.name("TestEndTag");
        endTagToken.asEndTag().name().equals("TestEndTag");
        endTagToken.asEndTag().name("testendtag");
        endTagToken.asEndTag().normalName().equals("testendtag");

        endTagToken.asEndTag().getAttributes().size().equals(0);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Assertions.assertEquals(TokenType.Comment, commentToken.type);

        commentToken.reset();

        commentToken.asComment().getData().equals("");

        commentToken.asComment().appendAttributeName("TestCommentData");
        commentToken.asComment().appendAttributeValue("TestDataValue");
        commentToken.asComment().setEmptyAttributeValue();
        commentToken.asComment().getData().equals("TestCommentDataTestDataValue");

        commentToken.asComment().appendAttributeName("TestCommentData");
        commentToken.asComment().appendAttributeValue("TestDataValue");
        commentToken.asComment().setEmptyAttributeValue();
        commentToken.asComment().getData().equals("TestCommentDataTestDataValue");

        commentToken.asComment().appendAttributeName("TestCommentData");
        commentToken.asComment().appendAttributeValue("TestDataValue");
        commentToken.asComment().setHasPendingAttributeValue(true);
        commentToken.asComment().getData().equals("TestCommentDataTestDataValue");

        commentToken.asComment().getData().equals("TestCommentDataTestDataValue");
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Assertions.assertEquals(TokenType.Character, characterToken.type);

        characterToken.reset();

        characterToken.asCharacter().getData().equals("");

        characterToken.asCharacter().data("TestCharacterData");
        characterToken.asCharacter().getData().equals("TestCharacterData");

        characterToken.asCharacter().data("Test\u0001CharacterData");
        characterToken.asCharacter().getData().equals("Test\u0001CharacterData");

        characterToken.asCharacter().data("Test\u0001\u0002CharacterData");
        characterToken.asCharacter().getData().equals("Test\u0001\u0002CharacterData");

        characterToken.asCharacter().data("Test\uD800\uDC00CharacterData");
        characterToken.asCharacter().getData().equals("Test\uD800\uDC00CharacterData");
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Assertions.assertEquals(TokenType.EOF, eofToken.type);

        eofToken.reset();

        Assertions.assertEquals(TokenType.EOF, eofToken.type);
    }

    @Test
    public void testAllTokenTypes() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token eofToken = new Token.EOF();

        Assertions.assertAll(
                () -> Assertions.assertEquals(TokenType.Doctype, doctypeToken.type),
                () -> Assertions.assertEquals(TokenType.StartTag, startTagToken.type),
                () -> Assertions.assertEquals(TokenType.EndTag, endTagToken.type),
                () -> Assertions.assertEquals(TokenType.Comment, commentToken.type),
                () -> Assertions.assertEquals(TokenType.Character, characterToken.type),
                () -> Assertions.assertEquals(TokenType.EOF, eofToken.type)
        );
    }

    @Test
    public void testTokenToString() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token eofToken = new Token.EOF();

        String[] expectedStrings = {"<doctype>", "",
                "<startTag name=\"StartTag\">", "<startTag name=\"teststarttag\">",
                "</endTag>", "<endTag name=\"endtag\">",
                "<!-- -->", "<!-- TestCommentDataTestDataValue -->",
                "TestCharacterData", "TestCharacterData",
                "", ""};

        Assertions.assertAll(
                () -> Assertions.assertEquals("<doctype>", doctypeToken.toString()),
                () -> Assertions.assertEquals("", startTagToken.toString()),
                () -> Assertions.assertEquals("<startTag name=\"StartTag\">", startTagToken.toString()),
                () -> Assertions.assertEquals("<startTag name=\"teststarttag\">", startTagToken.toString()),
                () -> Assertions.assertEquals("", endTagToken.toString()),
                () -> Assertions.assertEquals("<endTag name=\"endtag\">", endTagToken.toString()),
                () -> Assertions.assertEquals("<!-- -->", commentToken.toString()),
                () -> Assertions.assertEquals("<!-- TestCommentDataTestDataValue -->", commentToken.toString()),
                () -> Assertions.assertEquals("TestCharacterData", characterToken.toString()),
                () -> Assertions.assertEquals("TestCharacterData", characterToken.toString()),
                () -> Assertions.assertEquals("", eofToken.toString())
        );
    }

    @Test
    public void testTokenTypeEnumValues() {
        TokenType[] expectedValues = {TokenType.Doctype, TokenType.StartTag, TokenType.EndTag, TokenType.Comment, TokenType.Character, TokenType.EOF};

        TokenType[] actualValues = TokenType.values();

        Assertions.assertArrayEquals(expectedValues, actualValues);
    }
}