package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = new Tag(TAG_NAME);
 Tag expectedTag = new Tag(TAG_NAME);

 assertEquals("Incorrect tag name", TAG_NAME, tag.getName());
 assertEquals("Tags should be equal", expectedTag, tag);
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag expectedTag = new Tag(TAG_NAME);

 assertEquals("Tags should be equal", expectedTag, tag);
 }

 @Test
 public void testIsBlockTag() {
 Tag blockTag = Tag.valueOf("div");
 Tag inlineTag = Tag.valueOf("span");

 assertTrue("Div should be block tag", blockTag.isBlock());
 assertTrue("Span should be inline tag", inlineTag.isInline());
 }

 @Test
 public void testFormatAsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag preTag = Tag.valueOf("pre");

 assertTrue("p should format as block", pTag.formatAsBlock());
 assertTrue("pre should format as block", preTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTag() {
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue("div should be able to contain block tags", divTag.canContainBlock());
 assertFalse("span should not be able to contain block tags", spanTag.canContainBlock());
 }

 @Test
 public void testCanContainInlineTag() {
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue("div should be able to contain inline tags", divTag.canContainInline());
 assertTrue("span should be able to contain inline tags", spanTag.canContainInline());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");

 assertFalse("img should not be empty", imgTag.isEmpty());
 assertFalse("p should not be empty", pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse("img should not be self closing", imgTag.isSelfClosing());
 assertTrue("br should be self closing", brTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = new Tag("unknownTag");

 assertTrue("div is a known tag", Tag.isKnownTag("div"));
 assertFalse("unknownTag is not a known tag", Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 assertTrue("pre should preserve whitespace", preTag.preserveWhitespace());
 assertFalse("p should not preserve whitespace", pTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = null;
 try {
 tag = new Tag(null);
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNotNull("Exception should be thrown", e);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = null;
 try {
 tag = new Tag("");
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNotNull("Exception should be thrown", e);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhitespaceTagName() {
 Tag tag = null;
 try {
 tag = new Tag(" tag ");
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNotNull("Exception should be thrown", e);
 }
}