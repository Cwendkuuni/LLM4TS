package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testNormalizedTagName() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("mytag");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock()); // same as canContainBlock

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock()); // same as canContainBlock
 }

 @Test
 public void testIsInline() {
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isInline());

 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isData());

 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag imgSelfClosingTag = imgTag.setSelfClosing();
 assertTrue(imgSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("mytag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormListed());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormSubmittable());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.isFormSubmittable());
 }
}