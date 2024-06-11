package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper);
        assertTrue(component.isFocusable()); // The component should be focusable
        assertNotNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testDetach() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
        assertFalse(component.hasFocus()); // The component should lose focus after detaching the KeyListenerHelper
    }

    @Test
    public void testNotifyListenerEnter() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_BACK_SPACE);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        Component component = new JFrame(); // A valid component
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testKeyPressedNullComponent() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.keyPressed(new KeyEvent(null, 0, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFireNegativeKeycode() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(-1);
    }

    @Test
    public void testKeyPressedEnterAndDetach() {
        Component component = new JFrame();
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
        verifyNoMoreInteractions(listener);
    }

    private SimpleKeyListenerHelper getSimpleKeyListenerHelper(Component component) {
        return new SimpleKeyListenerHelper(component);
    }

    private void addListenerAndFire(SimpleKeyListenerHelper simpleKeyListenerHelper, SimpleKeyListener listener, int keyCode) {
        simpleKeyListenerHelper.addListener(listener);
        simpleKeyListenerHelper.fire(keyCode);
    }
}