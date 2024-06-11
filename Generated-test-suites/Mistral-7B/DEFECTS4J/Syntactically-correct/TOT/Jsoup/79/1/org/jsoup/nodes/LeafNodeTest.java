package org.jsoup.nodes.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.jsoup.nodes.Attribute.*;

public class LeafNodeTest {

    @Test
    public void testCoreValue_NoAttributes() {
        LeafNode node = new LeafNode() {
            {
                nodeName("testNodeName");
            }
        };
        assertEquals("testNodeName", node.coreValue());
    }

    @Test
    public void testAttr_SetAndGet() {
        LeafNode node = new LeafNode() {
            {
                nodeName("testNodeName");
            }
        };

        node.attr("testAttribute", "testValue");
        assertEquals("testValue", node.attr("testAttribute"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureAttributes_HasAttributes() {
        LeafNode node = new LeafNode() {
            {
                nodeName("testNodeName");
                attributes().put("testAttribute", EMPTY_STRING);
            }
        };

        node.ensureAttributes();
    }
}