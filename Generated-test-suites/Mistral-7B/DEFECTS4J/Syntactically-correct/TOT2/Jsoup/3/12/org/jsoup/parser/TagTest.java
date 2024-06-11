import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

// Test for getName() method
@Test
public void testTagName() {
Tag tag = new Tag("div");
assertEquals("div", tag.getName());
}

// Test for valueOf() method with pre-defined tag
@Test
public void testValueOfPreDefinedTag() {
Tag pTag = Tag.valueOf("p");
assertEquals("p", pTag.getName());
}

// Test for valueOf() method with unknown tag
@Test
public void testValueOfUnknownTag() {
Tag unknownTag = Tag.valueOf("abc");
assertTrue(unknownTag.isInline()); // Since unknown tag is not registered, it is treated as an inline tag
}

// Test for canContain() method with valid child
@Test
public void testCanContainValidChild() {
Tag parentTag = new Tag("div");
Tag childTag = new Tag("p");
assertTrue(parentTag.canContain(childTag));
}

// Test for canContain() method with invalid child
@Test
public void testCanContainInvalidChild() {
Tag parentTag = new Tag("div");
Tag childTag = new Tag("div");
assertFalse(parentTag.canContain(childTag));
}

// Test for isBlock() method
@Test
public void testIsBlock() {
Tag pTag = new Tag("p");
assertTrue(pTag.isBlock());
}

// Test for isInline() method
@Test
public void testIsInline() {
Tag pTag = new Tag("p");
assertFalse(pTag.isInline());
}

// Test for isData() method
@Test
public void testIsData() {
Tag scriptTag = new Tag("script").setEmpty();
assertTrue(scriptTag.isData());
}

// Test for isEmpty() method
@Test
public void testIsEmpty() {
Tag imgTag = new Tag("img").setEmpty();
assertTrue(imgTag.isEmpty());
}

// Test for preserveWhitespace() method
@Test
public void testPreserveWhitespace() {
Tag preTag = new Tag("pre").setPreserveWhitespace();
assertTrue(preTag.preserveWhitespace());
}

// Test for getImplicitParent() method
@Test
public void testGetImplicitParent() {
Tag divTag = new Tag("div");
Tag bodyTag = divTag.getImplicitParent();
assertEquals("body", bodyTag.getName());
}

// Test for isValidParent() method
@Test
public void testIsValidParent() {
Tag parentTag = new Tag("div");
Tag childTag = new Tag("p");
assertTrue(parentTag.isValidParent(childTag));
}

// Test for equals() method
@Test
public void testEquals() {
Tag tag1 = new Tag("p");
Tag tag2 = new Tag("p");
assertTrue(tag1.equals(tag2));
}

// Test for hashCode() method
@Test
public void testHashCode() {
Tag tag1 = new Tag("p");
Tag tag2 = new Tag("p");
assertEquals(tag1.hashCode(), tag2.hashCode());
}

// Test for toString() method
@Test
public void testToString() {
Tag tag = new Tag("div");
assertEquals("div", tag.toString());
}

// Test for canContain() method with head tag
@Test
public void testCanContainHeadTag() {
Tag parentTag = new Tag("body");
Tag childTag = new Tag("head");
assertFalse(parentTag.canContain(childTag));
}

// Test for canContain() method with dt and dd tags
@Test
public void testCanContainDtAndDdTags() {
Tag parentTag = new Tag("dl");
Tag childTag1 = new Tag("dt");
Tag childTag2 = new Tag("dd");
assertFalse(parentTag.canContain(childTag1));
assertFalse(parentTag.canContain(childTag2));
}

// Test for canContain() method with same tag
@Test
public void testCanContainSameTag() {
Tag parentTag = new Tag("div");
Tag childTag = new Tag("div");
assertFalse(parentTag.canContain(childTag));
}

// Test for canContain() method with empty tag
@Test
public void testCanContainEmptyTag() {
Tag parentTag = new Tag("div");
Tag childTag = new Tag("img").setEmpty();
assertTrue(parentTag.canContain(childTag));
}

// Test for canContain() method with block and inline tags
@Test
public void testCanContainBlockAndInlineTags() {
Tag parentTag = new Tag("div");
Tag childTag1 = new Tag("p");
Tag childTag2 = new Tag("span");
assertTrue(parentTag.canContain(childTag1));
assertTrue(parentTag.canContain(childTag2));
}
}