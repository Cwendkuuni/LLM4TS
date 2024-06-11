package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setUp() {
 component = Mockito.mock(Component.class);
 listener = Mockito.mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyKeyListenerHelper(component);
 }

 @Test
 public void testAddKeyListener() {
 assertNotNull(simpleKeyListenerHelper.getComponent());
 verify(component).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 verify(component).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListenerEnter() {
 when(listener.enter(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER);
 verify(listener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 when(listener.insert(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, KeyEvent.VK_INSERT);
 verify(listener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 when(listener.delete(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, KeyEvent.VK_BACK_SPACE);
 verify(listener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 when(listener.tab(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, KeyEvent.VK_TAB);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedEventEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.NONE, Character.MIN_VALUE, KeyEvent.CHAMED_KEY_PRESSED));
 verify(listener).enter(component);
 }

 @Test
 public void testKeyPressedEventInsert() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.NONE, Character.MIN_VALUE, KeyEvent.NONE));
 verify(listener).insert(component);
 }

 @Test
 public void testKeyPressedEventDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.NONE, Character.MIN_VALUE, KeyEvent.NONE));
 verify(listener).delete(component);
 }

 @Test
 public void testKeyPressedEventTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.NONE, Character.MIN_VALUE, KeyEvent.NONE));
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedEventOther() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_F1, KeyEvent.NONE, Character.MIN_VALUE, KeyEvent.NONE));
 verifyNoMoreInteractions(listener);
 }
}