package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag IMG = Tag.valueOf("img");

 @Test
 public void testTagCreation() {
 Tag tag = new Tag("testTag");
 assertEquals("testtag", tag.getName());
 }

 @Test
 public void testTagValueOf() {
 assertEquals(HTML, Tag.valueOf("html"));
 assertEquals(HEAD, Tag.valueOf("head"));
 assertEquals(BODY, Tag.valueOf("body"));
 assertEquals(DIV, Tag.valueOf("div"));
 assertEquals(P, Tag.valueOf("p"));
 assertEquals(IMG, Tag.valueOf("img"));
 assertEquals(P, Tag.valueOf("P")); // case insensitive
 }

 @Test
 public void testTagIsBlock() {
 assertTrue(HTML.isBlock());
 assertTrue(HEAD.isBlock());
 assertTrue(BODY.isBlock());
 assertTrue(DIV.isBlock());
 assertTrue(P.isBlock());
 assertFalse(IMG.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 assertTrue(HTML.formatAsBlock());
 assertTrue(HEAD.formatAsBlock());
 assertTrue(BODY.formatAsBlock());
 assertTrue(DIV.formatAsBlock());
 assertTrue(P.formatAsBlock());
 assertFalse(IMG.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 assertTrue(HTML.canContainBlock());
 assertTrue(HEAD.canContainBlock());
 assertTrue(BODY.canContainBlock());
 assertTrue(DIV.canContainBlock());
 assertFalse(P.canContainBlock());
 assertFalse(IMG.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 assertFalse(HTML.isEmpty());
 assertFalse(HEAD.isEmpty());
 assertFalse(BODY.isEmpty());
 assertFalse(DIV.isEmpty());
 assertTrue(IMG.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 assertFalse(HTML.isSelfClosing());
 assertFalse(HEAD.isSelfClosing());
 assertFalse(BODY.isSelfClosing());
 assertFalse(DIV.isSelfClosing());
 assertFalse(P.isSelfClosing());
 assertTrue(IMG.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("head"));
 assertTrue(Tag.isKnownTag("body"));
 assertTrue(Tag.isKnownTag("div"));
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("img"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 assertFalse(HTML.preserveWhitespace());
 assertFalse(HEAD.preserveWhitespace());
 assertFalse(BODY.preserveWhitespace());
 assertFalse(DIV.preserveWhitespace());
 assertFalse(P.preserveWhitespace());
 assertTrue(IMG.preserveWhitespace());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("testTag1");
 Tag tag2 = new Tag("testTag1");
 Tag tag3 = new Tag("testTag2");

 assertEquals(tag1, tag1);
 assertEquals(tag1, tag1);
 assertNotEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }
}