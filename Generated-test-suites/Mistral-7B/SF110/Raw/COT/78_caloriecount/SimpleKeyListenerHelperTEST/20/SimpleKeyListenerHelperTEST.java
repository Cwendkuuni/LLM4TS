package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setUp() {
 component = mock(Component.class);
 listener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListener_Enter() {
 when(component.isFocusable()).thenReturn(true);
 simpleKeyListenerHelper.notifyListener(listener, 10, null);
 verify(listener).enter(component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyListener_InvalidType() {
 simpleKeyListenerHelper.notifyListener(listener, 3, null);
 }

 @Test
 public void testKeyPressed_SupportedKeys() {
 KeyEvent keyEvent = new KeyEvent(component, 120, true, false, 'A', 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.fire(keyEvent.getKeyCode());
 verify(listener).delete(component);

 keyEvent = new KeyEvent(component, 9, true, false, KeyEvent.VK_TAB, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.fire(keyEvent.getKeyCode());
 verify(listener).tab(component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKeyPressed_UnsupportedKey() {
 KeyEvent keyEvent = new KeyEvent(component, 200, true, false, 'A', 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 }
}