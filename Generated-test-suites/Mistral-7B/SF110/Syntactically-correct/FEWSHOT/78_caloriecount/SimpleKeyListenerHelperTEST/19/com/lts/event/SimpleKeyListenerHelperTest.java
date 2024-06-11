package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListener helper;
 private Component component;

 @Before
 public void setUp() {
 component = mock(Component.class);
 helper = new SimpleKeyKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 assertNotNull(helper);
 assertEquals(component, helper.getComponent());
 }

 @Test
 public void testDetach() {
 helper.detach();
 assertNull(helper.getComponent());
 }

 @Test
 public void testNotifyListenerEnter() {
 final SimpleKeyListener listener = mock(SimpleKeyListener.class);
 when(component.isEnabled()).thenReturn(true);
 helper.notifyListener(listener, 10, null);
 verify(listener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 final SimpleKeyListener listener = mock(SimpleKeyListener.class);
 when(component.isEnabled()).thenReturn(true);
 helper.notifyListener(listener, 155, null);
 verify(listener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 final SimpleKeyListener listener = mock(SimpleKeyListener.class);
 when(component.isEnabled()).thenReturn(true);
 helper.notifyListener(listener, 127, null);
 verify(listener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 final SimpleKeyListener listener = mock(SimpleKeyListener.class);
 when(component.isEnabled()).thenReturn(true);
 helper.notifyListener(listener, 9, null);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 helper.keyPressed(new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
 verify(component).requestFocus();
 }

 @Test
 public void testKeyPressedTab() {
 helper.keyPressed(new KeyEvent(component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ""));
 verify(component).transferFocus();
 }

 @Test
 public void testKeyPressedDelete() {
 helper.keyPressed(new KeyEvent(component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ""));
 verify(component).requestFocus();
 }

 @Test
 public void testKeyPressedInsert() {
 helper.keyPressed(new KeyEvent(component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ""));
 verify(component).requestFocus();
 }

 @Test
 public void testKeyPressedOther() {
 helper.keyPressed(new KeyEvent(component, 1, System.currentTimeMillis(), 0, KeyEvent.VK_A, ""));
 assertFalse(helper.fire(KeyEvent.VK_A));
 }
}