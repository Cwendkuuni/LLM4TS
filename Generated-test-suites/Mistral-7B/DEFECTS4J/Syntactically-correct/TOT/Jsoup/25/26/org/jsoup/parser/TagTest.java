package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertFalse(pTag.isBlock()); // p is an inline tag by default
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertFalse(pTag.isData());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());

 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isData());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 Assert.assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertTrue(unknownTag.canContainBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isData());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testEmptyTags() {
 Tag metaTag = Tag.valueOf("meta");
 Tag imgTag = Tag.valueOf("img");

 Assert.assertFalse(metaTag.isBlock());
 Assert.assertFalse(metaTag.canContainBlock());
 Assert.assertFalse(metaTag.canContainInline());
 Assert.assertTrue(metaTag.isEmpty());
 Assert.assertFalse(metaTag.isSelfClosing());
 Assert.assertFalse(metaTag.preserveWhitespace());

 Assert.assertFalse(imgTag.isBlock());
 Assert.assertFalse(imgTag.canContainBlock());
 Assert.assertFalse(imgTag.canContainInline());
 Assert.assertTrue(imgTag.isEmpty());
 Assert.assertTrue(imgTag.isSelfClosing());
 Assert.assertFalse(imgTag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 Tag brTag = Tag.valueOf("br");
 Tag imgTag = Tag.valueOf("img");
 imgTag.setSelfClosing();

 Assert.assertFalse(brTag.isBlock());
 Assert.assertFalse(brTag.canContainBlock());
 Assert.assertFalse(brTag.canContainInline());
 Assert.assertFalse(brTag.isEmpty());
 Assert.assertTrue(brTag.isSelfClosing());
 Assert.assertFalse(brTag.preserveWhitespace());

 Assert.assertFalse(imgTag.isBlock());
 Assert.assertFalse(imgTag.canContainBlock());
 Assert.assertFalse(imgTag.canContainInline());
 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertTrue(imgTag.isSelfClosing());
 Assert.assertFalse(imgTag.preserveWhitespace());
 }

 @Test
 public void testKnownTags() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("div"));
 Assert.assertTrue(Tag.isKnownTag("meta"));
 Assert.assertTrue(Tag.isKnownTag("img"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Tag preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.isBlock());
 Assert.assertTrue(preTag.canContainBlock());
 Assert.assertTrue(preTag.canContainInline());
 Assert.assertFalse(preTag.isData());
 Assert.assertFalse(preTag.isEmpty());
 Assert.assertFalse(preTag.isSelfClosing());
 Assert.assertTrue(preTag.preserveWhitespace());
 }
}