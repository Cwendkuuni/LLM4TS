package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag class.
 */
public class TagTest {

 private static final String TAG_NAME_P = "p";
 private static final String TAG_NAME_DIV = "div";
 private static final String TAG_NAME_IMG = "img";
 private static final String TAG_NAME_SPAN = "span";
 private static final String TAG_NAME_FORM = "form";
 private static final String TAG_NAME_INPUT = "input";
 private static final String TAG_NAME_BUTTON = "button";
 private static final String TAG_NAME_META = "meta";

 @Test
 public void testTagCreationAndEquality() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagP2 = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagSpan = Tag.valueOf(TAG_NAME_SPAN);
 Tag tagForm = Tag.valueOf(TAG_NAME_FORM);
 Tag tagInput = Tag.valueOf(TAG_NAME_INPUT);
 Tag tagButton = Tag.valueOf(TAG_NAME_BUTTON);
 Tag tagMeta = Tag.valueOf(TAG_NAME_META);

 Tag tagP3 = new Tag(TAG_NAME_P);

 Assert.assertEquals(tagP, tagP2);
 Assert.assertEquals(tagP.hashCode(), tagP2.hashCode());
 Assert.assertNotEquals(tagP, tagDiv);
 Assert.assertNotEquals(tagP.hashCode(), tagDiv.hashCode());
 Assert.assertNotEquals(tagP, tagImg);
 Assert.assertNotEquals(tagP.hashCode(), tagImg.hashCode());
 Assert.assertNotEquals(tagP, tagSpan);
 Assert.assertNotEquals(tagP.hashCode(), tagSpan.hashCode());
 Assert.assertNotEquals(tagP, tagForm);
 Assert.assertNotEquals(tagP.hashCode(), tagForm.hashCode());
 Assert.assertNotEquals(tagP, tagInput);
 Assert.assertNotEquals(tagP.hashCode(), tagInput.hashCode());
 Assert.assertNotEquals(tagP, tagButton);
 Assert.assertNotEquals(tagP.hashCode(), tagButton.hashCode());
 Assert.assertNotEquals(tagP, tagMeta);
 Assert.assertNotEquals(tagP.hashCode(), tagMeta.hashCode());

 Assert.assertEquals(tagP, tagP3);
 Assert.assertEquals(tagP.hashCode(), tagP3.hashCode());
 }

 @Test
 public void testIsBlockTag() {
 Assert.assertTrue(tagP.isBlock());
 Assert.assertTrue(tagDiv.isBlock());
 Assert.assertFalse(tagImg.isBlock());
 Assert.assertFalse(tagSpan.isBlock());
 Assert.assertTrue(tagForm.isBlock());
 }

 @Test
 public void testFormatAsBlockTag() {
 Assert.assertTrue(tagP.formatAsBlock());
 Assert.assertTrue(tagP.formatAsBlock());
 Assert.assertFalse(tagImg.formatAsBlock());
 Assert.assertFalse(tagSpan.formatAsBlock());
 Assert.assertFalse(tagForm.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTag() {
 Assert.assertTrue(tagP.canContainBlock());
 Assert.assertTrue(tagDiv.canContainBlock());
 Assert.assertFalse(tagImg.canContainBlock());
 Assert.assertFalse(tagSpan.canContainBlock());
 Assert.assertTrue(tagForm.canContainBlock());
 }

 @Test
 public void testIsEmptyTag() {
 Assert.assertFalse(tagP.isEmpty());
 Assert.assertFalse(tagDiv.isEmpty());
 Assert.assertTrue(tagImg.isEmpty());
 Assert.assertFalse(tagSpan.isEmpty());
 Assert.assertFalse(tagForm.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Assert.assertFalse(tagP.isSelfClosing());
 Assert.assertFalse(tagDiv.isSelfClosing());
 Assert.assertTrue(tagImg.isSelfClosing());
 Assert.assertFalse(tagSpan.isSelfClosing());
 Assert.assertFalse(tagForm.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_P));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_DIV));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_IMG));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_SPAN));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_FORM));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_INPUT));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_BUTTON));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_META));
 }

 @Test
 public void testIsFormListedTag() {
 Assert.assertFalse(tagP.isFormListed());
 Assert.assertFalse(tagDiv.isFormListed());
 Assert.assertFalse(tagImg.isFormListed());
 Assert.assertFalse(tagSpan.isFormListed());
 Assert.assertTrue(tagForm.isFormListed());
 Assert.assertTrue(tagInput.isFormListed());
 Assert.assertTrue(tagButton.isFormListed());
 }

 @Test
 public void testIsFormSubmittableTag() {
 Assert.assertFalse(tagP.isFormSubmittable());
 Assert.assertFalse(tagDiv.isFormSubmittable());
 Assert.assertFalse(tagImg.isFormSubmittable());
 Assert.assertFalse(tagSpan.isFormSubmittable());
 Assert.assertTrue(tagForm.isFormSubmittable());
 Assert.assertTrue(tagInput.isFormSubmittable());
 Assert.assertTrue(tagButton.isFormSubmittable());
 }
}