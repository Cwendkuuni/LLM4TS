package com.allenstudio.ir.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("key", "value");
        String actualValue = xmlElement.getAttribute("key");
        assertEquals("value", actualValue);
    }
}