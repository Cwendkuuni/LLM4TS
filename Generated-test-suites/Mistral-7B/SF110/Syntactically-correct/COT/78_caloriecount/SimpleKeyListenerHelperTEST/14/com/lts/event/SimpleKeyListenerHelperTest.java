package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component testComponent;
    private SimpleKeyListener testListener;

    @Before
    public void setup() {
        testComponent = mock(Component.class);
        testListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(testComponent, times(1)).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testNotifyListener_enter() {
        simpleKeyListenerHelper.notifyListener(testListener, 10, null);
        verify(testListener, times(1)).enter(testComponent);
    }

    @Test
    public void testNotifyListener_insert() {
        simpleKeyListenerHelper.notifyListener(testListener, 155, null);
        verify(testListener, times(1)).insert(testComponent);
    }

    @Test
    public void testNotifyListener_delete() {
        simpleKeyListenerHelper.notifyListener(testListener, 127, null);
        verify(testListener, times(1)).delete(testComponent);
    }

    @Test
    public void testNotifyListener_tab() {
        simpleKeyListenerHelper.notifyListener(testListener, 9, null);
        verify(testListener, times(1)).tab(testComponent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListener_invalidType() {
        simpleKeyListenerHelper.notifyListener(testListener, 5, null);
    }

    @Test
    public void testKeyPressed() {
        keyPressEvent(KeyEvent.VK_TAB);
        verify(testComponent, times(1)).requestFocus();
    }

    @Test
    public void testKeyPressed_multipleKeys() {
        keyPressEvent(KeyEvent.VK_ENTER);
        keyPressEvent(KeyEvent.VK_DELETE);
        verify(testComponent, times(2)).requestFocus();
    }

    @Test
    public void testKeyPressed_invalidKeyCode() {
        keyPressEvent(KeyEvent.VK_Z);
        verify(testComponent, never()).requestFocus();
    }

    private void keyPressEvent(int keyCode) {
        when(testComponent.getFocusOwner()).thenReturn(testComponent);
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, keyCode, KeyEvent.KEY_PRESSED, System.currentTimeMillis()));
    }

}