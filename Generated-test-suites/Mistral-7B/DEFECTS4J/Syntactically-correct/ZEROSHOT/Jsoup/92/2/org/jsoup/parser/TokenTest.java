package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Token abstract class and its subclasses.
 */
public class TokenTest {

    private Doctype doctype;
    private StartTag startTag;
    private EndTag endTag;
    private Comment comment;
    private Character character;
    private CData cData;
    private EOF eof;

    @Before
    public void setup() {
        doctype = new Doctype();
        startTag = new StartTag();
        endTag = new EndTag();
        comment = new Comment();
        character = new Character();
        cData = new CData("This is a CData");
        eof = new EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.getName().equals("Doctype");

        Doctype resetDoctype = doctype.reset();
        resetDoctype.getClass().equals(Doctype.class);
        resetDoctype.getName().equals("");
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeMethods() {
        doctype.getName("html").getName().equals("html");

        String pubSysKey = "public: ";
        doctype.setPubSysKey(pubSysKey);
        String gotPubSysKey = doctype.getPubSysKey();
        gotPubSysKey.equals(pubSysKey);

        String publicIdentifier = "public id: ";
        doctype.setPublicIdentifier(publicIdentifier);
        String gotPublicIdentifier = doctype.getPublicIdentifier();
        gotPublicIdentifier.equals(publicIdentifier);

        String systemIdentifier = "system id: ";
        doctype.setSystemIdentifier(systemIdentifier);
        String gotSystemIdentifier = doctype.getSystemIdentifier();
        gotSystemIdentifier.equals(systemIdentifier);

        doctype.setForceQuirks(true);
        boolean isForceQuirks = doctype.isForceQuirks();
        isForceQuirks = true;
    }

    @Test
    public void testStartTagReset() {
        startTag.name().equals("StartTag");

        StartTag resetStartTag = startTag.reset();
        resetStartTag.getClass().equals(StartTag.class);
        resetStartTag.name().equals(null);
    }

    @Test
    public void testStartTagMethods() {
        startTag.name("div").name().equals("div");
        String normalName = "divLowercase";
        startTag.normalName(normalName);
        startTag.normalName().equals(normalName);

        startTag.selfClosing(false);
        assertFalse(startTag.isSelfClosing());

        startTag.newAttribute().getAttribute("id").equals("");
        startTag.newAttribute("class", "class1 class2").getAttribute("class").equals("class1 class2");

        String tagName = "myTag";
        startTag.name(tagName);
        String gotTagName = startTag.name().equals(tagName) ? tagName : "";
        gotTagName.equals(tagName);

        startTag.name("div").attributes().get("class").equals("class1 class2");
        startTag.name("div").attributes().get("id").equals("id");
    }

    @Test
    public void testEndTagReset() {
        endTag.name().equals("EndTag");

        EndTag resetEndTag = endTag.reset();
        resetEndTag.getClass().equals(EndTag.class);
        resetEndTag.name().equals(null);
    }

    @Test
    public void testEndTagMethods() {
        endTag.name("div");
        String gotName = endTag.name().equals("div") ? "div" : "";
        gotName.equals("div");
    }

    @Test
    public void testCommentReset() {
        comment.getData().equals("");

        Comment resetComment = comment.reset();
        resetComment.getClass().equals(Comment.class);
        resetComment.getData().equals("");
        comment.getData().equals("");
    }

    @Test
    public void testCommentMethods() {
        comment.getData("<!-- This is a comment -->");
        String gotData = comment.getData().equals("This is a comment") ? "This is a comment" : "";
        gotData.equals("This is a comment");

        comment.setBogus(true);
        boolean isBogus = comment.isBogus();
        isBogus = true;
    }

    @Test
    public void testCharacterReset() {
        character.getData().equals("");

        Character resetCharacter = character.reset();
        resetCharacter.getClass().equals(Character.class);
        resetCharacter.getData().equals("");
        character.getData().equals("");
    }

    @Test
    public void testCharacterMethods() {
        character.getData("A");
        String gotData = character.getData().equals("A") ? "A" : "";
        gotData.equals("A");
    }

    @Test
    public void testCDataReset() {
        cData.getData().equals("");

        CData resetCData = cData.reset();
        resetCData.getClass().equals(CData.class);
        resetCData.getData().equals("");
        cData.getData().equals("");
    }

    @Test
    public void testCDataMethods() {
        cData.getData("<!-- This is a CData --><![CDATA[ <p>This is a CData</p> ]]>");
        String gotData = cData.getData().equals("This is a CData") ? "This is a CData" : "";
        gotData.equals("This is a CData");
    }

    @Test
    public void testEOFReset() {
        // EOF does not have any methods or fields to test.
    }

    @Test
    public void testEnumTokenType() {
        TokenType doctypeTokenType = TokenType.Doctype;
        TokenType startTagTokenType = TokenType.StartTag;
        TokenType endTagTokenType = TokenType.EndTag;
        TokenType commentTokenType = TokenType.Comment;
        TokenType characterTokenType = TokenType.Character;
        TokenType eofTokenType = TokenType.EOF;

        List<TokenType> tokenTypes = Arrays.asList(doctypeTokenType, startTagTokenType, endTagTokenType, commentTokenType, characterTokenType, eofTokenType);

        for (TokenType tokenType : tokenTypes) {
            String tokenTypeName = tokenType.toString();
            Token token = null;

            switch (tokenType) {
                case Doctype:
                    token = doctype;
                    break;
                case StartTag:
                    token = startTag;
                    break;
                case EndTag:
                    token = endTag;
                    break;
                case Comment:
                    token = comment;
                    break;
                case Character:
                    token = character;
                    break;
                case EOF:
                    token = eof;
                    break;
                default:
                    Validate.isFalse(true); // unreachable
            }

            String tokenClassName = token.getClass().getSimpleName();
            String expectedTokenClassName = tokenTypeName;

            assertThat(token, is(instanceOf(Token.class)));
            assertThat(token, is(notNullValue()));
            assertThat(tokenClassName, is(expectedTokenClassName));
        }
    }

    @Test
    public void testIsTokenType() {
        doctype.isDoctype();
        endTag.isDoctype();
        startTag.isDoctype();
        comment.isDoctype();
        character.isDoctype();
        eof.isDoctype();

        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        assertFalse(endTag.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isCharacter());
        assertFalse(character.isEOF());

        assertTrue(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTypeSpecificMethods() {
        doctype.getName("html").equals("html");
        String pubSysKey = "public: ";
        doctype.setPubSysKey(pubSysKey);
        String gotPubSysKey = doctype.getPubSysKey();
        gotPubSysKey.equals(pubSysKey);
        doctype.setForceQuirks(true);
        boolean isForceQuirks = doctype.isForceQuirks();
        isForceQuirks = true;

        startTag.name("div");
        String normalName = "divLowercase";
        startTag.normalName(normalName);
        startTag.normalName().equals(normalName);
        startTag.selfClosing(false);
        assertFalse(startTag.isSelfClosing());
        startTag.newAttribute("id", "idValue");
        String gotIdValue = startTag.getAttribute("id");
        gotIdValue.equals("idValue");
        startTag.newAttribute("class", "class1 class2");
        String gotClassValues = startTag.getAttribute("class");
        Arrays.asList(gotClassValues.split("\\s+")).containsAll(Arrays.asList("class1", "class2"));

        endTag.name("div");
        String gotEndTagName = endTag.name().equals("div") ? "div" : "";
        gotEndTagName.equals("div");

        comment.getData("<!-- This is a comment -->");
        String gotCommentData = comment.getData().equals("This is a comment") ? "This is a comment" : "";
        gotCommentData.equals("This is a comment");
        comment.setBogus(true);
        boolean isBogusComment = comment.isBogus();
        isBogusComment = true;

        character.getData("A");
        String gotCharacterData = character.getData().equals("A") ? "A" : "";
        gotCharacterData.equals("A");

        cData.getData("<!-- This is a CData --><![CDATA[ <p>This is a CData</p> ]]>");
        String gotCDataData = cData.getData().equals("This is a CData") ? "This is a CData" : "";
        gotCDataData.equals("This is a CData");
    }
}