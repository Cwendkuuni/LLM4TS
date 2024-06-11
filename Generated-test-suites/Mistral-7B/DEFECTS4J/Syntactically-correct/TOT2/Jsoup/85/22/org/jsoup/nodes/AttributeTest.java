package org.junit.platform.commons.annotation.Generated;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Attribute.class)
@Generated("org.jsoup.nodes.AttributeTest")
public class AttributeTest {

    @Test
    public void testAttributeConstructionWithValidArguments() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testAttributeConstructionWithValidArgumentsAndParent() {
        Attributes parent = PowerMockito.mock(Attributes.class);
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.getParent());
    }

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.getValue();
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertNotEquals(oldValue, attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        Appendable accumulator = mock(Appendable.class);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        Attribute.html(attribute.getKey(), attribute.getValue(), accumulator, outputSettings);
        verify(accumulator).append(attribute.getKey());
        verify(accumulator).append("=\"");
        verify(accumulator).append(attribute.getValue());
        verify(accumulator).append('"');
        verify(accumulator).append("\n");
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

}