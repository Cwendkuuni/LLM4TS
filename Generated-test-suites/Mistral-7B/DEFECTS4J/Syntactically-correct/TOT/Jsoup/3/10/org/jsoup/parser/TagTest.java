package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagCreationAndProperties() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag span = Tag.valueOf("SPAN");
 Tag img = Tag.valueOf("IMG");

 assertEquals("HTML", html.getName());
 assertEquals("BODY", body.getName());
 assertEquals("P", p.getName());
 assertEquals("SPAN", span.getName());
 assertEquals("IMG", img.getName());

 assertTrue(html.isBlock());
 assertTrue(body.isBlock());
 assertTrue(p.isInline());
 assertTrue(span.isInline());
 assertTrue(img.isInline());

 assertTrue(p.canContainInline());
 assertFalse(p.canContainBlock());
 assertFalse(p.isEmpty());
 assertFalse(p.preserveWhitespace());

 assertTrue(body.canContainBlock());
 assertTrue(body.canContainInline());
 assertFalse(body.isEmpty());
 assertFalse(body.preserveWhitespace());

 assertTrue(img.isEmpty());
 assertTrue(img.preserveWhitespace());
 assertFalse(img.canContainBlock());
 assertFalse(img.canContainInline());
 }

 @Test
 public void testCanContain() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");
 Tag h1 = Tag.valueOf("H1");

 assertTrue(body.canContain(p));
 assertTrue(body.canContain(div));
 assertTrue(body.canContain(h1));

 assertFalse(p.canContain(p));
 assertFalse(p.canContain(h1));
 assertFalse(h1.canContain(p));
 }

 @Test
 public void testCanContainWithInvalidParentChild() {
 Tag p = Tag.valueOf("P");
 Tag h1 = Tag.valueOf("H1");

 assertFalse(p.canContain(h1));
 assertFalse(h1.canContain(p));
 }

 @Test
 public void testCanContainWithEmptyTag() {
 Tag p = Tag.valueOf("P");
 Tag img = Tag.valueOf("IMG");

 assertTrue(p.canContain(img));
 }

 @Test
 public void testCanContainWithDataOnlyTag() {
 Tag p = Tag.valueOf("P");
 Tag script = Tag.valueOf("SCRIPT");

 assertFalse(p.canContain(script));
 }

 @Test
 public void testCanContainWithBlockAndBlock() {
 Tag div = Tag.valueOf("DIV");
 Tag p = Tag.valueOf("P");

 assertTrue(div.canContain(p));
 }

 @Test
 public void testCanContainWithBlockAndInline() {
 Tag div = Tag.valueOf("DIV");
 Tag p = Tag.valueOf("P");
 Tag span = Tag.valueOf("SPAN");

 assertTrue(div.canContain(p));
 assertTrue(div.canContain(span));
 }

 @Test
 public void testCanContainWithInlineAndInline() {
 Tag p = Tag.valueOf("P");
 Tag span = Tag.valueOf("SPAN");

 assertTrue(p.canContain(span));
 }

 @Test
 public void testCanContainWithEmptyParent() {
 Tag body = Tag.valueOf("BODY");
 Tag img = Tag.valueOf("IMG");

 assertTrue(body.canContain(img));
 }

 @Test
 public void testCanContainWithHeadTag() {
 Tag head = Tag.valueOf("HEAD");
 Tag script = Tag.valueOf("SCRIPT");

 assertFalse(head.canContain(script));
 }

 @Test
 public void testCanContainWithDTandDD() {
 Tag dl = Tag.valueOf("DL");
 Tag dt = Tag.valueOf("DT");
 Tag dd = Tag.valueOf("DD");

 assertTrue(dl.canContain(dt));
 assertTrue(dl.canContain(dd));
 assertFalse(dt.canContain(dd));
 assertFalse(dd.canContain(dt));
 }

 @Test
 public void testCanContainWithLiTag() {
 Tag ul = Tag.valueOf("UL");
 Tag li = Tag.valueOf("LI");

 assertTrue(ul.canContain(li));
 }
}