import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag script = Tag.valueOf("script");

        assertEquals("html", html.getName());
        assertEquals("body", body.getName());
        assertEquals("p", p.getName());
        assertEquals("div", div.getName());
        assertEquals("script", script.getName());

        // Test case for unknown tag
        Tag unknownTag = Tag.valueOf("unknownTag");
        assertTrue(unknownTag.isData());
    }

    @Test
    public void testCanContain() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag span = Tag.valueOf("span");
        Tag script = Tag.valueOf("script");

        // Test valid cases
        assertTrue(body.canContain(p));
        assertTrue(body.canContain(div));
        assertTrue(body.canContain(span));
        assertTrue(html.canContain(body));
        assertTrue(html.canContain(p));
        assertTrue(html.canContain(div));
        assertTrue(html.canContain(script));

        // Test invalid cases
        assertFalse(p.canContain(p));
        assertFalse(p.canContain(script));
        assertFalse(body.canContain(html));
        assertFalse(body.canContain(script));
        assertFalse(html.canContain(html));
        assertFalse(html.canContain(script));
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag span = Tag.valueOf("span");

        assertTrue(html.isBlock());
        assertTrue(body.isBlock());
        assertFalse(p.isBlock());
        assertFalse(div.isBlock());
        assertFalse(span.isBlock());
    }

    @Test
    public void testIsData() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag script = Tag.valueOf("script");

        assertFalse(html.isData());
        assertFalse(body.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag p = Tag.valueOf("p");
        Tag br = Tag.valueOf("br");

        assertFalse(img.isEmpty());
        assertFalse(p.isEmpty());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        Tag textarea = Tag.valueOf("textarea");
        Tag script = Tag.valueOf("script");

        assertFalse(pre.preserveWhitespace());
        assertFalse(textarea.preserveWhitespace());
        assertFalse(script.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertEquals(body, body.getImplicitParent());
        assertEquals(body, p.getImplicitParent());
        assertEquals(body, div.getImplicitParent());

        Tag divParent = Tag.valueOf("div");
        Tag pInDiv = Tag.valueOf("p");

        assertEquals(divParent, pInDiv.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertTrue(html.isValidParent(body));
        assertTrue(body.isValidParent(p));
        assertTrue(body.isValidParent(div));
        assertFalse(p.isValidParent(p));
        assertFalse(p.isValidParent(html));
        assertFalse(div.isValidParent(html));
    }
}