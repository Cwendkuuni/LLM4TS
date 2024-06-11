package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("img", imgTag.getName());
 assertEquals("div", divTag.getName());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
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
 public void testCanContainInline() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.canContainInline());
 assertTrue(divTag.canContainInline());
 assertFalse(imgTag.canContainInline());
 }

 @Test
 public void testOptionalClosing() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");

 assertFalse(pTag.isOptionalClosing());
 assertFalse(pTag2.isOptionalClosing());

 Tag pTagWithOptionalClosing = new Tag("p").setOptionalClosing();
 assertTrue(pTagWithOptionalClosing.isOptionalClosing());
 }

 @Test
 public void testEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");

 assertFalse(preTag.preserveWhitespace());
 assertFalse(textareaTag.preserveWhitespace());

 Tag preTagWithPreserveWhitespace = new Tag("pre").setPreserveWhitespace();
 assertTrue(preTagWithPreserveWhitespace.preserveWhitespace());
 }

 @Test
 public void testTagValidation() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");
 Tag metaTag = Tag.valueOf("meta");
 Tag spanTag = Tag.valueOf("span");

 // Check valid parents
 assertTrue(pTag.canContain(pTag));
 assertTrue(pTag.canContain(metaTag));
 assertTrue(pTag.canContain(scriptTag));
 assertTrue(scriptTag.canContain(metaTag));

 // Check invalid parents
 assertFalse(pTag.canContain(scriptTag));
 assertFalse(scriptTag.canContain(pTag));
 assertFalse(pTag.canContain(spanTag));
 assertFalse(spanTag.canContain(pTag));
 }

 @Test
 public void testTagValidationWithAncestor() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");

 bodyTag.ancestors.add(pTag);
 bodyTag.ancestors.add(divTag);

 // Check valid children
 assertTrue(pTag.isValidParent(divTag));
 assertTrue(divTag.isValidParent(pTag));

 // Check invalid children
 assertFalse(pTag.isValidParent(bodyTag));
 assertFalse(bodyTag.isValidParent(pTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }
}