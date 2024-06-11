package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    // Test case for constructor and component attachment
    @Test
    public void testSimpleKeyListenerHelperAttachment() {
        Component testComponent = new Button(); // any Component subclass will do
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        assertNotNull(helper.getComponent());
        assertEquals(testComponent, helper.getComponent());
    }

    // Test case for key events: enter
    @Test
    public void testEnterKeyEvent() {
        Component testComponent = new Button();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent enterKeyEvent = new KeyEvent(testComponent, 10, 1, ' ');
        helper.keyPressed(enterKeyEvent);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertNotNull(component);
                assertEquals(testComponent, component);
            }
        };
        helper.setListener(listener);
        helper.fire(enterKeyEvent.getKeyCode());
        listener.assertEnterCalled();
    }

    // Test case for key events: insert
    @Test
    public void testInsertKeyEvent() {
        Component testComponent = new Button();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent insertKeyEvent = new KeyEvent(testComponent, 155, 1, ' ');
        helper.keyPressed(insertKeyEvent);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void insert(Component component) {
                assertNotNull(component);
                assertEquals(testComponent, component);
            }
        };
        helper.setListener(listener);
        helper.fire(insertKeyEvent.getKeyCode());
        listener.assertInsertCalled();
    }

    // Test case for key events: delete
    @Test
    public void testDeleteKeyEvent() {
        Component testComponent = new Button();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent deleteKeyEvent = new KeyEvent(testComponent, 127, 1, ' ');
        helper.keyPressed(deleteKeyEvent);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void delete(Component component) {
                assertNotNull(component);
                assertEquals(testComponent, component);
            }
        };
        helper.setListener(listener);
        helper.fire(deleteKeyEvent.getKeyCode());
        listener.assertDeleteCalled();
    }

    // Test case for key events: tab
    @Test
    public void testTabKeyEvent() {
        Component testComponent = new Button();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        KeyEvent tabKeyEvent = new KeyEvent(testComponent, 9, 1, ' ');
        helper.keyPressed(tabKeyEvent);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void tab(Component component) {
                assertNotNull(component);
                assertEquals(testComponent, component);
            }
        };
        helper.setListener(listener);
        helper.fire(tabKeyEvent.getKeyCode());
        listener.assertTabCalled();
    }

    // Test case for detach
    @Test
    public void testSimpleKeyListenerHelperDetach() {
        Component testComponent = new Button();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
        helper.detach();
        assertNull(helper.getComponent());
    }

    // Test case for null component
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleKeyListenerHelperNullComponent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
    }

    // 
}