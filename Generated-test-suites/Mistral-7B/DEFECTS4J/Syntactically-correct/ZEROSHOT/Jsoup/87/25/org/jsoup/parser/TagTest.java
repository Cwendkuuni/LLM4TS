package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag class.
 */
public class TagTest {

    @Test
    public void testTagCreationAndNameRetrieval() {
        Tag tag = new Tag("p");
        String tagName = tag.getName();
        assertEquals("p", tagName);
    }

    @Test
    public void testTagValueOfWithPredefinedTag() {
        Tag tag = Tag.valueOf("p");
        String tagName = tag.getName();
        assertEquals("p", tagName);
    }

    @Test
    public void testTagValueOfWithUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");
        String tagName = tag.getName();
        assertNotNull(tag);
        assertEquals("unknownTag", tagName);
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("p");
        boolean isBlock = tag.isBlock();
        assertTrue(isBlock);
    }

    @Test
    public void testTagIsFormatAsBlock() {
        Tag tag = new Tag("p");
        boolean formatAsBlock = tag.formatAsBlock();
        assertTrue(formatAsBlock);
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag = new Tag("img");
        boolean empty = tag.isEmpty();
        assertFalse(empty);
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new Tag("img");
        boolean selfClosing = tag.isSelfClosing();
        assertFalse(selfClosing);
    }

    @Test
    public void testTagIsKnownTag() {
        Tag tag = new Tag("p");
        boolean isKnownTag = Tag.isKnownTag("p");
        assertTrue(isKnownTag);
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag = new Tag("pre");
        boolean preserveWhitespace = tag.preserveWhitespace();
        assertTrue(preserveWhitespace);
    }

    @Test
    public void testTagIsFormListed() {
        Tag tag = new Tag("input");
        boolean isFormListed = tag.isFormListed();
        assertTrue(isFormListed);
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag tag = new Tag("input");
        boolean isFormSubmittable = tag.isFormSubmittable();
        assertTrue(isFormSubmittable);
    }

    @Test
    public void testTagEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
        Tag tag3 = new Tag("div");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testTagHashCode() {
        Tag tag = new Tag("p");
        int hashCode1 = tag.hashCode();
        Tag tag2 = new Tag("p");
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testTagToString() {
        Tag tag = new Tag("p");
        String tagString = tag.toString();
        assertEquals("p", tagString);
    }

    @Test
    public void testBlockTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.blockTags) {
            Tag tag = new Tag(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.blockTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.inlineTags) {
            Tag tag = new Tag(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.inlineTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.tags.get(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.emptyTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormatAsInlineTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.tags.get(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.formatAsInlineTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.tags.get(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.preserveWhitespaceTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.tags.get(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.formListedTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void testFormSubmitTags() {
        Map<String, Tag> expectedTags = new HashMap<>();

        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.tags.get(tagName);
            expectedTags.put(tagName, tag);
        }

        Map<String, Tag> actualTags = new HashMap<>();
        for (Map.Entry<String, Tag> entry : Tag.tags.entrySet()) {
            if (Tag.formSubmitTags.contains(entry.getKey())) {
                actualTags.put(entry.getKey(), entry.getValue());
            }
        }

        assertEquals(expectedTags, actualTags);
    }
}