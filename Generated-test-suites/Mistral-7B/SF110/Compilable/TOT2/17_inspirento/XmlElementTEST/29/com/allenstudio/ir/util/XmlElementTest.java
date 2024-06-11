package com.allenstudio.ir.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        String attributeName = "test";
        String attributeValue = "testValue";
        xmlElement.addAttribute(attributeName, attributeValue);
        assertEquals(attributeValue, xmlElement.getAttribute(attributeName));
    }
}