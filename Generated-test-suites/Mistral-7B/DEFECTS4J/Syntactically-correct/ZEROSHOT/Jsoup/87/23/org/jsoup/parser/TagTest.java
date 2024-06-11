package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>(); // map of known tags

 @Test
 public void testTagCreationAndEquality() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 assertEquals("p", tag1.getName());
 assertEquals("p", tag2.getName());
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testTagValueOf() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 assertEquals("p", tag1.getName());
 assertEquals("p", tag2.getName());
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.canContainInline());
 assertTrue(divTag.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 assertFalse(imgTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag inputTag = Tag.valueOf("input");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(inputTag.isSelfClosing());
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag("unknown");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTypeSubmitTag = Tag.valueOf("input").setAttribute("type", "submit");

 assertTrue(inputTypeSubmitTag.isFormSubmittable());
 }
}