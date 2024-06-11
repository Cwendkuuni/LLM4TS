import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matches("hello"));
        assertTrue(queue.matches("world"));
        assertFalse(queue.matches("hello1"));
        assertFalse(queue.matches("world1"));
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        TokenQueue queue1 = new TokenQueue("abc");
        assertFalse(queue1.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        TokenQueue queue1 = new TokenQueue("xyz");
        assertEquals('x', queue1.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());

        TokenQueue queue1 = new TokenQueue("xyz");
        queue1.addFirst('X');
        assertEquals('X', queue1.peek());
        assertEquals('x', queue1.consume());
        assertEquals('y', queue1.consume());
        assertEquals('z', queue1.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("ac"));

        TokenQueue queue1 = new TokenQueue("xyzxyz");
        assertTrue(queue1.matches("xy"));
        assertTrue(queue1.matches("yz"));
        assertFalse(queue1.matches("zx"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCabc");
        assertTrue(queue.matchesCS("AB"));
        assertTrue(queue.matchesCS("bc"));
        assertFalse(queue.matchesCS("Ac"));

        TokenQueue queue1 = new TokenQueue("XYZxyz");
        assertTrue(queue1.matchesCS("XY"));
        assertTrue(queue1.matchesCS("yz"));
        assertFalse(queue1.matchesCS("zX"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertTrue(queue.matchesAny("1", "5"));
        assertTrue(queue.matchesAny("3", "4"));
        assertFalse(queue.matchesAny("6"));

        TokenQueue queue1 = new TokenQueue("ABCabc");
        assertTrue(queue1.matchesAny("A", "a", "b", "c"));
        assertFalse(queue1.matchesAny("d"));
    }

    @Test
    public void testMatchesAnyStringArray() {
        TokenQueue queue = new TokenQueue("12345");
        TokenQueue queue1 = new TokenQueue("ABCabc");

        String[] arr = {"1", "5"};
        assertTrue(queue.matchesAny(arr));
        assertTrue(queue1.matchesAny(new String[]{"A", "a", "b", "c"}));

        String[] arr1 = {"6", "7"};
        assertFalse(queue.matchesAny(arr1));
        assertFalse(queue1.matchesAny(arr1));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc><def>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.matchChomp("");
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("hello<world>");
        String result = queue.matchChomp("<world>");
        assertEquals("hello", result);
        assertEquals('w', queue.peek());
        assertEquals('o', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('d', queue.consume());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.matchesWhitespace());
        TokenQueue queue1 = new TokenQueue("abc");
        assertFalse(queue1.matchesWhitespace());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchesWordEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.matchesWord();
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("wordword");
        assertEquals('w', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('w', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('d', queue.consume());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());

        TokenQueue queue1 = new TokenQueue("xyz");
        queue1.advance();
        assertEquals('y', queue1.consume());
        assertEquals('z', queue1.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('', queue.consume());

        TokenQueue queue1 = new TokenQueue("xyz");
        assertEquals('x', queue1.consume());
        assertEquals('y', queue1.consume());
        assertEquals('z', queue1.consume());
        assertEquals('', queue1.consume());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consume("12");
        assertEquals("345", result);

        TokenQueue queue1 = new TokenQueue("ABCabc");
        String result1 = queue1.consume("AB");
        assertEquals("abc", result1);

        TokenQueue queue2 = new TokenQueue("ABCabc");
        String result2 = queue2.consume("Ca");
        assertEquals("ABabc", result2);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("12");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceQueueNotMatchingSequence() {
        TokenQueue queue = new TokenQueue("12345");
        queue.consume("67");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceQueueNotLongEnoughToConsumeSequence() {
        TokenQueue queue = new TokenQueue("123");
        queue.consume("1234");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consumeTo("12");
        assertEquals("345", result);
        assertEquals('3', queue.peek());

        TokenQueue queue1 = new TokenQueue("ABCabc");
        String result1 = queue1.consumeTo("AB");
        assertEquals("abc", result1);
        assertEquals('a', queue1.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consumeToIgnoreCase("12");
        assertEquals("345", result);
        assertEquals('3', queue.peek());

        TokenQueue queue1 = new TokenQueue("ABCabc");
        String result1 = queue1.consumeToIgnoreCase("Ab");
        assertEquals("abc", result1);
        assertEquals('a', queue1.peek());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consumeToAny(new String[]{"1", "3"});
        assertEquals("245", result);
        assertEquals('4', queue.peek());

        TokenQueue queue1 = new TokenQueue("ABCabc");
        String result1 = queue1.consumeToAny(new String[]{"A", "b", "c"});
        assertEquals("ABC", result1);
        assertEquals('A', queue1.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())()");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));

        TokenQueue queue1 = new TokenQueue("([{}])");
        assertEquals("[{}]", queue1.chompBalanced('[', ']'));
        assertEquals("{}", queue1.chompBalanced('[', ']'));
        assertEquals("", queue1.chompBalanced('[', ']'));
    }

    @Test(expected = IllegalStateException.class)
    public void testChompBalancedEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.chompBalanced('(', ')');
    }

    @Test
    public void testUnescape() {
        String result = TokenQueue.unescape("\\( \\) \\[ \\] \\{ \\} \\/");
        assertEquals("( ) [ ] { } /", result);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());

        TokenQueue queue1 = new TokenQueue("abc");
        assertFalse(queue1.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("wordword");
        String result = queue.consumeWord();
        assertEquals("word", result);
        assertEquals('w', queue.peek());

        TokenQueue queue1 = new TokenQueue("abcd");
        String result1 = queue1.consumeWord();
        assertEquals("abcd", result1);
        assertEquals('a', queue1.peek());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag><tag>");
        String result = queue.consumeTagName();
        assertEquals("tag", result);
        assertEquals('<', queue.peek());

        TokenQueue queue1 = new TokenQueue("<tag1><tag2>");
        String result1 = queue1.consumeTagName();
        assertEquals("tag1", result1);
        assertEquals('<', queue1.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag:class tag:class tag:id tag:id");
        String result = queue.consumeElementSelector();
        assertEquals("tag:class", result);
        assertEquals(':', queue.peek());

        TokenQueue queue1 = new TokenQueue("tag:class tag:class tag:class tag:id");
        String result1 = queue1.consumeElementSelector();
        assertEquals("tag:class", result1);
        assertEquals(':', queue1.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:identifier class:identifier class:identifier id:identifier");
        String result = queue.consumeCssIdentifier();
        assertEquals("class:identifier", result);
        assertEquals(' ', queue.peek());

        TokenQueue queue1 = new TokenQueue("class:identifier class:identifier class:identifier id:identifier");
        String result1 = queue1.consumeCssIdentifier();
        assertEquals("class:identifier", result1);
        assertEquals(' ', queue1.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("attribute:key attribute:key attribute:key tag:name");
        String result = queue.consumeAttributeKey();
        assertEquals("attribute:key", result);
        assertEquals(' ', queue.peek());

        TokenQueue queue1 = new TokenQueue("attribute:key attribute:key attribute:key tag:name");
        String result1 = queue1.consumeAttributeKey();
        assertEquals("attribute:key", result1);
        assertEquals(' ', queue1.peek());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.remainder();
        assertEquals("5", result);

        TokenQueue queue1 = new TokenQueue("ABCabc");
        String result1 = queue1.remainder();
        assertEquals("abc", result1);
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("5", queue.toString());

        TokenQueue queue1 = new TokenQueue("ABCabc");
        assertEquals("abc", queue1.toString());
    }
}