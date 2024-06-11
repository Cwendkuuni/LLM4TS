package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    // List all the public methods and their signatures

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        Component component = new JFrame(); // assuming JFrame extends Component
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testDetachMethod() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_DELETE);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnterKey() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEYCODE_ENTER, 0, 0));
        simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
        verify(listener).enter(component);
    }

    @Test
    public void testKeyPressedTabKey() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.VK_TAB, 0, 0));
        simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
        verify(listener).tab(component);
    }

    @Test(expected = NullPointerException.class)
    public void testKeyPressedWithNullComponent() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.keyPressed(new KeyEvent(null, KeyEvent.KEYCODE_ENTER, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFireWithUnknownKeyCode() {
        Component component = new JFrame();
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.fire(123);
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
        simpleKeyListenerHelper.keyReleased(new KeyEvent(new JFrame(), KeyEvent.VK_A, 0, 0));
        // No verification as keyReleased does not call notifyListener
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
        simpleKeyListenerHelper.keyTyped(new KeyEvent(new JFrame(), KeyEvent.VK_A, 0, 'A'));
        // No verification as keyTyped does not call notifyListener
    }

    @Test
    public void testDetachAfterComponentIsNull() {
        SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.detach();
        // No verification as detach does not throw exception when component is null
    }
}