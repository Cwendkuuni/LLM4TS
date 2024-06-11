package org.jdom2.util;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class NamespaceStackTest {
    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }
}