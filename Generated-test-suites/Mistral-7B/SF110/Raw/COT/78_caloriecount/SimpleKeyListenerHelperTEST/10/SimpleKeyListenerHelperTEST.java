package com.lts.event.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

import com.lts.event.SimpleKeyListener;
import com.lts.event.SimpleKeyListenerHelper;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListener;

    @Before
    public void setUp() {
        simpleKeyListener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.detach();
        assertNotNull(simpleKeyListenerHelper.component);
        verify(simpleKeyListenerHelper.component).removeKeyListener(simpleKeyListenerHelper);
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        verify(simpleKeyListener).enter(mock(Component.class));
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        verify(simpleKeyListener).insert(mock(Component.class));
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
        verify(simpleKeyListener).delete(mock(Component.class));
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(simpleKeyListener).tab(mock(Component.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListener_InvalidKeyCode() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        simpleKeyListenerHelper.fire(KeyEvent.VK_A);
    }

    @Test
    public void testKeyPressed_Enter() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEventEnter = new KeyEvent(mock(Component.class), 1, false, KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyPressed(keyEventEnter);
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        verify(simpleKeyListener).enter(mock(Component.class));
    }

    @Test
    public void testKeyPressed_Insert() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEventInsert = new KeyEvent(mock(Component.class), 1, false, KeyEvent.VK_INSERT);
        simpleKeyListenerHelper.keyPressed(keyEventInsert);
        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        verify(simpleKeyListener).insert(mock(Component.class));
    }

    @Test
    public void testKeyPressed_Delete() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEventDelete = new KeyEvent(mock(Component.class), 1, false, KeyEvent.VK_DELETE);
        simpleKeyListenerHelper.keyPressed(keyEventDelete);
        simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
        verify(simpleKeyListener).delete(mock(Component.class));
    }

    @Test
    public void testKeyPressed_Tab() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEventTab = new KeyEvent(mock(Component.class), 1, false, KeyEvent.VK_TAB);
        simpleKeyListenerHelper.keyPressed(keyEventTab);
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(simpleKeyListener).tab(mock(Component.class));
    }

    @Test
    public void testKeyPressed_InvalidKeyCode() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEventInvalid = new KeyEvent(mock(Component.class), 1, false, KeyEvent.VK_A);
        simpleKeyListenerHelper.keyPressed(keyEventInvalid);
        simpleKeyListenerHelper.fire(KeyEvent.VK_A);
    }
}