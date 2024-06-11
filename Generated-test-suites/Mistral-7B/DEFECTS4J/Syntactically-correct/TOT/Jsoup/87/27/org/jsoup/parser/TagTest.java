package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagValues() {
 // Testing getName() method
 Tag pTag = Tag.valueOf("p");
 Assert.assertEquals("p", pTag.getName());

 Tag divTag = Tag.valueOf("div");
 Assert.assertEquals("div", divTag.getName());

 // Testing isBlock() method
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(divTag.isBlock());

 // Testing formatAsBlock() method
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(divTag.formatAsBlock());

 // Testing isData() method
 Tag scriptTag = Tag.valueOf("script");
 Assert.assertTrue(scriptTag.isData());

 // Testing isEmpty() method
 Tag imgTag = Tag.valueOf("img");
 Assert.assertFalse(imgTag.isEmpty());

 // Testing isSelfClosing() method
 Tag brTag = Tag.valueOf("br");
 Assert.assertTrue(brTag.isSelfClosing());

 // Testing isKnownTag() method
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("div"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));

 // Testing preserveWhitespace() method
 Tag preTag = Tag.valueOf("pre");
 Assert.assertTrue(preTag.preserveWhitespace());

 // Testing isFormListed() method
 Tag inputTag = Tag.valueOf("input");
 Assert.assertTrue(inputTag.isFormListed());

 // Testing isFormSubmittable() method
 Tag buttonTag = Tag.valueOf("button");
 Assert.assertTrue(buttonTag.isFormSubmittable());
 }

 @Test
 public void testTagValueCaseInsensitive() {
 // Testing valueOf() method with case insensitive tag name
 Tag pTag = Tag.valueOf("p");
 Assert.assertEquals("p", pTag.getName());

 Tag PTag = Tag.valueOf("P");
 Assert.assertEquals("p", PTag.getName());
 }

 @Test
 public void testTagValueNormalization() {
 // Testing valueOf() method with normalized tag name
 Tag pTag = Tag.valueOf("p");
 Assert.assertEquals("p", pTag.getName());

 Tag P_tag = Tag.valueOf("P_tag");
 Assert.assertEquals("p", P_tag.getName());
 }

 @Test
 public void testTagValueUnknown() {
 // Testing valueOf() method with unknown tag name
 Tag unknownTag = Tag.valueOf("unknown");
 Assert.assertFalse(unknownTag.isKnownTag());
 Assert.assertEquals("unknown", unknownTag.getName());
 }

 @Test
 public void testTagValueUnknownCreateDefault() {
 // Testing valueOf() method with unknown tag name, creating a new generic tag
 Tag unknownTag = Tag.valueOf("unknown");
 Assert.assertFalse(unknownTag.isKnownTag());
 Assert.assertEquals("unknown", unknownTag.getName());
 Assert.assertFalse(unknownTag.isBlock());
 }
}