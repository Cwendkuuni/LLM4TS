package com.allenstudio.ir.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "testValue");
        assertEquals("testValue", xmlElement.getAttribute("test"));
    }
}