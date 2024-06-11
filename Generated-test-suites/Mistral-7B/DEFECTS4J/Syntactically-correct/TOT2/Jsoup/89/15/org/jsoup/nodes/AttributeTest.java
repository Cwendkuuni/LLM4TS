package org.junit.platform.commons.annotation.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RunWith(JUnitPlatform.class)
public class AttributeTest {

    @Test
    public void testCreateAttributeWithValidKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    // Test other methods and their test cases here...

    @Test
    public void testShouldCollapseAttributeWithValidBooleanAttributeKeyAndEmptyValue() {
        boolean isCollapsible = Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "checked");
        assertTrue(isCollapsible);
    }

    @Test
    public void testShouldCollapseAttributeWithValidBooleanAttributeKeyAndSameValue() {
        boolean isCollapsible = Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "checked", "checked");
        assertTrue(isCollapsible);
    }

    // Test other methods and their test cases here...
}