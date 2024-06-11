package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag anotherTag = new Tag(TAG_NAME);

 assertEquals(tag.getName(), tag.tagName);
 assertEquals(tag, anotherTag.setSelfClosing().setSelfClosing());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(divTag.canContainBlock());
 assertFalse(pTag.canContainBlock());
 assertTrue(imgTag.canContainBlock());
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
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag metaTag = Tag.valueOf("meta");

 assertFalse(imgTag.isEmpty());
 assertFalse(brTag.isEmpty());
 assertTrue(metaTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");

 assertFalse(pTag.isSelfClosing());
 assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag(TAG_NAME);

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag(TAG_NAME));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 assertFalse(pTag.preserveWhitespace());
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagMapping() {
 Map<String, Tag> tagMap = new HashMap<>();
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 tagMap.put("p", pTag);
 tagMap.put("div", divTag);

 assertEquals(pTag, tagMap.get("p"));
 assertEquals(divTag, tagMap.get("div"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = null;
 try {
 tag = new Tag(null);
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertEquals("tagName is markInvalid", e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = null;
 try {
 tag = new Tag("");
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertEquals("tagName cannot be empty", e.getMessage());
 }
}