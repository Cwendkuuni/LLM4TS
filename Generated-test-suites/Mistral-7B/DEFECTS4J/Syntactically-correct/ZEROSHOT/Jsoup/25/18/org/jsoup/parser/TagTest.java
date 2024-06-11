package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = new Tag(TAG_NAME);
 Tag expected = new Tag(TAG_NAME);

 assertEquals(tag.getName(), expected.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf(TAG_NAME);

 assertEquals(tag.getName(), TAG_NAME);
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());

 Tag tagInline = new Tag("span");
 assertFalse(tagInline.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());

 Tag tagInline = new Tag("span");
 assertFalse(tagInline.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.canContainBlock());

 Tag tagInline = new Tag("span");
 assertFalse(tagInline.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag tagEmpty = new Tag("img");
 tagEmpty.isEmpty();
 assertTrue(tagEmpty.isEmpty());

 Tag tagNotEmpty = new Tag("p");
 assertFalse(tagNotEmpty.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tagSelfClosing = new Tag("img").setSelfClosing();
 assertTrue(tagSelfClosing.isSelfClosing());

 Tag tagNotSelfClosing = new Tag("p");
 assertFalse(tagNotSelfClosing.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tagPreserveWhitespace = new Tag("pre");
 assertTrue(tagPreserveWhitespace.preserveWhitespace());

 Tag tagNoPreserveWhitespace = new Tag("span");
 assertFalse(tagNoPreserveWhitespace.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Tag tag3 = new Tag("span");

 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Tag tag3 = new Tag("span");

 int hashCodeTag1 = tag1.hashCode();
 int hashCodeTag2 = tag2.hashCode();
 int hashCodeTag3 = tag3.hashCode();

 assertEquals(hashCodeTag1, hashCodeTag2);
 assertNotEquals(hashCodeTag1, hashCodeTag3);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.toString());
 }

 @Test
 public void testBlockTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.blockTags) {
 Tag tag = new Tag(tagName);
 tags.put(tagName, tag);
 }

 for (Map.Entry<String, Tag> entry : tags.entrySet()) {
 String tagName = entry.getKey();
 Tag tag = entry.getValue();
 assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.inlineTags) {
 Tag tag = new Tag(tagName);
 tag.isBlock = false;
 tag.canContainBlock = false;
 tag.formatAsBlock = false;
 tags.put(tagName, tag);
 }

 for (Map.Entry<String, Tag> entry : tags.entrySet()) {
 String tagName = entry.getKey();
 Tag tag = entry.getValue();
 assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testEmptyTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.emptyTags) {
 Tag tag = tags.get(tagName);
 Validate.notNull(tag);
 tag.canContainBlock = false;
 tag.canContainInline = false;
 tag.empty = true;
 }
 }

 @Test
 public void testFormatAsInlineTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = tags.get(tagName);
 Validate.notNull(tag);
 tag.formatAsBlock = false;
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = tags.get(tagName);
 Validate.notNull(tag);
 tag.preserveWhitespace = true;
 }
 }
}