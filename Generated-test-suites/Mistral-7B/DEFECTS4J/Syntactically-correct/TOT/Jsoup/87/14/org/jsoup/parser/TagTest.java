package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // Test tag equality
 assertEquals(pTag, pTag);
 assertEquals(pTag, Tag.valueOf("p"));
 assertEquals(pTag, Tag.valueOf("P")); // case insensitive

 // Test tag inequality
 assertNotEquals(pTag, divTag);
 assertNotEquals(pTag, null);

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isKnownTag());
 assertTrue(unknownTag.equals(Tag.valueOf("unknown")));

 // Test known tag
 assertTrue(pTag.isKnownTag());
 assertTrue(pTag.isKnownTag("p")); // case insensitive

 // Test tag properties
 assertEquals("p", pTag.getName());
 assertTrue(pTag.isBlock());
 assertFalse(pTag.formatAsBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());
 assertFalse(pTag.preserveWhitespace());
 assertFalse(pTag.isFormListed());
 assertFalse(pTag.isFormSubmittable());
 }

 @Test
 public void testTagMethods() {
 // Test isBlock()
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());

 // Test formatAsBlock()
 pTag.setFormatAsBlock(false);
 assertFalse(pTag.formatAsBlock());
 assertFalse(divTag.formatAsBlock());

 // Test canContainInline()
 pTag.setCanContainInline(false);
 assertFalse(pTag.canContainInline());
 assertTrue(divTag.canContainInline());

 // Test empty()
 pTag.setEmpty(true);
 assertTrue(pTag.isEmpty());
 assertFalse(divTag.isEmpty());

 // Test selfClosing()
 pTag.setSelfClosing(true);
 assertTrue(pTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());

 // Test preserveWhitespace()
 pTag.setPreserveWhitespace(true);
 assertTrue(pTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());

 // Test formList()
 pTag.setFormList(true);
 assertTrue(pTag.isFormListed());
 assertFalse(divTag.isFormListed());

 // Test formSubmit()
 pTag.setFormSubmit(true);
 assertTrue(pTag.isFormSubmittable());
 assertFalse(divTag.isFormSubmittable());
 }

 @Test
 public void testTagConstants() {
 // Test blockTags
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 // Test inlineTags
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }

 // Test emptyTags
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }

 // Test formatAsInlineTags
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }

 // Test preserveWhitespaceTags
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 // Test formListedTags
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormListed());
 }

 // Test formSubmitTags
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormSubmittable());
 }
 }
}