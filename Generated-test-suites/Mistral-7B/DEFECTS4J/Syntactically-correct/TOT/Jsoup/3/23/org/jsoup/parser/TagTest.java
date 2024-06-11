package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag img = Tag.valueOf("img");
 Tag script = Tag.valueOf("script");
 Tag head = Tag.valueOf("head");
 Tag title = Tag.valueOf("title");

 assertEquals("p", p.getName());
 assertEquals("div", div.getName());
 assertEquals("img", img.getName());
 assertEquals("script", script.getName());
 assertEquals("head", head.getName());
 assertEquals("title", title.getName());
 }

 @Test
 public void testTagProperties() {
 Tag p = Tag.valueOf("p");
 Tag h1 = Tag.valueOf("h1");

 assertTrue(p.isBlock());
 assertTrue(p.canContainBlock());
 assertTrue(p.canContainInline());
 assertFalse(p.optionalClosing);
 assertFalse(p.isEmpty());
 assertFalse(p.preserveWhitespace);

 assertTrue(h1.isBlock());
 assertTrue(h1.canContainBlock());
 assertTrue(h1.canContainInline());
 assertFalse(h1.optionalClosing);
 assertFalse(h1.isEmpty());
 assertFalse(h1.preserveWhitespace);

 assertEquals(new String[0], p.getAncestors().toArray(new String[0]));
 assertEquals("html", h1.getAncestors().get(0));
 }

 @Test
 public void testTagCanContain() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag h1 = Tag.valueOf("h1");
 Tag title = Tag.valueOf("title");
 Tag script = Tag.valueOf("script");

 assertTrue(p.canContain(div));
 assertTrue(p.canContain(h1));
 assertFalse(p.canContain(title));
 assertFalse(p.canContain(script));
 }

 @Test
 public void testTagContainDataOnly() {
 Tag p = Tag.valueOf("p");
 Tag script = Tag.valueOf("script");

 assertFalse(p.isData());
 assertTrue(script.isData());
 }

 @Test
 public void testTagEmpty() {
 Tag p = Tag.valueOf("p");
 Tag img = Tag.valueOf("img");

 assertFalse(p.isEmpty());
 assertTrue(img.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag p = Tag.valueOf("p");
 Tag pre = Tag.valueOf("pre");

 assertFalse(p.preserveWhitespace());
 assertTrue(pre.preserveWhitespace());
 }

 @Test
 public void testTagOptionalClosing() {
 Tag p = Tag.valueOf("p");
 Tag br = Tag.valueOf("br");
 Tag pbr = Tag.valueOf("p").setValue("p");
 Tag pbrbr = Tag.valueOf("p").setValue("p br");

 assertFalse(p.optionalClosing);
 assertFalse(br.optionalClosing);
 assertTrue(pbr.optionalClosing);
 assertTrue(pbrbr.optionalClosing);
 }

 @Test
 public void testTagEqualsAndHashcode() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");

 assertEquals(p1, p2);
 assertSame(p1.hashCode(), p2.hashCode());
 assertNotSame(p1, div);
 }

 @Test
 public void testTagInvalidParent() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag title = Tag.valueOf("title");

 assertFalse(p.isValidParent(div));
 assertFalse(p.isValidParent(title));
 }

 @Test
 public void testTagValidParent() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");
 Tag body = Tag.valueOf("body");

 assertTrue(p.isValidParent(div));
 assertTrue(p.isValidParent(body));
 }
}