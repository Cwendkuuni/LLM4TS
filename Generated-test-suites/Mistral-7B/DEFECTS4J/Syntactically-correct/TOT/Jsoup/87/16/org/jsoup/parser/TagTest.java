package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("p", pTag.toString());
 assertEquals("P", PTag.getName());
 assertEquals("P", PTag.toString());
 assertTrue(pTag.isEqual(pTag));
 assertTrue(pTag.isEqual(PTag));
 assertFalse(pTag.isEqual(unknownTag));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(inputTag.formatAsBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(scriptTag.isData());
 assertTrue(textareaTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(brTag.isSelfClosing());
 assertTrue(inputTag.isSelfClosing());
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
 Tag plaintextTag = Tag.valueOf("plaintext");

 assertTrue(preTag.preserveWhitespace());
 assertFalse(plaintextTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(inputTag.isFormListed());
 assertTrue(textareaTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag buttonTag = Tag.valueOf("button");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormSubmittable());
 assertFalse(buttonTag.isFormSubmittable());
 }
}