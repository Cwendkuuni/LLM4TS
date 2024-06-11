
package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component testComponent;
    private SimpleKeyListener mockSimpleKeyListener;

    @Before
    public void setUp() {
        testComponent = mock(Component.class);
        mockSimpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
    }

    @Test
    public void testKeyListenerAttached() {
        verify(testComponent, times(1)).addKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testKeyPressed_Enter_KeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 10, 0, 0));
        simpleKeyListenerHelper.fire(10);
        verify(mockSimpleKeyListener, times(1)).enter(testComponent);
    }

    @Test
    public void testKeyPressed_Delete_KeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 127, 0, 0));
        simpleKeyListenerHelper.fire(127);
        verify(mockSimpleKeyListener, times(1)).delete(testComponent);
    }

    @Test
    public void testKeyPressed_Tab_KeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 9, 0, 0));
        simpleKeyListenerHelper.fire(9);
        verify(mockSimpleKeyListener, times(1)).tab(testComponent);
    }

    @Test
    public void testKeyPressed_Insert_KeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 155, 0, 0));
        simpleKeyListenerHelper.fire(155);
        verify(mockSimpleKeyListener, times(1)).insert(testComponent);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(testComponent, times(1)).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testFire_NullComponent() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.fire(10);
        // Assertion or verification needed based on the application's behavior
        // For example, Assert.assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testFire_NullListener() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.component = null;
        simpleKeyListenerHelper.fire(10);
        // Assertion or verification needed based on the application's behavior
        // For example, Assert.assertNull(simpleKeyListenerHelper.listener);
    }

    @Test
    public void testKeyPressed_InvalidKeyCode() {
        simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 1, 0, 0));
        simpleKeyListenerHelper.fire(1);
        verify(mockSimpleKeyListener, never()).enter(testComponent);
        verify(mockSimpleKeyListener, never()).delete(testComponent);
        verify(mockSimpleKeyListener, never()).tab(testComponent);
        verify(mockSimpleKeyListener, never()).insert(testComponent);
    }

    @Test
    public void testKeyReleased() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.keyReleased(new KeyEvent(testComponent, 0, 0, 0));
        // No verification or assertion needed as keyReleased is an empty method
    }

    @Test
    public void testKeyTyped() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
        simpleKeyListenerHelper.keyTyped(new KeyEvent(testComponent, 0, 0, 'A'));
        // No verification or assertion needed as keyTyped is an empty method
    }
}
