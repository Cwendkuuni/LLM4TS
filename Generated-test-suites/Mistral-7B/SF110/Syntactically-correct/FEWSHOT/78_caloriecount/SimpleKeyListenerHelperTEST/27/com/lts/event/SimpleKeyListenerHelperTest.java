package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper listenerHelper;
    private SimpleKeyListener mockSimpleKeyListener;

    @Before
    public void setUp() {
        component = mock(Component.class);
        mockSimpleKeyListener = mock(SimpleKeyListener.class);
        listenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        if (listenerHelper != null) {
            listenerHelper.detach();
        }
    }

    @Test
    public void testSimpleKeyListenerHelperCreation() {
        assertNotNull(listenerHelper);
        assertNotNull(component);
    }

    @Test
    public void testAddKeyListener() {
        listenerHelper.addListener(mockSimpleKeyListener);
        verify(component).addKeyListener(mockSimpleKeyListener);
    }

    @Test
    public void testKeyPressedEnter() {
        listenerHelper.keyPressed(new KeyEvent(component, 10, 1, KeyEvent.VK_ENTER));
        verify(mockSimpleKeyListener).enter(component);
    }

    @Test
    public void testKeyPressedTab() {
        listenerHelper.keyPressed(new KeyEvent(component, 9, 1, KeyEvent.VK_TAB));
        verify(mockSimpleKeyListener).tab(component);
    }

    @Test
    public void testKeyPressedDelete() {
        listenerHelper.keyPressed(new KeyEvent(component, 127, 1, KeyEvent.VK_DELETE));
        verify(mockSimpleKeyListener).delete(component);
    }

    @Test
    public void testKeyPressedInsert() {
        listenerHelper.keyPressed(new KeyEvent(component, 155, 1, KeyEvent.VK_INSERT));
        verify(mockSimpleKeyListener).insert(component);
    }

    @Test
    public void testDetach() {
        listenerHelper.detach();
        verify(component).removeKeyListener(listenerHelper);
    }
}