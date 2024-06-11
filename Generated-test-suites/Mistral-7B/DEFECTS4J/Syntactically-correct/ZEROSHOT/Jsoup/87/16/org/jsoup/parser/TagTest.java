package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME = "testTag";

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Assert.assertNotNull(tag);
 Assert.assertEquals(TAG_NAME, tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setBlock(false);
 Assert.assertFalse(tag.isBlock());
 tag.setBlock(true);
 Assert.assertTrue(tag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setFormatAsBlock(false);
 Assert.assertFalse(tag.formatAsBlock());
 tag.setFormatAsBlock(true);
 Assert.assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setCanContainInline(false);
 Assert.assertFalse(tag.canContainInline());
 tag.setCanContainInline(true);
 Assert.assertTrue(tag.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setEmpty(true);
 Assert.assertTrue(tag.isEmpty());
 tag.setEmpty(false);
 Assert.assertFalse(tag.isEmpty());
 }

 @Test
 public void testTagSelfClosing() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setSelfClosing(true);
 Assert.assertTrue(tag.isSelfClosing());
 tag.setSelfClosing(false);
 Assert.assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setPreserveWhitespace(true);
 Assert.assertTrue(tag.preserveWhitespace());
 tag.setPreserveWhitespace(false);
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagFormList() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setFormList(true);
 Assert.assertTrue(tag.isFormListed());
 tag.setFormList(false);
 Assert.assertFalse(tag.isFormListed());
 }

 @Test
 public void testTagFormSubmit() {
 Tag tag = Tag.valueOf(TAG_NAME);
 tag.setFormSubmit(true);
 Assert.assertTrue(tag.isFormSubmittable());
 tag.setFormSubmit(false);
 Assert.assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Assert.assertFalse(Tag.isKnownTag(TAG_NAME));
 Tag knownTag = Tag.valueOf("p");
 Assert.assertTrue(Tag.isKnownTag(knownTag.getName()));
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf(TAG_NAME);
 Tag tag2 = Tag.valueOf(TAG_NAME);
 Tag tag3 = Tag.valueOf("p");
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = Tag.valueOf(TAG_NAME);
 Tag tag2 = Tag.valueOf(TAG_NAME);
 Tag tag3 = Tag.valueOf("p");
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = Tag.valueOf(TAG_NAME);
 Assert.assertEquals(TAG_NAME, tag.toString());
 }

 @Test
 public void testBlockTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.blockTags) {
 Tag tag = tags.get(tagName);
 Assert.assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.inlineTags) {
 Tag tag = tags.get(tagName);
 Assert.assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testEmptyTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.emptyTags) {
 Tag tag = tags.get(tagName);
 Assert.assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testFormatAsInlineTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = tags.get(tagName);
 Assert.assertFalse(tag.formatAsBlock());
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = tags.get(tagName);
 Assert.assertTrue(tag.preserveWhitespace());
 }
 }

 @Test
 public void testFormListedTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.formListedTags) {
 Tag tag = tags.get(tagName);
 Assert.assertTrue(tag.isFormListed());
 }
 }

 @Test
 public void testFormSubmitTags() {
 Map<String, Tag> tags = new HashMap<>();
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 tags.put(tagName, tag);
 }
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = tags.get(tagName);
 Assert.assertTrue(tag.isFormSubmittable());
 }
 }
}