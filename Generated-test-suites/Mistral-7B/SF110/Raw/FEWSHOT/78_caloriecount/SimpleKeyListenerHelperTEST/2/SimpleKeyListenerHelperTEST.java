package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private Component component;
 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener listener;

 @Before
 public void setup() {
 component = mock(Component.class);
 listener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 // check if component was added as key listener
 verify(component).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 // check if component was removed as key listener
 verify(component).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 when(listener.enter(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, new KeyEvent(component, 10, 0, KeyEvent.VK_ENTER));
 verify(listener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 when(listener.insert(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, new KeyEvent(component, 155, 0, KeyEvent.VK_INSERT));
 verify(listener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 when(listener.delete(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, new KeyEvent(component, 127, 0, KeyEvent.VK_DELETE));
 verify(listener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 when(listener.tab(component)).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, new KeyEvent(component, 9, 0, KeyEvent.VK_TAB));
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedEventEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, 0, KeyEvent.VK_ENTER));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, new KeyEvent(component, 10, 0, KeyEvent.VK_ENTER));
 // keyPressed method should call fire method only if keyCode matches
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testKeyPressedEventOtherKeys() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, 0, KeyEvent.VK_A));
 simpleKeyListenerHelper.fire(new KeyEvent(component, 127, 0, KeyEvent.VK_DELETE));
 // keyPressed method should not call fire method if keyCode does not match
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNullComponent() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNullListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.component = null;
 simpleKeyListenerHelper.fire(new KeyEvent(component, 10, 0, KeyEvent.VK_ENTER));
 assertNull(listener);
 }
}