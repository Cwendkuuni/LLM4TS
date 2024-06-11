import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 // Test for Tag constructor
 @Test
 public void testTagConstructor() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.getName());
 }

 // Test for Tag.valueOf() method
 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag emptyTag = Tag.valueOf("img");
 assertEquals("p", pTag.getName());
 assertEquals("img", emptyTag.getName());
 }

 // Test for canContain() method: block tag containing another block tag
 @Test
 public void testCanContainBlockBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContain(divTag));
 }

 // Test for canContain() method: inline tag containing another inline tag
 @Test
 public void testCanContainInlineInline() {
 Tag spanTag = Tag.valueOf("span");
 Tag iTag = Tag.valueOf("i");
 assertTrue(spanTag.canContain(iTag));
 }

 // Test for canContain() method: block tag containing inline tag
 @Test
 public void testCanContainBlockInline() {
 Tag pTag = Tag.valueOf("p");
 Tag iTag = Tag.valueOf("i");
 assertTrue(pTag.canContain(iTag));
 }

 // Test for canContain() method: parent tag should not contain child tag
 @Test
 public void testCannotContainSelf() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertFalse(pTag.canContain(pTag2));
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isEmpty());
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());
 }

 // Test for isInline() method
 @Test
 public void testIsInline() {
 Tag spanTag = Tag.valueOf("span");
 assertTrue(spanTag.isInline());
 }

 // Test for equals() method
 @Test
 public void testEquals() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertEquals(p1, p1);
 assertEquals(p1, p2);
 assertNotEquals(p1, divTag);
 }

 // Test for hashCode() method
 @Test
 public void testHashCode() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 int hashCode1 = pTag.hashCode();
 int hashCode2 = pTag2.hashCode();
 int hashCode3 = divTag.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 // Test for toString() method
 @Test
 public void testToString() {
 Tag pTag = Tag.valueOf("p");
 assertEquals("p", pTag.toString());
 }

 // Test for getName() method
 @Test
 public void testgetName() {
 Tag pTag = Tag.valueOf("p");
 assertEquals("p", pTag.getName());
 }

 // Test for getImplicitParent() method
 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("p");
 Tag bodyTag = Tag.valueOf("body");
 pTag.setAncestor(bodyTag.tagName);
 Tag implicitParent = pTag.getImplicitParent();
 assertEquals(bodyTag, implicitParent);
 }

 // Test for isValidParent() method
 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 pTag.setAncestor(bodyTag.tagName);
 assertTrue(pTag.isValidParent(divTag));
 }

 // Test for canContain() method:head tag containing base tag
 @Test
 public void testHeadCanContainBase() {
 Tag headTag = Tag.valueOf("head");
 Tag baseTag = Tag.valueOf("base");
 assertTrue(headTag.canContain(baseTag));
 }

 // Test for canContain() method:dt tag containing dd tag
 @Test
 public void testDtCanContainDd() {
 Tag dtTag = Tag.valueOf("dt");
 Tag ddTag = Tag.valueOf("dd");
 assertTrue(dtTag.canContain(ddTag));
 }

 // Test for canContain() method:li tag containing li tag
 @Test
 public void testLiCanContainLi() {
 Tag ulTag = Tag.valueOf("ul");
 Tag liTag1 = Tag.valueOf("li");
 Tag liTag2 = Tag.valueOf("li");
 ulTag.setAncestor("li".equals("") ? null : "li");
 assertTrue(ulTag.canContain(liTag1));
 assertTrue(ulTag.canContain(liTag2));
 }

 // Test for canContain() method:form tag containing input tag
 @Test
 public void testFormCanContainInput() {
 Tag formTag = Tag.valueOf("form");
 Tag inputTag = Tag.valueOf("input");
 assertTrue(formTag.canContain(inputTag));
 }

 // Test for canContain() method:table tag containing tr tag
 @Test
 public void testTableCanContainTr() {
 Tag tableTag = Tag.valueOf("table");
 Tag trTag = Tag.valueOf("tr");
 assertTrue(tableTag.canContain(trTag));
 }

 // Test for canContain() method:table tag containing th tag
 @Test
 public void testTableCanContainTh() {
 Tag tableTag = Tag.valueOf("table");
 Tag thTag = Tag.valueOf("th");
 assertTrue(tableTag.canContain(thTag));
 }

 // Test for canContain() method:table tag containing td tag
 @Test
 public void testTableCanContainTd() {
 Tag tableTag = Tag.valueOf("table");
 Tag tdTag = Tag.valueOf("td");
 assertTrue(tableTag.canContain(tdTag));
 }

 // Test for getAncestors() method
 @Test
 public void testGetAncestors() {
 Tag pTag = Tag.valueOf("p");
 Tag bodyTag = Tag.valueOf("body");
 pTag.setAncestor(bodyTag.tagName);
 List<Tag> ancestors = pTag.getAncestors();
 List<String> expectedAncestors = Arrays.asList("body", "p");
 assertEquals(expectedAncestors, ancestors.stream().map(Tag::getName).collect(Collectors.toList()));
 }

 // Test for getAncestors() method: empty list when no ancestors
 @Test
 public void testGetAncestorsEmpty() {
 Tag pTag = Tag.valueOf("p");
 List<Tag> ancestors = pTag.getAncestors();
 assertTrue(ancestors.isEmpty());
 }
}