package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 Tag unknownTag = Tag.valueOf("unknownTag");
 assertFalse(unknownTag.isKnownTag());
 assertTrue(unknownTag.equals(Tag.valueOf("unknownTag")));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.isBlock());
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");

 assertTrue(pTag.formatAsBlock());
 assertFalse(scriptTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContain(pTag));
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(inputTag.isEmpty());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(pTag.isSelfClosing());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag scriptTag = Tag.valueOf("script");

 assertTrue(preTag.preserveWhitespace());
 assertFalse(scriptTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 Tag selectTag = Tag.valueOf("select");

 assertTrue(inputTag.isFormListed());
 assertTrue(selectTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(inputTag.isFormSubmittable());
 assertTrue(textareaTag.isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }
}