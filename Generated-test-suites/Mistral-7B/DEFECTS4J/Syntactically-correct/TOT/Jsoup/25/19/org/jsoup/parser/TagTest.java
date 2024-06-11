import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValues() {
 // Test getting tag values for known tags
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());

 Assert.assertEquals("div", divTag.getName());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 Assert.assertFalse(divTag.preserveWhitespace());

 // Test getting tag values for unknown tag
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertEquals("unknown", unknownTag.getName());
 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.formatAsBlock());
 Assert.assertTrue(unknownTag.canContainBlock());
 Assert.assertTrue(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testTagMethods() {
 // Test isBlock method
 Tag pTag = Tag.valueOf("p");
 Tag inputTag = Tag.valueOf("input");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(inputTag.isBlock());

 // Test formatAsBlock method
 pTag.setFormatAsBlock(false);
 Assert.assertFalse(pTag.formatAsBlock());

 inputTag.setFormatAsBlock(true);
 Assert.assertTrue(inputTag.formatAsBlock());

 // Test canContainBlock method
 pTag.setCanContainBlock(false);
 Assert.assertFalse(pTag.canContainBlock());

 inputTag.setCanContainBlock(true);
 Assert.assertTrue(inputTag.canContainBlock());

 // Test isInline method
 Assert.assertFalse(pTag.isInline());
 Assert.assertTrue(inputTag.isInline());

 // Test isData method
 pTag.setEmpty(true);
 Assert.assertFalse(pTag.isData());

 inputTag.setEmpty(true);
 inputTag.setCanContainInline(false);
 Assert.assertTrue(inputTag.isData());

 // Test isEmpty method
 pTag.setEmpty(false);
 Assert.assertFalse(pTag.isEmpty());

 inputTag.setEmpty(true);
 Assert.assertTrue(inputTag.isEmpty());

 // Test isSelfClosing method
 pTag.setEmpty(false);
 Assert.assertFalse(pTag.isSelfClosing());

 pTag.setSelfClosing(true);
 Assert.assertTrue(pTag.isSelfClosing());

 // Test isKnownTag method
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknown"));
 }
}