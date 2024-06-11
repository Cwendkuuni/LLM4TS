package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag FORM = Tag.valueOf("form");
 private static final Tag INPUT = Tag.valueOf("input");

 @Test
 public void testTagValues() {
 // Test getting tag values
 assertEquals("html", HTML.getName());
 assertEquals("head", HEAD.getName());
 assertEquals("body", BODY.getName());
 assertEquals("p", P.getName());
 assertEquals("div", DIV.getName());
 assertEquals("form", FORM.getName());

 // Test getting tag normalised values
 assertEquals("HTML", HTML.normalize(), "HTML tag should be normalized to uppercase HTML");
 assertEquals("P", P.normalize());
 }

 @Test
 public void testBlockTags() {
 // Test if block tags are recognized
 assertTrue("HTML should be a block tag", HTML.isBlock());
 assertTrue("Head should be a block tag", HEAD.isBlock());
 assertTrue("Body should be a block tag", BODY.isBlock());
 assertTrue("P should be a block tag", P.isBlock());
 // Test if inline tags are not block tags
 assertFalse("Div should not be a block tag", DIV.isBlock());
 assertFalse("Input should not be a block tag", INPUT.isBlock());
 }

 @Test
 public void testFormatAsBlockTags() {
 // Test if formatAsBlock tags are recognized
 assertTrue("HTML should format as a block", HTML.formatAsBlock());
 assertTrue("Head should format as a block", HEAD.formatAsBlock());
 assertTrue("Body should format as a block", BODY.formatAsBlock());
 // Test if formatAsInline tags are not formatAsBlock
 assertFalse("P should not format as a block", P.formatAsBlock());
 }

 @Test
 public void testCanContainInlineTags() {
 // Test if canContainInline tags can contain inline tags
 assertTrue("Div can contain inline tags", DIV.canContainInline());
 assertFalse("P cannot contain inline tags", P.canContainInline());
 }

 @Test
 public void testEmptyTags() {
 // Test if empty tags are recognized
 assertTrue("Meta should be empty", Tag.valueOf("meta").isEmpty());
 assertTrue("Br should be empty", Tag.valueOf("br").isEmpty());
 // Test if non-empty tags are not empty
 assertFalse("P should not be empty", P.isEmpty());
 }

 @Test
 public void testSelfClosingTags() {
 // Test if selfClosing tags are recognized
 assertFalse("Div should not be selfClosing", DIV.isSelfClosing());
 assertTrue("Input should be selfClosing", INPUT.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // Test if known tags are recognized
 assertTrue("HTML is a known tag", Tag.isKnownTag("html"));
 assertTrue("P is a known tag", Tag.isKnownTag("p"));
 // Test if unknown tags are not known
 assertFalse("Unknown tag is not a known tag", Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTags() {
 // Test if preserveWhitespace tags are recognized
 assertTrue("Pre should preserve whitespace", P.preserveWhitespace());
 assertFalse("Div should not preserve whitespace", DIV.preserveWhitespace());
 }

 @Test
 public void testFormListedTags() {
 // Test if formListed tags are recognized
 assertTrue("Input is a formListed tag", INPUT.isFormListed());
 assertFalse("Div is not a formListed tag", DIV.isFormListed());
 }

 @Test
 public void testFormSubmittableTags() {
 // Test if formSubmittable tags are recognized
 assertTrue("Input is a formSubmittable tag", INPUT.isFormSubmittable());
 assertFalse("Div is not a formSubmittable tag", DIV.isFormSubmittable());
 }
}