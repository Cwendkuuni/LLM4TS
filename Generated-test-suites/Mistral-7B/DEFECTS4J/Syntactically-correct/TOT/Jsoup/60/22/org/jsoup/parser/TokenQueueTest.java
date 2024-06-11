import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testNonEmptyQueue() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('h', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstNullString() {
        TokenQueue queue = new TokenQueue("hello");
        queue.addFirst(null);
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("hello");
        queue.addFirst('a');
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("hello");
        queue.addFirst("abc");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals("abchello", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("ll"));
        Assert.assertTrue(queue.matches("wor"));
        Assert.assertFalse(queue.matches("foo"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("HELLO"));
        Assert.assertTrue(queue.matchesCS("WORLD"));
        Assert.assertFalse(queue.matchesCS("FOO"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123abcdef456");
        Assert.assertTrue(queue.matchesAny("1", "2", "3"));
        Assert.assertTrue(queue.matchesAny("b", "c", "d"));
        Assert.assertTrue(queue.matchesAny("e", "f"));
        Assert.assertFalse(queue.matchesAny("g", "h"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("123abcdef456");
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '2', '3'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'b', 'c', 'd'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'e', 'f'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'g', 'h'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        Assert.assertTrue(queue.matchesStartTag());

        queue.advance(); // consume <
        Assert.assertTrue(queue.matchesStartTag());

        queue.advance(); // consume h
        queue.advance(); // consume t
        queue.advance(); // consume t
        queue.advance(); // consume l
        queue.advance(); // consume e
        queue.advance(); // consume >
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>");
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", queue.matchChomp("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Assert.assertEquals("<html>\n<head>\n<title>", queue.toString());

        queue = new TokenQueue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>foo</title>");
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>", queue.toString());
        Assert.assertEquals("foo", queue.matchChomp("<title>foo</title>"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   \t Hello World   ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals("bcddef", queue.toString());

        queue = new TokenQueue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>foo</title>\n</head>\n<body>\n<p>bar</p>\n</body>\n");
        Assert.assertEquals('<', queue.consume());
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>foo</title>\n</head>\n<body>\n<p>", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnmatchedSequence() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        queue.consume("body");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("<html><head><title>foo<bar></title></head>");
        Assert.assertEquals("<title>foo<bar>", queue.consumeTo("<title>"));
        Assert.assertEquals("</head>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("<html><head><title>foo<BAR></title></head>");
        Assert.assertEquals("<title>foo<BAR>", queue.consumeToIgnoreCase("<title>"));
        Assert.assertEquals("</head>", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("<html><head><title>foo<bar>baz</title></head>");
        Assert.assertEquals("<title>foo<bar>", queue.consumeToAny("<title>"));
        Assert.assertEquals("baz", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        Assert.assertEquals("foo (bar) baz", queue.chompBalanced('(', ')'));
        Assert.assertEquals("qux", queue.toString());

        queue = new TokenQueue("<html><head><title>foo<bar>baz</title></head>");
        Assert.assertEquals("<html><head><title>foo<bar>baz", queue.chompBalanced('<', '>'));
        Assert.assertEquals("</head>", queue.toString());
    }

    @Test
    public void testconsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   \t Hello World   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testconsumeWord() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertEquals("Hello", queue.consumeWord());
        Assert.assertEquals("World", queue.toString());
    }

    @Test
    public void testconsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Title</title></head>");
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("My Title", queue.toString());
    }

    @Test
    public void testconsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/></head>");
        Assert.assertEquals("link", queue.consumeElementSelector());
        Assert.assertEquals("rel=\"stylesheet\" href=\"styles.css\"", queue.toString());
    }

    @Test
    public void testconsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("<style>.foo .bar:hover { color: red; }</style>");
        Assert.assertEquals(".foo", queue.consumeCssIdentifier());
        Assert.assertEquals(".bar:hover", queue.toString());
    }

    @Test
    public void testconsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<p id=\"myId\" class=\"myClass\" style=\"color: red;\">Hello</p>");
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("class", queue.toString());
    }
}