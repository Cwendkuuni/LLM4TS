package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = new Tag("div");
 Tag unknownTag = new Tag("unknown");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertTrue(pTag.isInline());
 Assert.assertTrue(pTag.isData());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());

 Assert.assertEquals("div", divTag.getName());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());

 Assert.assertFalse(unknownTag.isKnownTag());
 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.formatAsBlock());
 Assert.assertFalse(unknownTag.canContainBlock());
 Assert.assertFalse(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 }

 @Test
 public void testTagEquality() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag unknown = new Tag("unknown");

 Assert.assertEquals(p1, p2);
 Assert.assertNotEquals(p1, div);
 Assert.assertNotEquals(p1, unknown);
 Assert.assertFalse(p1.equals("not a tag"));
 }

 @Test
 public void testTagHashcode() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag empty = Tag.valueOf("input");
 empty.setEmpty(true);

 Assert.assertNotEquals(p.hashCode(), div.hashCode());
 Assert.assertNotEquals(p.hashCode(), empty.hashCode());
 }

 @Test
 public void testTagMethods() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag input = Tag.valueOf("input");
 input.setEmpty(true);

 Assert.assertTrue(p.isBlock());
 Assert.assertTrue(p.formatAsBlock());
 Assert.assertTrue(p.canContainBlock());
 Assert.assertTrue(p.canContainInline());
 Assert.assertFalse(p.isEmpty());
 Assert.assertFalse(p.isSelfClosing());

 Assert.assertFalse(p.isBlock());
 Assert.assertFalse(div.formatAsBlock());
 Assert.assertTrue(div.canContainBlock());
 Assert.assertTrue(div.canContainInline());
 Assert.assertFalse(div.isEmpty());
 Assert.assertFalse(div.isSelfClosing());

 Assert.assertFalse(input.isBlock());
 Assert.assertFalse(input.formatAsBlock());
 Assert.assertFalse(input.canContainBlock());
 Assert.assertFalse(input.canContainInline());
 Assert.assertTrue(input.isEmpty());
 Assert.assertFalse(input.isSelfClosing());

 Assert.assertFalse(p.isKnownTag());
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagValueOf() {
 Map<String, Tag> tagMap = new HashMap<String, Tag>();

 Tag p = Tag.valueOf("p");
 Tag div = new Tag("div");
 tagMap.put("p", p);
 tagMap.put("div", div);

 Assert.assertEquals(p, Tag.valueOf("p"));
 Assert.assertEquals(div, Tag.valueOf("div"));
 Assert.assertEquals(p, Tag.valueOf("P")); // case insensitive
 Assert.assertNull(Tag.valueOf(null));
 Assert.assertNull(Tag.valueOf(""));
 }

 @Test
 public void testTagRegistration() {
 Tag p = Tag.valueOf("p");
 Tag div = new Tag("div");
 Tag input = new Tag("input");
 input.setEmpty(true);

 Assert.assertFalse(p.isKnownTag());
 Assert.assertFalse(div.isKnownTag());
 Assert.assertFalse(input.isKnownTag());
 Tag.register(p);
 Tag.register(div);
 Tag.register(input);
 Assert.assertTrue(p.isKnownTag());
 Assert.assertTrue(div.isKnownTag());
 Assert.assertTrue(input.isKnownTag());
 }
}