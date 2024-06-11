import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("H1");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("H1", h1Tag.getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pInDivTag = Tag.valueOf("p");

 Assert.assertTrue(pTag.canContain(pInDivTag));
 Assert.assertTrue(pTag.canContain(divTag));
 }

 @Test
 public void testInvalidParentChildRelationship() {
 Tag pTag = Tag.valueOf("p");
 Tag pTagChild = Tag.valueOf("p");

 Assert.assertFalse(pTag.canContain(pTagChild));
 }

 @Test
 public void testIsBlockAndCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.canContainBlock());

 Assert.assertTrue(pTag.canContain(divTag));
 Assert.assertFalse(divTag.canContain(pTag));
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("SCRIPT");

 Assert.assertFalse(scriptTag.isBlock());
 Assert.assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("IMG");

 Assert.assertFalse(imgTag.isEmpty());

 Tag brTag = Tag.valueOf("BR");

 Assert.assertTrue(brTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("PRE");

 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testIsValidParent() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 Assert.assertTrue(divTag.isValidParent(pTag));
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("H1");

 Assert.assertEquals(pTag1, pTag2);
 Assert.assertEquals(pTag1.hashCode(), pTag2.hashCode());
 Assert.assertNotEquals(pTag1, h1Tag);
 }
}