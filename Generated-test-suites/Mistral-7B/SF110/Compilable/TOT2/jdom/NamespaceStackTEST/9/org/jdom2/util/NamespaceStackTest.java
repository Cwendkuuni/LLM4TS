package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;
import org.junit.Assert.*;

public class NamespaceStackTest {
    @Test
    public void testPushElement() {
        // ... (Expert 1's test case)
    }

    @Test(expected = IllegalStateException.class)
    public void testPushElementNullElement() {
        // ...
    }

    @Test
    public void testPushElementMultipleNamespaces() {
        // ... (Expert 1's test case with modifications)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPushElementSameNamespace() {
        // ... (Expert 1's test case with modifications)
    }

    @Test
    public void testPushAttribute() {
        // ... (Expert 2's test case)
    }

    @Test(expected = IllegalStateException.class)
    public void testPushAttributeNullAttribute() {
        // ...
    }

    @Test
    public void testPushAttributeMultipleNamespaces() {
        // ... (Expert 2's test case with modifications)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPushAttributeSameNamespace() {
        // ... (Expert 2's test case with modifications)
    }

    @Test
    public void testPop() {
        // ... (Expert 3's test case)
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        // ...
    }
}