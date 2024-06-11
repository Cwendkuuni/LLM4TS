package org.jsoup.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TagTest {

 private static final Tag DEFAULT_ANCESTOR = new Tag("BODY");

 @Parameters(name = "{index}: test case {0} with Tag '{1}'")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Test Tag instantiation
 {new Tag("testTag"), "testTag"},
 // Test Tag valueOf()
 {Tag.valueOf("testTag"), "testTag"},
 // Test Tag.tags map
 {Tag.tags.get("testTag"), "testTag"},
 // Test Tag.canContain()
 {new Tag("p"), new Tag("p")},
 {new Tag("p"), new Tag("a")},
 {new Tag("p"), new Tag("div")},
 {new Tag("div"), new Tag("p")},
 {new Tag("div"), new Tag("div")},
 {new Tag("head"), new Tag("script")},
 {new Tag("head"), new Tag("title")},
 {new Tag("dt"), new Tag("dd")},
 {new Tag("dd"), new Tag("dt")},
 // Test Tag.isBlock()
 {new Tag("p"), true},
 {new Tag("span"), false},
 // Test Tag.canContainBlock()
 {new Tag("p"), true},
 {new Tag("span"), false},
 // Test Tag.canContainInline()
 {new Tag("p"), true},
 {new Tag("span"), true},
 // Test Tag.optionalClosing()
 {new Tag("p"), false},
 {new Tag("p"), false},
 {new Tag("p"), new Tag("p")},
 {new Tag("p"), new Tag("p")},
 // Test Tag.empty()
 {new Tag("img"), false},
 {new Tag("input"), false},
 {new Tag("br"), true},
 // Test Tag.preserveWhitespace()
 {new Tag("pre"), true},
 {new Tag("script"), false},
 // Test Tag.toString()
 {new Tag("p"), "p"},
 {new Tag("div"), "div"},
 // Test Tag.getImplicitParent()
 {new Tag("div").setAncestor(new Tag("body")), new Tag("body")},
 {new Tag("div").setAncestor(new Tag("div"), new Tag("body")), new Tag("div")},
 // Test Tag.isValidParent()
 {new Tag("p"), new Tag("p")},
 {new Tag("div"), new Tag("p")},
 {new Tag("p"), new Tag("div")},
 {new Tag("div"), new Tag("div")},
 {new Tag("p"), new Tag("body")},
 {new Tag("body"), new Tag("p")},
 {new Tag("head"), new Tag("p")},
 {new Tag("p"), new Tag("head")},
 {new Tag("div"), new Tag("head")},
 {new Tag("head"), new Tag("div")},
 {new Tag("script"), new Tag("script")},
 {new Tag("script"), new Tag("input")},
 {new Tag("input"), new Tag("script")},
 {new Tag("input"), new Tag("input")},
 // Test Tag equals() and hashCode()
 {new Tag("testTag"), new Tag("testTag"), true, 31},
 {new Tag("testTag"), new Tag("TestTag"), false, 31},
 {new Tag("testTag"), null, false, 0},
 {new Tag("testTag"), "", false, 0},
 {new Tag("testTag"), new Tag("testTag"), false, 31},
 {new Tag("testTag1"), new Tag("testTag2"), false, 31},
 {new Tag("testTag1"), new Tag("testTag1"), true, 31}
 });
 }

 @Parameterized.Parameters(name = "{index}: test case {0} with Tag '{1}'")
 private Tag tag;
 private String tagName;

 @Test
 public void testTagName() {
 assertEquals(tagName, tag.getName());
 }

 @Test
 public void testIsBlock() {
 assertEquals(tag.isBlock(), tag.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertEquals(tag.canContainBlock(), tag.canContainBlock());
 }

 @Test
 public void testCanContainInline() {
 assertEquals(tag.canContainInline(), tag.canContainInline());
 }

 @Test
 public void testOptionalClosing() {
 assertEquals(tag.optionalClosing(), tag.optionalClosing());
 }

 @Test
 public void testEmpty() {
 assertEquals(tag.isEmpty(), tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 assertEquals(tag.preserveWhitespace(), tag.preserveWhitespace());
 }

 @Test
 public void testAncestors() {
 List<Tag> ancestors = tag.getAncestors();
 assertNotNull(ancestors);
 assertTrue(ancestors.isEmpty() || ancestors.get(0).equals(DEFAULT_ANCESTOR));
 }

 @Test
 public void testGetImplicitParent() {
 assertNotNull(tag.getImplicitParent());
 }

 @Test
 public void testGetValidParent() {
 assertFalse(tag.isValidParent(new Tag("testTag")));
 assertFalse(tag.isValidParent(new Tag("p")));
 assertTrue(tag.isValidParent(new Tag("div")));
 }

 @Test
 public void testEquals() {
 Tag otherTag = new Tag("testTag");
 assertEquals(tag, otherTag);
 }

 @Test
 public void testHashCode() {
 int hashCode1 = tag.hashCode();
 Tag otherTag = new Tag("testTag");
 int hashCode2 = otherTag.hashCode();
 assertEquals(hashCode1, hashCode2);
 }
}