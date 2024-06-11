package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertEquals("img", imgTag.getName());
 }

 @Test
 public void testIsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.formatAsBlock());
 assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsInlineTag() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertFalse(pTag.isInline());
 assertTrue(imgTag.isInline());
 }

 @Test
 public void testIsDataTag() {
 Tag divTag = Tag.valueOf("div");
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(divTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(inputTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(inputTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknown");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertEquals(pTag1, pTag2);
 assertNotEquals(pTag1, imgTag);
 }
}