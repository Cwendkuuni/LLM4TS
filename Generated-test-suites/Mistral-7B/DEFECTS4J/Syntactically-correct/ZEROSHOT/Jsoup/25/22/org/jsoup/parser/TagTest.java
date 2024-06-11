package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "TestTag";

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Tag anotherTag = Tag.valueOf(TAG_NAME);

 assertEquals("Tags should be equal", tag, anotherTag);
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertTrue("p tag should be block", pTag.isBlock());
 assertTrue("br tag should not be block", !brTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 assertTrue("pre tag should format as block", preTag.formatAsBlock());
 assertTrue("p tag should format as block by default", pTag.formatAsBlock());

 Tag spanTag = Tag.valueOf("span");
 spanTag.formatAsBlock(false);

 assertFalse("span tag should not format as block by default", spanTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue("div tag can contain block tags", divTag.canContainBlock());
 assertTrue("p tag is a block tag", pTag.isBlock());
 assertTrue("p tag can be contained in a div tag", divTag.canContain(pTag));

 assertTrue("img tag is not a block tag", !imgTag.isBlock());
 assertFalse("img tag cannot contain block tags", imgTag.canContainBlock());
 assertFalse("img tag cannot be contained in a div tag", !divTag.canContain(imgTag));
 }

 @Test
 public void testTagCanContainInline() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue("p tag can contain inline tags", pTag.canContainInline());
 assertTrue("span tag is an inline tag", spanTag.isInline());
 assertTrue("span tag can be contained in a p tag", pTag.canContain(spanTag));
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");

 assertTrue("script tag is a data only tag", scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 assertFalse("img tag is not an empty tag", !imgTag.isEmpty());

 imgTag.empty = true;
 assertTrue("img tag is now an empty tag", imgTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse("img tag is not self closing by default", !imgTag.isSelfClosing());
 assertFalse("br tag is not self closing", !brTag.isSelfClosing());

 imgTag.selfClosing = true;
 assertTrue("img tag is now self closing", imgTag.isSelfClosing());

 brTag.empty = true;
 brTag.selfClosing = true;
 assertTrue("br tag is now self closing and empty", brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("UnknownTag");

 assertTrue("p tag is a known tag", Tag.isKnownTag(pTag.getName()));
 assertFalse("UnknownTag is not a known tag", Tag.isKnownTag(unknownTag.getName()));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue("pre tag should preserve whitespace", preTag.preserveWhitespace());
 }

 @Test
 public void testTagEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertEquals("Two p tags should be equal", pTag1, pTag2);
 assertNotEquals("p tag and span tag should not be equal", pTag1, spanTag);
 }

 @Test
 public void testTagHashcode() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 int pTagHashcode = pTag.hashCode();
 int pTag2Hashcode = pTag2.hashCode();

 assertEquals("Two equal p tags should have the same hashcode", pTagHashcode, pTag2Hashcode);
 int spanTagHashcode = spanTag.hashCode();

 assertNotEquals("p tag and span tag should have different hashcodes", pTagHashcode, spanTagHashcode);
 }
}