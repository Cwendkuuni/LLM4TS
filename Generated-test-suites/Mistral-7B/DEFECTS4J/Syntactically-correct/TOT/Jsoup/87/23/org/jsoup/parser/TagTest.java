package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag html = Tag.valueOf("html");
 Tag p = Tag.valueOf("p");
 Tag unknown = Tag.valueOf("unknown");

 Assert.assertEquals("html", html.getName());
 Assert.assertTrue(html.isBlock());
 Assert.assertTrue(html.formatAsBlock());
 // Other assertions for other properties as needed

 Assert.assertEquals("p", p.getName());
 Assert.assertFalse(p.isBlock());
 Assert.assertFalse(p.formatAsBlock());

 Assert.assertEquals("unknown", unknown.getName());
 Assert.assertFalse(unknown.isBlock());
 Assert.assertFalse(unknown.formatAsBlock());
 }

 @Test
 public void testTagNormalization() {
 Tag P = Tag.valueOf("P");
 Tag p = Tag.valueOf("p");

 Assert.assertEquals("P", P.getName());
 Assert.assertEquals("p", p.getName());
 }

 @Test
 public void testTagValueOfKnownTag() {
 Tag p = Tag.valueOf("p");
 Tag q = Tag.valueOf("p");

 Assert.assertSame(p, q);
 }

 @Test
 public void testTagValueOfUnknownTag() {
 Tag unknown1 = Tag.valueOf("unknown1");
 Tag unknown2 = Tag.valueOf("unknown2");

 Assert.assertNotSame(unknown1, unknown2);
 Assert.assertTrue(unknown1.equals(unknown2));
 }

 @Test
 public void testTagIsBlock() {
 Tag html = Tag.valueOf("html");
 Tag p = Tag.valueOf("p");
 Tag input = Tag.valueOf("input");

 Assert.assertTrue(html.isBlock());
 Assert.assertTrue(p.isBlock());
 Assert.assertFalse(input.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag p = Tag.valueOf("p");
 Tag input = Tag.valueOf("input");

 Assert.assertTrue(p.formatAsBlock());
 Assert.assertFalse(input.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");

 Assert.assertTrue(p.canContainBlock());
 Assert.assertTrue(div.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 Tag script = Tag.valueOf("script");
 Tag textarea = Tag.valueOf("textarea");

 Assert.assertTrue(script.isData());
 Assert.assertTrue(textarea.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag img = Tag.valueOf("img");
 Tag br = Tag.valueOf("br");

 Assert.assertFalse(img.isEmpty());
 Assert.assertTrue(br.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag img = Tag.valueOf("img");
 Tag input = Tag.valueOf("input");

 Assert.assertFalse(img.isSelfClosing());
 Assert.assertFalse(input.isSelfClosing());

 Tag imgSelfClosing = Tag.valueOf("img").setSelfClosing();
 Assert.assertTrue(imgSelfClosing.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag pre = Tag.valueOf("pre");
 Tag textarea = Tag.valueOf("textarea");

 Assert.assertTrue(pre.preserveWhitespace());
 Assert.assertTrue(textarea.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag button = Tag.valueOf("button");
 Tag input = Tag.valueOf("input");

 Assert.assertTrue(button.isFormListed());
 Assert.assertTrue(input.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag button = Tag.valueOf("button");
 Tag input = Tag.valueOf("input");

 Assert.assertFalse(button.isFormSubmittable());
 Assert.assertFalse(input.isFormSubmittable());

 Tag inputTypeSubmit = Tag.valueOf("input").setFormSubmit(true);
 Assert.assertTrue(inputTypeSubmit.isFormSubmittable());
 }
}