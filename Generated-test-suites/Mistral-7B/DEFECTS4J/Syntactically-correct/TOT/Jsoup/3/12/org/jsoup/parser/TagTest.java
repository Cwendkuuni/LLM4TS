package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagUnknown = Tag.valueOf("unknownTag");

 assertEquals("p", tagP.getName());
 assertEquals("div", tagDiv.getName());
 assertNotSame(tagP, tagDiv);
 assertTrue(tagP.equals(tagP));
 assertTrue(tagP.equals(tagP.valueOf("p")));
 assertFalse(tagP.equals(tagDiv));
 assertFalse(tagP.equals(tagUnknown));
 }

 @Test
 public void testTagCanContain() {
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");
 Tag h1 = Tag.valueOf("H1");
 Tag script = Tag.valueOf("SCRIPT");

 assertTrue(body.canContain(p));
 assertTrue(body.canContain(div));
 assertTrue(body.canContain(h1));
 assertTrue(body.canContain(script));

 // head can only contain a few. if more than head in here, modify to have a list of valids
 // TODO: (could solve this with walk for ancestor)
 assertFalse(body.canContain(Tag.valueOf("HEAD")));

 assertFalse(p.canContain(p));
 assertFalse(p.canContain(div));
 assertFalse(p.canContain(h1));
 assertFalse(p.canContain(script));

 assertTrue(div.canContain(p));
 assertTrue(div.canContain(h1));
 assertFalse(div.canContain(div));
 assertFalse(div.canContain(script));

 assertTrue(h1.canContain(p));
 assertFalse(h1.canContain(h1));
 assertFalse(h1.canContain(div));
 assertFalse(h1.canContain(script));

 assertFalse(script.canContain(p));
 assertFalse(script.canContain(h1));
 assertFalse(script.canContain(div));
 assertFalse(script.canContain(script));
 }

 @Test
 public void testTagIsBlock() {
 Tag p = Tag.valueOf("P");
 Tag h1 = Tag.valueOf("H1");
 Tag input = Tag.valueOf("INPUT");

 assertTrue(p.isBlock());
 assertTrue(h1.isBlock());
 assertFalse(input.isBlock());
 }

 @Test
 public void testTagIsData() {
 Tag p = Tag.valueOf("P");
 Tag script = Tag.valueOf("SCRIPT");

 assertFalse(p.isData());
 assertTrue(script.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag img = Tag.valueOf("IMG");
 Tag p = Tag.valueOf("P");
 Tag br = Tag.valueOf("BR");

 assertFalse(img.isEmpty());
 assertFalse(p.isEmpty());
 assertTrue(br.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag pre = Tag.valueOf("PRE");
 Tag textarea = Tag.valueOf("TEXTAREA");

 assertFalse(pre.preserveWhitespace());
 assertTrue(textarea.preserveWhitespace());
 }

 @Test
 public void testTagGetImplicitParent() {
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");
 Tag body = Tag.valueOf("BODY");

 assertEquals(p.getImplicitParent(), body);
 assertEquals(div.getImplicitParent(), body);
 }

 @Test
 public void testTagIsValidParent() {
 Tag body = Tag.valueOf("BODY");
 Tag div = Tag.valueOf("DIV");
 Tag p = Tag.valueOf("P");

 assertTrue(body.isValidParent(p));
 assertTrue(p.isValidParent(p));
 assertTrue(p.isValidParent(div));
 assertFalse(div.isValidParent(body));
 assertFalse(body.isValidParent(div));
 }

 @Test
 public void testTagEqualsAndHashcode() {
 Tag p1 = Tag.valueOf("P");
 Tag p2 = Tag.valueOf("P");
 Tag div1 = Tag.valueOf("DIV");
 Tag div2 = Tag.valueOf("DIV");

 assertEquals(p1, p2);
 assertEquals(p1.hashCode(), p2.hashCode());

 assertNotEquals(p1, div1);
 assertNotEquals(p1.hashCode(), div1.hashCode());

 assertEquals(div1, div2);
 assertEquals(div1.hashCode(), div2.hashCode());

 assertNotEquals(p1, div1);
 assertNotEquals(p1.hashCode(), div1.hashCode());

 assertNotEquals(p1, null);
 assertNotEquals(p1.hashCode(), Integer.MIN_VALUE);
 }

 @Test
 public void testToString() {
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");

 assertEquals("p", p.toString());
 assertEquals("DIV", div.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testTagWithInvalidAncestors() {
 Tag parent = Tag.valueOf("P");
 Tag child = new Tag("CHILD");
 child.setAncestor(parent.tagName, "INVALID");
 }

 @Test
 public void testTagWithMissingAncestors() {
 Tag child = Tag.valueOf("CHILD");
 child.setAncestor(new String[0]);

 assertTrue(child.isValidParent(Tag.valueOf("BODY")));
 assertTrue(child.isValidParent(Tag.valueOf("HTML")));
 }
}