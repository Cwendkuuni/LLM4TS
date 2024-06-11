package com.lts.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        component = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        listener = mock(SimpleKeyListener.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
        assertNotNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter_KeyCode_10() {
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_Insert_KeyCode_155() {
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_Delete_KeyCode_127() {
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_Tab_KeyCode_9() {
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListener_InvalidKeyCode() {
        simpleKeyListenerHelper.notifyListener(listener, 128, null);
    }

    @Test
    public void testKeyPressed_Enter_KeyCode_10() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER));
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
        verify(listener).enter(component);
    }

    @Test
    public void testKeyPressed_Tab_KeyCode_9() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_Delete_KeyCode_127() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE));
        simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
        verify(listener).delete(component);
    }

    @Test
    public void testKeyPressed_Insert_KeyCode_155() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT));
        simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
        verify(listener).insert(component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyPressed_InvalidKeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_F1));
        simpleKeyListenerHelper.fire(KeyEvent.VK_F1);
    }

    @Test
    public void testKeyReleased() {
        simpleKeyListenerHelper.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, 0, KeyEvent.VK_BACK_SPACE));
    }

    @Test
    public void testKeyTyped() {
        simpleKeyListenerHelper.keyTyped(new KeyEvent(component, KeyEvent.KEY_TYPED, 0, 'A'));
    }
}