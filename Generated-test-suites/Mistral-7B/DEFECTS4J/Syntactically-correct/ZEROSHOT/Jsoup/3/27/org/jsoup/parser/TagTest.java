package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.getName());
 assertFalse(tag.isBlock());
 tag.setBlock(true);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testCanContain() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 Tag imgTag = new Tag("img");
 Tag h1Tag = new Tag("h1");
 Tag headTag = new Tag("head");
 Tag bodyTag = new Tag("body");

 // p can contain div
 assertTrue(pTag.canContain(divTag));

 // p cannot contain p
 assertFalse(pTag.canContain(pTag));

 // p can contain img
 assertTrue(pTag.canContain(imgTag));

 // h1 cannot contain p (heading can't contain block level elements)
 assertFalse(h1Tag.canContain(pTag));

 // img cannot contain body
 assertFalse(imgTag.canContain(bodyTag));

 // head cannot contain body
 assertFalse(headTag.canContain(bodyTag));
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag pTag1 = new Tag("p");
 Tag pTag2 = new Tag("p");
 Tag divTag = new Tag("div");

 assertEquals(pTag1, pTag1);
 assertEquals(pTag1, pTag2);
 assertNotSame(pTag1, divTag);

 int hashCode1 = pTag1.hashCode();
 int hashCode2 = pTag2.hashCode();
 int hashCode3 = divTag.hashCode();

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testIsData() {
 Tag pTag = new Tag("p");
 Tag scriptTag = new Tag("script");

 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag pTag = new Tag("p");
 Tag imgTag = new Tag("img");

 assertFalse(pTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = new Tag("pre");
 Tag textareaTag = new Tag("textarea");

 assertFalse(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 List<Tag> ancestors = new ArrayList<>();
 ancestors.add(divTag);
 pTag.setAncestors(ancestors);

 assertEquals(divTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 Tag divTag2 = new Tag("div");
 List<Tag> divTagAncestors = new ArrayList<>();
 divTagAncestors.add(pTag);
 divTag2.setAncestors(divTagAncestors);

 assertTrue(pTag.isValidParent(divTag));
 assertFalse(pTag.isValidParent(divTag2));
 }

 @Test
 public void testToString() {
 Tag pTag = new Tag("p");
 assertEquals("p", pTag.toString());
 }

 @Test
 public void testRegisterTags() {
 // Pre-defined tags (P, DIV etc) will be ==, but unknown tags are not registered and will only .equals().
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 Tag unknownTag = new Tag("unknown");

 assertEquals(pTag, Tag.valueOf("p"));
 assertEquals(divTag, Tag.valueOf("div"));
 assertNotSame(unknownTag, Tag.valueOf("unknown"));
 }
}