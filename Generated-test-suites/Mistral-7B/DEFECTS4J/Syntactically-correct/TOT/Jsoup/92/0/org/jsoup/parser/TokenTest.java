import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Character;
import org.jsoup.parser.Comment;
import org.jsoup.parser.CData;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);

        Token expected = new Doctype();
        expected.getClass().getSimpleName().equals("Doctype");
        expected.type = TokenType.Doctype;
        expected.asDoctype().getName().equals("html");
        expected.asDoctype().isForceQuirks() = true;

        Assert.assertEquals(expected, doctype);
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        Attributes attributes = new Attributes();
        attributes.put("lang", "en");
        startTag.attributes(attributes);

        Token expected = new StartTag();
        expected.getClass().getSimpleName().equals("StartTag");
        expected.type = TokenType.StartTag;
        expected.asStartTag().name("html").getName().equals("html");
        expected.asStartTag().getAttributes().put("lang", "en");

        Assert.assertEquals(expected, startTag);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("html");

        Token expected = new EndTag();
        expected.getClass().getSimpleName().equals("EndTag");
        expected.type = TokenType.EndTag;
        expected.asEndTag().name("html").getName().equals("html");

        Assert.assertEquals(expected, endTag);
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.getData("This is a comment");

        Token expected = new Comment();
        expected.getClass().getSimpleName().equals("Comment");
        expected.type = TokenType.Comment;
        expected.asComment().getData().equals("This is a comment");

        Assert.assertEquals(expected, comment);
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("A");

        Token expected = new Character();
        expected.getClass().getSimpleName().equals("Character");
        expected.type = TokenType.Character;
        expected.asCharacter().getData().equals("A");

        Assert.assertEquals(expected, character);
    }

    @Test
    public void testCDATAToken() {
        CData cdata = new CData("<![CDATA[This is some CDATA]]>");

        Token expected = new CData();
        expected.getClass().getSimpleName().equals("CData");
        expected.type = TokenType.Character;
        expected.asCharacter().getData().equals("<![CDATA[This is some CDATA]]>");

        Assert.assertEquals(expected, cdata);
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();

        Token expected = new EOF();
        expected.getClass().getSimpleName().equals("EOF");
        expected.type = TokenType.EOF;

        Assert.assertEquals(expected, eof);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        // This test should fail as the class is abstract and cannot be instantiated
        Token invalidToken = new Token();
    }
}