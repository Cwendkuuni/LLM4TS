package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag scriptTag = Tag.valueOf("script");

 // Test known tags
 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertEquals("script", scriptTag.getName());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknownTag");
 Assert.assertFalse(unknownTag.isKnownTag());
 Assert.assertEquals("unknownTag", unknownTag.getName());

 // Test known tag equals
 Tag pTag2 = Tag.valueOf("p");
 Assert.assertEquals(pTag, pTag2);

 // Test unknown tag equals
 Tag unknownTag2 = Tag.valueOf("unknownTag");
 Assert.assertNotEquals(pTag, unknownTag2);
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");
 Tag divTag = Tag.valueOf("div");

 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertFalse(brTag.canContainBlock());
 Assert.assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertFalse(inputTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertFalse(imgTag.isSelfClosing());
 Assert.assertFalse(inputTag.isSelfClosing());

 Tag imgSelfClosingTag = imgTag.setSelfClosing();
 Assert.assertTrue(imgSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknownTag");

 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.preserveWhitespace());
 }
}