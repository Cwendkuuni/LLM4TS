package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagProperties() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 assertTrue(pTag.isInline());
 assertFalse(divTag.isInline());

 assertFalse(pTag.isBlock());
 assertTrue(divTag.isBlock());

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());

 assertFalse(pTag.canContainInline());
 assertTrue(divTag.canContainInline());

 assertFalse(pTag.isEmpty());
 assertFalse(divTag.isEmpty());

 assertFalse(pTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());

 assertFalse(pTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag unknownTag = Tag.valueOf("unknown");
 assertEquals("unknown", unknownTag.getName());
 assertFalse(unknownTag.isBlock());
 assertTrue(unknownTag.canContainBlock());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testKnownTags() {
 Tag[] knownTags = {Tag.valueOf("p"), Tag.valueOf("div"), Tag.valueOf("img"), Tag.valueOf("script")};
 for (Tag tag : knownTags) {
 assertTrue(Tag.isKnownTag(tag.getName()));
 }
 }

 @Test
 public void testEmptyTags() {
 Tag metaTag = Tag.valueOf("meta");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(metaTag.isEmpty());
 assertTrue(imgTag.isSelfClosing());
 assertFalse(metaTag.canContainBlock());
 assertFalse(metaTag.canContainInline());
 }

 @Test
 public void testFormatAsBlockTags() {
 Tag preTag = Tag.valueOf("pre");
 Tag titleTag = Tag.valueOf("title");

 assertTrue(preTag.formatAsBlock());
 assertFalse(titleTag.formatAsBlock());
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }
}