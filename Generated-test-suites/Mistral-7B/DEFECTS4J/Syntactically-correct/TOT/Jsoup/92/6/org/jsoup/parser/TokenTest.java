import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.CData;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(false);

        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("html", doctype.getName());
        Assert.assertFalse(doctype.isForceQuirks());

        doctype.reset();

        doctype.getName("html");
        doctype.setForceQuirks(true);

        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("html", doctype.getName());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        Assert.assertEquals("StartTag", startTag.tokenType().name());
        Assert.assertEquals("html", startTag.name());

        startTag.name("html").attributes(new Attributes().put("class", "test"));

        Assert.assertEquals("html", startTag.name());
        Assert.assertEquals("test", startTag.getAttributes().get("class"));

        startTag.reset();

        Assert.assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("html");

        Assert.assertEquals("EndTag", endTag.tokenType().name());
        Assert.assertEquals("html", endTag.name());
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data("This is a comment");

        Assert.assertEquals("Comment", comment.tokenType().name());
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("A");

        Assert.assertEquals("Character", character.tokenType().name());
        Assert.assertEquals("A", character.getData());
    }

    @Test
    public void testCDataToken() {
        CData cdata = new CData("<script src='script.js'></script>");

        Assert.assertEquals("Character", cdata.tokenType().name());
        Assert.assertEquals("<![CDATA[", cdata.toString().substring(0, 9));
        Assert.assertEquals("]]>", cdata.toString().substring(cdata.toString().length() - 3));
        Assert.assertEquals("<script src='script.js'></script>", cdata.getData());
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();

        Assert.assertEquals("EOF", eof.tokenType().name());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        Token token = new Token();
        Assert.assertFalse(token.isDoctype());
    }
}