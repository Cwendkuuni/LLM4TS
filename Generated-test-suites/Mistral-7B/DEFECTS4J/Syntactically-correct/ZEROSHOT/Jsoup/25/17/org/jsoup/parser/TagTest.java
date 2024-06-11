package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreationAndProperties() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 assertTrue(pTag.isBlock());
 assertTrue(pTag.formatAsBlock());
 assertTrue(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());
 assertFalse(pTag.preserveWhitespace());

 assertTrue(divTag.isBlock());
 assertTrue(divTag.formatAsBlock());
 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContainInline());
 assertFalse(divTag.isEmpty());
 assertFalse(divTag.isSelfClosing());
 assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("unknown", unknownTag.getName());
 assertFalse(unknownTag.isBlock());
 assertFalse(unknownTag.formatAsBlock());
 assertFalse(unknownTag.canContainBlock());
 assertFalse(unknownTag.canContainInline());
 assertFalse(unknownTag.isEmpty());
 assertFalse(unknownTag.isSelfClosing());
 assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testKnownTagCheck() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testIsBlockTag() {
 assertTrue(Tag.valueOf("p").isBlock());
 assertTrue(Tag.valueOf("div").isBlock());
 assertFalse(Tag.valueOf("input").isBlock());
 }

 @Test
 public void testIsInlineTag() {
 assertFalse(Tag.valueOf("p").isInline());
 assertFalse(Tag.valueOf("div").isInline());
 assertTrue(Tag.valueOf("input").isInline());
 }

 @Test
 public void testIsDataTag() {
 assertFalse(Tag.valueOf("p").isData());
 assertFalse(Tag.valueOf("div").isData());
 assertTrue(Tag.valueOf("script").isData());
 }

 @Test
 public void testIsEmptyTag() {
 assertFalse(Tag.valueOf("p").isEmpty());
 assertFalse(Tag.valueOf("div").isEmpty());
 assertTrue(Tag.valueOf("img").isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 assertFalse(Tag.valueOf("p").isSelfClosing());
 assertFalse(Tag.valueOf("div").isSelfClosing());
 assertTrue(Tag.valueOf("img").isSelfClosing());
 }

 @Test
 public void testIsKnownTagName() {
 assertTrue(Tag.isKnownTagName("p"));
 assertTrue(Tag.isKnownTagName("div"));
 assertFalse(Tag.isKnownTagName("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 assertTrue(Tag.valueOf("pre").preserveWhitespace());
 assertTrue(Tag.valueOf("plaintext").preserveWhitespace());
 assertFalse(Tag.valueOf("p").preserveWhitespace());
 }
}