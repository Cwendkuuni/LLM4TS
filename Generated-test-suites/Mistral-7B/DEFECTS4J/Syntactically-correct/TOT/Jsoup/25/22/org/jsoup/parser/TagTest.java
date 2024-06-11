package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName.toLowerCase(), tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);
 assertNotEquals(tagName, tag.getName()); // should return a default tag
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testTagIsBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 assertTrue(tag1.isBlock());

 String tagName2 = "img";
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag2.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 assertTrue(tag1.formatAsBlock());

 String tagName2 = "input";
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag2.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName1 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 assertTrue(tag1.canContainBlock());

 String tagName2 = "br";
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag2.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName1 = "img";
 Tag tag1 = Tag.valueOf(tagName1);
 assertFalse(tag1.isEmpty());

 String tagName2 = "input";
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag2.isEmpty());

 String tagName3 = "br";
 Tag tag3 = Tag.valueOf(tagName3);
 assertTrue(tag3.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 String tagName1 = "img";
 Tag tag1 = Tag.valueOf(tagName1);
 assertFalse(tag1.isSelfClosing());

 String tagName2 = "input";
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag2.isSelfClosing());

 String tagName3 = "img";
 Tag tag3 = Tag.valueOf(tagName3);
 tag3.setSelfClosing();
 assertTrue(tag3.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName1 = "p";
 assertTrue(Tag.isKnownTag(tagName1));

 String tagName2 = "mytag";
 assertFalse(Tag.isKnownTag(tagName2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 String tagName = null;
 Tag tag = Tag.valueOf(tagName);
 fail();
 }
}