package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        assertEquals(tag1.tagName, "p");
        assertEquals(tag1.isBlock(), true);
        assertEquals(tag1.canContainBlock(), true);
        assertEquals(tag1.canContainInline(), true);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testTagAncestors() {
        Tag html = new Tag("HTML").setAncestor(new String[0]);
        Tag head = new Tag("HEAD").setAncestor("HTML");
        Tag body = new Tag("BODY").setAncestor("HTML");

        Tag script = new Tag("SCRIPT").setAncestor("HEAD", "BODY").setContainDataOnly();
        Tag link = new Tag("LINK").setAncestor("HEAD", "BODY").setEmpty();

        Tag p = new Tag("P").setContainInlineOnly();
        Tag div = new Tag("DIV");

        List<Tag> htmlAncestors = html.getImplicitParents();
        List<Tag> headAncestors = head.getImplicitParents();
        List<Tag> bodyAncestors = body.getImplicitParents();
        List<Tag> scriptAncestors = script.getImplicitParents();
        List<Tag> linkAncestors = link.getImplicitParents();
        List<Tag> pAncestors = p.getImplicitParents();
        List<Tag> divAncestors = div.getImplicitParents();

        assertEquals(htmlAncestors.size(), 0);
        assertEquals(headAncestors.size(), 1);
        assertEquals(headAncestors.get(0).tagName, "HTML");
        assertEquals(bodyAncestors.size(), 1);
        assertEquals(bodyAncestors.get(0).tagName, "HTML");
        assertEquals(scriptAncestors.size(), 2);
        assertEquals(scriptAncestors.get(0).tagName, "HEAD");
        assertEquals(scriptAncestors.get(1).tagName, "BODY");
        assertEquals(linkAncestors.size(), 2);
        assertEquals(linkAncestors.get(0).tagName, "HEAD");
        assertEquals(linkAncestors.get(1).tagName, "BODY");
        assertEquals(pAncestors.size(), 1);
        assertEquals(pAncestors.get(0).tagName, "HTML");
        assertEquals(divAncestors.size(), 1);
        assertEquals(divAncestors.get(0).tagName, "HTML");
    }

    @Test
    public void testTagCanContain() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag h1 = new Tag("H1");
        Tag script = new Tag("SCRIPT");

        assertTrue(p.canContain(new Tag("BR")));
        assertTrue(p.canContain(new Tag("A")));
        assertTrue(p.canContain(new Tag("IMG")));
        assertTrue(p.canContain(new Tag("SPAN")));
        assertTrue(p.canContain(new Tag("P")));
        assertTrue(p.canContain(new Tag("DIV")));
        assertTrue(p.canContain(new Tag("H1")));
        assertFalse(p.canContain(new Tag("P").setCanContainBlock(false)));
        assertFalse(p.canContain(new Tag("SCRIPT")));
        assertFalse(p.canContain(new Tag("TABLE")));

        assertTrue(div.canContain(new Tag("BR")));
        assertTrue(div.canContain(new Tag("A")));
        assertTrue(div.canContain(new Tag("IMG")));
        assertTrue(div.canContain(new Tag("SPAN")));
        assertTrue(div.canContain(new Tag("P")));
        assertTrue(div.canContain(new Tag("DIV")));
        assertTrue(div.canContain(new Tag("H1")));
        assertTrue(div.canContain(new Tag("SCRIPT")));
        assertTrue(div.canContain(new Tag("TABLE")));

        assertFalse(h1.canContain(new Tag("P")));
        assertFalse(h1.canContain(new Tag("DIV")));
        assertFalse(h1.canContain(new Tag("H1")));
        assertFalse(h1.canContain(new Tag("SCRIPT")));
        assertFalse(h1.canContain(new Tag("TABLE")));

        assertFalse(script.canContain(new Tag("P")));
        assertFalse(script.canContain(new Tag("DIV")));
        assertFalse(script.canContain(new Tag("H1")));
        assertFalse(script.canContain(new Tag("SCRIPT")));
        assertFalse(script.canContain(new Tag("TABLE")));
    }

    @Test
    public void testTagCanContainInvalidParents() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag script = new Tag("SCRIPT");

        assertFalse(p.isValidParent(new Tag("SCRIPT")));
        assertFalse(p.isValidParent(new Tag("DIV").setAncestors(Collections.singletonList(new Tag("P")))));
        assertFalse(p.isValidParent(new Tag("P").setAncestors(Collections.singletonList(new Tag("DIV")))));
        assertFalse(script.isValidParent(new Tag("DIV")));
        assertFalse(script.isValidParent(new Tag("SCRIPT")));
    }

    @Test
    public void testTagHashCodeAndEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("DIV");

        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testTagToString() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("DIV");

        assertEquals(tag1.toString(), "p");
        assertEquals(tag2.toString(), "DIV");
    }

    @Test
    public void testTagValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");

        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.isEmpty());
        assertTrue(tag.preserveWhitespace());
        assertEquals(tag.tagName, "unknownTag");
    }
}