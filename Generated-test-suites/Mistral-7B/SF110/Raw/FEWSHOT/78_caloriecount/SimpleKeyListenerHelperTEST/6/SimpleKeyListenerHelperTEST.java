package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testConstructor() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        assertEquals(mockComponent, helper.getComponent());
    }

    @Test
    public void testDetach() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.detach();
        verify(mockComponent, times(1)).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);

        helper.fire(KeyEvent.KEY_CODE_ENTER);
        verify(mockListener, times(1)).enter(mockComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);

        helper.fire(KeyEvent.KEY_CODE_INSERT);
        verify(mockListener, times(1)).insert(mockComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);

        helper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
        verify(mockListener, times(1)).delete(mockComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);

        helper.fire(KeyEvent.VK_TAB);
        verify(mockListener, times(1)).tab(mockComponent);
    }

    @Test
    public void testKeyPressedEnter() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);

        helper.addListener(mockListener);
        KeyEvent mockEvent = Mockito.mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.KEY_CODE_ENTER);
        helper.keyPressed(mockEvent);
        verify(mockListener, times(1)).enter(mockComponent);
    }

    @Test
    public void testKeyPressedTab() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);

        helper.addListener(mockListener);
        KeyEvent mockEvent = Mockito.mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        helper.keyPressed(mockEvent);
        verify(mockListener, times(1)).tab(mockComponent);
    }

    @Test
    public void testKeyPressedBackSpace() {
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);

        helper.addListener(mockListener);
        KeyEvent mockEvent = Mockito.mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.KEY_CODE_BACK_SPACE);
        helper.keyPressed(mockEvent);
        verify(mockListener, times(1)).delete(mockComponent);
    }
}