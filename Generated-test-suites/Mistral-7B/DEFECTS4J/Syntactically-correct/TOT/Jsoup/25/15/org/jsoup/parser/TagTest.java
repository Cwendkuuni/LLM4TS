package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testBlockTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("div", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testUnknownTag() {
 String tagName = "zxcvbnm";
 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals("zxcvbnm", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testBlockTagProperties() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 tag.setSelfClosing();

 Assert.assertEquals("p", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertTrue(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testIsKnownTag() {
 String tagName1 = "div";
 String tagName2 = "zxcvbnm";

 Assert.assertTrue(Tag.isKnownTag(tagName1));
 Assert.assertFalse(Tag.isKnownTag(tagName2));
 }

 @Test
 public void testIsEmptyTag() {
 String tagName1 = "img";
 String tagName2 = "input";

 Tag tagImg = Tag.valueOf(tagName1);
 Tag tagInput = Tag.valueOf(tagName2);

 Assert.assertFalse(tagImg.isEmpty());
 Assert.assertFalse(tagInput.isEmpty());

 Tag emptyImg = new Tag(tagName1).setEmpty();
 Tag emptyInput = new Tag(tagName2).setEmpty();

 Assert.assertTrue(emptyImg.isEmpty());
 Assert.assertTrue(emptyInput.isEmpty());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName = "pre";

 Tag tag = Tag.valueOf(tagName);

 Assert.assertEquals(tagName, tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertTrue(tag.preserveWhitespace());
 }
}