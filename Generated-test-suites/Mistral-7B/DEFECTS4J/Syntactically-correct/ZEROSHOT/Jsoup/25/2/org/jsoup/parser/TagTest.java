package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf(TAG_NAME);
 assertEquals(TAG_NAME, tag.getName());
 Assert.assertTrue(tag.isKnownTag());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf("UnknownTag");
 assertEquals("unknowntag", tag.getName());
 Assert.assertFalse(tag.isKnownTag());
 }

 @Test
 public void testIsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.formatAsBlock());

 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag aTag = Tag.valueOf("a");
 assertFalse(aTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainBlock());

 Tag divTag = Tag.valueOf("div");
 assertTrue(divTag.canContainBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isEmpty());

 Tag metaTag = Tag.valueOf("meta");
 assertTrue(metaTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isSelfClosing());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isSelfClosing());
 }

 @Test
 public void testIsDataTag() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isData());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("UnknownTag"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testTagHashCodeAndEquals() {
 Tag tag1 = new Tag(TAG_NAME);
 Tag tag2 = new Tag(TAG_NAME);
 Tag tag3 = new Tag("DIV");

 assertEquals(tag1.hashCode(), tag2.hashCode());
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("testTag");
 assertEquals("testtag", tag.toString());
 }
}