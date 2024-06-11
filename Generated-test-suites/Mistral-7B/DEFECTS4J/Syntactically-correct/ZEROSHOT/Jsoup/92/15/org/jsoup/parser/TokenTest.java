package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenTest {

    @org.junit.Test
    public void testTokenReset() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName().equals("doctype"); // initial test to ensure getSimpleName() has been called

        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPubSysKey());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        Token tagToken = new Token.StartTag();
        tagToken.tagName = "tag";
        tagToken.reset();
        assertEquals(TokenType.StartTag, tagToken.tokenType());
        assertNull(tagToken.tagName);
        assertNull(tagToken.normalName());
        assertNull(tagToken.getAttributes());
        assertFalse(tagToken.isSelfClosing());
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Token tagToken = new Token.StartTag();
        tagToken.tagName = "tag";

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute");
        tagToken.appendAttributeValue("value");
        assertEquals("value", tagToken.getAttributes().getValue("attribute"));

        tagToken.appendAttributeName("anotherAttribute");
        tagToken.appendAttributeValue("anotherValue");
        assertEquals("value", tagToken.getAttributes().getValue("attribute"));
        assertEquals("anotherValue", tagToken.getAttributes().getValue("anotherAttribute"));

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute");
        tagToken.setEmptyAttributeValue();
        assertEquals("", tagToken.getAttributes().getValue("attribute"));
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Token tagToken = new Token.StartTag();
        tagToken.tagName = "tag";

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute");
        tagToken.appendAttributeValue("value");

        tagToken.finaliseTag();
        assertFalse(tagToken.hasPendingAttributeValue());
        assertEquals("value", tagToken.getAttributes().getValue("attribute"));

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute");
        tagToken.appendAttributeValue("value");
        tagToken.finaliseTag();
        assertEquals(2, tagToken.getAttributes().size());
    }

    @org.junit.Test
    public void testTokenStringConcatenation() {
        Token tagToken = new Token.StartTag();
        tagToken.tagName = "tag";

        tagToken.appendTagName("test");
        tagToken.appendTagName("123");
        assertEquals("tagtest123", tagToken.name());

        tagToken.appendAttributeName("attribute");
        tagToken.appendAttributeValue("value");
        tagToken.appendTagName("test2");
        assertEquals("<tag test=\"value\"test2>", tagToken.toString());
    }

    @org.junit.Test
    public void testTokenCharacterAppend() {
        Token characterToken = new Token.Character();

        characterToken.appendAttributeValue("abc");
        assertEquals("abc", characterToken.getData());

        characterToken.appendCharacter('d');
        assertEquals("abc" + "d", characterToken.getData());

        characterToken.appendCharacter('\n');
        assertEquals("abc" + "d" + "\n", characterToken.getData());

        characterToken.appendCharacter('\u0001');
        assertEquals("abc" + "d" + "\n" + "\u0001", characterToken.getData());
    }

    @org.junit.Test
    public void testTokenCDataCharacterAppend() {
        Token cdataToken = new Token.CData("<![CDATA[abc]>");

        cdataToken.appendCharacter('d');
        assertEquals("<![CDATA[abc]>d", cdataToken.getData());

        cdataToken.appendCharacter('\n');
        assertEquals("<![CDATA[abc]>\nd", cdataToken.getData());

        cdataToken.appendCharacter('\u0001');
        assertEquals("<![CDATA[abc]>\nd\u0001", cdataToken.getData());
    }

    @org.junit.Test
    public void testTokenCommentCharacterAppend() {
        Token commentToken = new Token.Comment();

        commentToken.appendAttributeValue("--abc");
        assertEquals("--abc", commentToken.getData());

        commentToken.appendCharacter('d');
        assertEquals("--abc" + "d", commentToken.getData());

        commentToken.appendCharacter('\n');
        assertEquals("--abc" + "d" + "\n", commentToken.getData());

        commentToken.appendCharacter('\u0001');
        assertEquals("--abc" + "d" + "\n" + "\u0001", commentToken.getData());
    }

    @org.junit.Test
    public void testTokenAttributes() {
        Token tagToken = new Token.StartTag();
        tagToken.tagName = "tag";

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute1");
        tagToken.appendAttributeValue("value1");

        tagToken.newAttribute();
        tagToken.appendAttributeName("attribute2");
        tagToken.appendAttributeValue("value2");

        List<String> expectedAttributeNames = new ArrayList<>();
        expectedAttributeNames.add("attribute1");
        expectedAttributeNames.add("attribute2");

        List<String> actualAttributeNames = new ArrayList<>();
        for (String attributeName : tagToken.getAttributes().keySet()) {
            actualAttributeNames.add(attributeName);
        }

        assertEquals(expectedAttributeNames, actualAttributeNames);

        assertEquals("value1", tagToken.getAttributes().getValue("attribute1"));
        assertEquals("value2", tagToken.getAttributes().getValue("attribute2"));
    }
}