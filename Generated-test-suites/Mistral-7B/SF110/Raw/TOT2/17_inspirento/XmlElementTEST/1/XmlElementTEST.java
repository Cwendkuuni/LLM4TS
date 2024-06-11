package com.allenstudio.ir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElement() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
    }

    @Test
    public void testXmlElementWithName() {
        XmlElement xmlElement = new XmlElement("name");
        assertNotNull(xmlElement);
        assertEquals("name", xmlElement.getName());
    }

    // Add other constructor test cases here

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("name");
        String key = "key";
        String value = "value";
        xmlElement.addAttribute(key, value);
        assertEquals(value, xmlElement.getAttribute(key));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("name");
        xmlElement.addAttribute("key", "value");
        assertEquals("value", xmlElement.getAttribute("key"));
        assertEquals(null, xmlElement.getAttribute("non-existent-key"));
    }

    // Add other method test cases here
}