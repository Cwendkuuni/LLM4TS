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

 Tag unknownTag = Tag.valueOf("UnknownTag");
 assertNotEquals("p", unknownTag.getName());
 assertNotNull(unknownTag);

 Tag predefinedPTag = Tag.valueOf("p");
 Tag newlyCreatedPTag = new Tag("p");

 assertEquals(predefinedPTag, newlyCreatedPTag); // comparison by equals()
 assertSame(predefinedPTag, newlyCreatedPTag); // comparison by reference
 }

 @Test
 public void testTagProperties() {
 Tag pTag = Tag.valueOf("p");

 assertTrue(pTag.isBlock());
 assertTrue(pTag.formatAsBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(pTag.isEmpty());
 assertFalse(pTag.isSelfClosing());
 assertFalse(pTag.preserveWhitespace());
 assertFalse(pTag.isFormListed());
 assertFalse(pTag.isFormSubmittable());

 pTag.setSelfClosing();
 assertFalse(pTag.isEmpty());
 assertTrue(pTag.isSelfClosing());
 }

 @Test
 public void testUnknownTag() {
 ParseSettings settings = new ParseSettings();

 Tag unknownTag = Tag.valueOf("UnknownTag", settings);
 assertFalse(unknownTag.isBlock());
 assertFalse(unknownTag.formatAsBlock());
 assertFalse(unknownTag.canContainInline());
 assertFalse(unknownTag.isEmpty());
 assertFalse(unknownTag.isSelfClosing());
 assertFalse(unknownTag.preserveWhitespace());
 assertFalse(unknownTag.isFormListed());
 assertFalse(unknownTag.isFormSubmittable());
 }

 @Test
 public void testTagCases() {
 Tag pTag = Tag.valueOf("P");
 Tag pTag2 = Tag.valueOf("p");

 assertEquals("P", pTag.getName());
 assertEquals("p", pTag2.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("UnknownTag"));
 }

 @Test
 public void testTagInitialization() {
 Tag tag = new Tag("unknownTag");
 assertTrue(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertFalse(tag.isFormListed());
 assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagHashCode() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");

 assertEquals(pTag.hashCode(), pTag2.hashCode());
 }

 @Test
 public void testTagEquals() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag pTag3 = Tag.valueOf("div");
 Tag pTag4 = new Tag("p");

 assertEquals(pTag, pTag);
 assertEquals(pTag, pTag2);
 assertNotEquals(pTag, pTag3);
 assertNotEquals(pTag, pTag4);
 }
}