package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("H1");

 assertEquals("p", pTag.getName());
 assertEquals("H1", h1Tag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag h1Tag = Tag.valueOf("h1");

 assertTrue(pTag.canContain(divTag));
 assertTrue(pTag.canContain(h1Tag));
 assertFalse(pTag.canContain(pTag));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagInvalidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("h1");

 assertFalse(h1Tag.canContain(pTag));
 }

 @Test
 public void testTagOptionalClosing() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 Tag newPTag = new Tag("p");
 newPTag.optionalClosing = true;

 assertTrue(pTag.canContain(newPTag));
 assertTrue(pTag.canContain(brTag));
 assertFalse(newPTag.canContain(pTag));
 assertFalse(newPTag.canContain(newPTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmpty() {
 Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithSpaces() {
 Tag.valueOf(" p ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfUpperCase() {
 Tag.valueOf("P");
 }
}