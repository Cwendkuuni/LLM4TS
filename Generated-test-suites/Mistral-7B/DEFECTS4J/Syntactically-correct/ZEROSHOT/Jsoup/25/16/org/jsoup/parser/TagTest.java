package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 assertTrue(pTag.isInline());
 assertTrue(divTag.isBlock());

 assertFalse(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());

 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());

 assertFalse(pTag.preserveWhitespace());

 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContainInline());

 assertFalse(divTag.isEmpty());
 assertFalse(divTag.isSelfClosing());

 assertTrue(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertEquals("unknownTag", unknownTag.getName());

 assertFalse(unknownTag.isBlock());
 assertTrue(unknownTag.canContainBlock());

 assertFalse(unknownTag.canContainInline());
 assertFalse(unknownTag.isEmpty());

 assertFalse(unknownTag.isSelfClosing());
 assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));

 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Tag preTag = Tag.valueOf("pre");
 Tag plaintextTag = Tag.valueOf("plaintext");

 assertTrue(preTag.preserveWhitespace());
 assertTrue(plaintextTag.preserveWhitespace());
 }

 @Test
 public void testEmptyTags() {
 Tag metaTag = Tag.valueOf("meta");
 Tag imgTag = Tag.valueOf("img");

 assertFalse(metaTag.isEmpty());
 assertFalse(imgTag.isEmpty());

 assertTrue(metaTag.canContainBlock());
 assertTrue(metaTag.canContainInline());

 assertFalse(imgTag.canContainBlock());
 assertFalse(imgTag.canContainInline());

 assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testFormatAsInlineTags() {
 Tag titleTag = Tag.valueOf("title");

 assertTrue(titleTag.formatAsBlock());

 assertFalse(titleTag.isBlock());
 assertFalse(titleTag.canContainBlock());

 assertTrue(titleTag.canContainInline());
 assertFalse(titleTag.isEmpty());

 assertFalse(titleTag.isSelfClosing());
 assertFalse(titleTag.preserveWhitespace());
 }
}