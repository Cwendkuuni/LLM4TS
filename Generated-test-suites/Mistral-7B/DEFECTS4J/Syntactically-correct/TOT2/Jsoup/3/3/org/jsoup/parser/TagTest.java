package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 tag = Tag.valueOf("Div");
 assertTrue(tag.isInline()); // Since it is not defined, it will be treated as an inline tag

 Tag unknownTag = Tag.valueOf("UnknownTag");
 assertFalse(unknownTag.isBlock()); // Unknown tags are not registered and will only .equals()
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");
 Tag h1Tag = Tag.valueOf("h1");
 Tag bodyTag = Tag.valueOf("body");

 // Block tag can contain other block tags
 assertTrue(pTag.canContain(divTag));
 assertTrue(pTag.canContain(pTag));
 assertTrue(bodyTag.canContain(pTag));
 // Inline tag cannot contain block tags
 assertFalse(divTag.canContain(pTag));
 // Inline tag can contain other inline tags
 assertTrue(divTag.canContain(imgTag));
 // Parent and child cannot contain each other directly
 assertFalse(pTag.canContain(pTag));
 // Head can contain specific tags
 List<String> headTags = Arrays.asList("base", "script", "noscript", "link", "meta", "title", "style", "object");
 for (String headTagName : headTags) {
 String headTag = Tag.valueOf(headTagName);
 assertTrue(bodyTag.canContain(headTag));
 }
 // dt and dd (in dl)
 Tag dtTag = Tag.valueOf("dt");
 Tag ddTag = Tag.valueOf("dd");
 assertFalse(dtTag.canContain(ddTag));
 assertFalse(ddTag.canContain(dtTag));
 // Empty tags cannot contain anything
 Tag emptyTag = Tag.valueOf("img");
 emptyTag.setEmpty();
 assertFalse(pTag.canContain(emptyTag));
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testIsInline() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");
 assertFalse(pTag.isInline());
 assertTrue(spanTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");
 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag emptyTag = Tag.valueOf("img");
 emptyTag.setEmpty();

 assertTrue(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 assertFalse(pTag.isEmpty());
 assertFalse(emptyTag.isBlock());
 assertFalse(emptyTag.isInline());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textAreaTag = Tag.valueOf("textarea");
 Tag scriptTag = Tag.valueOf("script");

 preTag.setPreserveWhitespace();
 textAreaTag.setPreserveWhitespace();
 scriptTag.setPreserveWhitespace();

 // Check preserveWhitespace is set correctly
 assertTrue(preTag.preserveWhitespace());
 assertTrue(textAreaTag.preserveWhitespace());
 assertTrue(scriptTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");

 // Check getImplicitParent returns expected parent for empty ancestor list
 assertEquals(bodyTag, pTag.getImplicitParent());
 // Check getImplicitParent returns expected parent for non-empty ancestor list
 List<String> divAncestors = Arrays.asList("body", "div");
 divTag.setAncestors(divAncestors);
 assertEquals(divTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 Tag unknownTag = Tag.valueOf("UnknownTag");

 // Check isValidParent returns true for valid parent
 assertTrue(pTag.isValidParent(bodyTag));
 // Check isValidParent returns false for invalid parent
 assertFalse(pTag.isValidParent(divTag));
 // Check isValidParent returns false for unknown tag
 assertFalse(pTag.isValidParent(unknownTag));
 }

 // Edge cases testing
 @Test
 public void testCanContainEdgeCases() {
 // canContain should return false for self reference
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.canContain(pTag));
 // canContain should return false for empty ancestor list
 Tag emptyTag = Tag.valueOf("img");
 emptyTag.setEmpty();
 emptyTag.setAncestors(Collections.emptyList());
 assertFalse(pTag.canContain(emptyTag));
 // canContain should return false for ancestor in descendant chain
 Tag liTag = Tag.valueOf("li");
 Tag ulTag = Tag.valueOf("ul");
 liTag.setAncestors(Arrays.asList("ul", "li"));
 assertFalse(ulTag.canContain(liTag));
 // canContain should return true for valid ancestor chain
 Tag pParentTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 pParentTag.setAncestors(Arrays.asList("body", "p"));
 assertTrue(pTag.canContain(pParentTag));
 }

 // Exception handling testing
 @Test(expected = NullPointerException.class)
 public void testCanContainWithNullTag() {
 Tag pTag = Tag.valueOf("p");
 Tag nullTag = null;
 assertFalse(pTag.canContain(nullTag));
 }
}