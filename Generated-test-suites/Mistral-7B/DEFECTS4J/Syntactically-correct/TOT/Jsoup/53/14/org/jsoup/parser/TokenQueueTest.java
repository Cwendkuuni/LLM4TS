package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("hello"));
        Assert.assertFalse(queue.matchesCS("Hello"));
    }

    @Test
    public void testBasicFunctions() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertThat(queue.peek(), is('h'));
        Assert.assertThat(queue.matches("he"), is(true));
        Assert.assertThat(queue.matchesCS("He"), is(true));
        Assert.assertThat(queue.matches("ll"), is(true));
        Assert.assertThat(queue.matches("llo"), is(true));
        Assert.assertThat(queue.matches("world"), is(false));
        Assert.assertThat(queue.matchesAny("he", "ll"), is(true));
        Assert.assertThat(queue.matchesAny("world"), is(false));
        Assert.assertThat(queue.matchesAny('o', 'l'), is(true));
        Assert.assertThat(queue.matchesStartTag(), is(false));
        Assert.assertThat(queue.matchChomp("he"), is(true));
        Assert.assertThat(queue.matchChomp("He"), is(false));
        Assert.assertThat(queue.matchesWhitespace(), is(false));
        Assert.assertThat(queue.matchesWord(), is(true));
        queue.advance();
        Assert.assertThat(queue.matchesWord(), is(true));
        queue.consume();
        Assert.assertThat(queue.matchesWord(), is(false));
        Assert.assertThat(queue.consume("llo"), is("llo"));
        Assert.assertThat(queue.consume("world"), is("world"));
        Assert.assertThat(queue.consumeTo("ll"), is("ll"));
        Assert.assertThat(queue.consumeToIgnoreCase("Hello"), is("hello"));
        Assert.assertThat(queue.consumeToAny("lo", "ll"), is("ll"));
        Assert.assertThat(queue.chompBalanced('(', ')'), is("hello"));
        Assert.assertThat(queue.consumeToAny("lo", "ll"), is(""));
        Assert.assertThat(queue.chompBalanced('{', '}'), is(""));
        Assert.assertThat(queue.consumeWhitespace(), is(true));
        Assert.assertThat(queue.consumeWord(), is("world"));
        Assert.assertThat(queue.consumeTagName(), is("hello"));
        Assert.assertThat(queue.consumeElementSelector(), is("hello"));
        Assert.assertThat(queue.consumeCssIdentifier(), is("world"));
        Assert.assertThat(queue.consumeAttributeKey(), is(""));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeException() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("world");
        queue.consume("hello");
        queue.consume("world"); // should be empty after this
        queue.consume("world"); // should throw exception
    }
}