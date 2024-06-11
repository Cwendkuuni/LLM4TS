package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertTrue;

import java.util.HashMap; import java.util.Map;

/**
 * JUnit 4 tests for the Tag class.
 */
public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf("unknown");
 assertEquals("unknown", tag.getName());
 assertFalse(tag.isBlock());
 }

 @Test
 public void testKnownTagIsKnown() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");

 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 }

 @Test
 public void testUnknownTagIsNotKnown() {
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testGetters() {
 Tag pTag = new Tag("p");

 assertEquals("p", pTag.getName());
 assertTrue(pTag.isBlock());
 assertTrue(pTag.formatAsBlock());
 assertTrue(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 Tag imgTag = new Tag("img").setSelfClosing();

 assertFalse(imgTag.isEmpty());
 assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testEquals() {
 Tag pTag1 = new Tag("p");
 Tag pTag2 = new Tag("p");
 Tag divTag = new Tag("div");

 assertEquals(pTag1, pTag2);
 assertNotEquals(pTag1, divTag);
 }

 @Test
 public void testHashCode() {
 Tag pTag = new Tag("p");
 Tag pTag2 = new Tag("p");
 Tag divTag = new Tag("div");

 int pTagHashcode = pTag.hashCode();
 int pTag2Hashcode = pTag2.hashCode();
 int divTagHashcode = divTag.hashCode();

 Assert.assertEquals(pTagHashcode, pTag2Hashcode);
 Assert.assertNotEquals(pTagHashcode, divTagHashcode);
 }

 @Test
 public void testToString() {
 Tag pTag = new Tag("p");

 assertEquals("p", pTag.toString());
 }

 @Test
 public void testRegisteredTags() {
 Map<String, Tag> registeredTags = new HashMap<>();

 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 registeredTags.put(tagName, tag);
 }

 assertEquals(62, registeredTags.size());
 }
}