package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstruction() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyKeyListenerHelper(componentMock);
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testComponentDetachment() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.detach();
        verify(componentMock).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testEnterEvent() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListener listenerMock = Mockito.mock(SimpleKeyListener.class);

        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.addListener(listenerMock);

        KeyEvent keyEventMock = Mockito.mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        simpleKeyListenerHelper.notifyListener(listenerMock, KeyEvent.KEY_PRESSED, keyEventMock);
        verify(listenerMock).enter(componentMock);
    }

    @Test
    public void testInsertEvent() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListener listenerMock = Mockito.mock(SimpleKeyListener.class);

        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.addListener(listenerMock);

        KeyEvent keyEventMock = Mockito.mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);

        simpleKeyListenerHelper.notifyListener(listenerMock, KeyEvent.KEY_PRESSED, keyEventMock);
        verify(listenerMock).insert(componentMock);
    }

    @Test
    public void testDeleteEvent() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListener listenerMock = Mockito.mock(SimpleKeyListener.class);

        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.addListener(listenerMock);

        KeyEvent keyEventMock = Mockito.mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);

        simpleKeyListenerHelper.notifyListener(listenerMock, KeyEvent.KEY_PRESSED, keyEventMock);
        verify(listenerMock).delete(componentMock);
    }

    @Test
    public void testTabEvent() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListener listenerMock = Mockito.mock(SimpleKeyListener.class);

        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.addListener(listenerMock);

        KeyEvent keyEventMock = Mockito.mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_TAB);

        simpleKeyListenerHelper.notifyListener(listenerMock, KeyEvent.KEY_PRESSED, keyEventMock);
        verify(listenerMock).tab(componentMock);
    }

    @Test
    public void testKeyPressedNotSupportedKeycodes() {
        Component componentMock = Mockito.mock(Component.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);

        SimpleKeyListener listenerMock = Mockito.mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.addListener(listenerMock);

        KeyEvent keyEventMock = Mockito.mock(KeyEvent.class);
        when(keyEventMock.getKeyCode()).thenReturn(KeyEvent.VK_F1);

        simpleKeyListenerHelper.notifyListener(listenerMock, KeyEvent.KEY_PRESSED, keyEventMock);
        verifyNoMoreInteractions(listenerMock);
    }
}