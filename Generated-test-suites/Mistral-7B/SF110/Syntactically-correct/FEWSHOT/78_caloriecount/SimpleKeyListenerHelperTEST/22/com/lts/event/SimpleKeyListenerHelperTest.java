package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SimpleKeyListenerHelperTest {

 private Component component;
 private SimpleKeyListenerHelper listenerHelper;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setup() {
 component = mock(Component.class);
 simpleKeyListener = mock(SimpleKeyListener.class);
 listenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 listenerHelper = new SimpleKeyListenerHelper(component);
 assertNull(listenerHelper.getComponent());
 }

 @Test
 public void testAttach() {
 listenerHelper = new SimpleKeyListenerHelper(component);
 listenerHelper.attach();
 verify(component).addKeyListener(listenerHelper);
 }

 @Test
 public void testDetach() {
 listenerHelper = new SimpleKeyListenerHelper(component);
 listenerHelper.attach();
 listenerHelper.detach();
 verify(component).removeKeyListener(listenerHelper);
 }

 @Test
 public void testNotifyListenerEnter() {
 SimpleKeyEvent event = new SimpleKeyEvent(KeyEvent.KEY_PRESSED, 10, 1, ' ', false);
 listenerHelper.notifyListener(simpleKeyListener, 10, event);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 SimpleKeyEvent event = new SimpleKeyEvent(KeyEvent.KEY_PRESSED, 155, 1, KeyEvent.CHAR_UNDEFINED, false);
 listenerHelper.notifyListener(simpleKeyListener, 155, event);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 SimpleKeyEvent event = new SimpleKeyEvent(KeyEvent.KEY_PRESSED, 127, 1, KeyEvent.CHAR_DELETED, false);
 listenerHelper.notifyListener(simpleKeyListener, 127, event);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 SimpleKeyEvent event = new SimpleKeyEvent(KeyEvent.KEY_PRESSED, 9, 1, KeyEvent.CHAR_UNDEFINED, false);
 listenerHelper.notifyListener(simpleKeyListener, 9, event);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent event = new KeyEvent(component, 10, 1, ' ', false);
 listenerHelper.keyPressed(event);
 listenerHelper.fire(10);
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent event = new KeyEvent(component, 9, 1, ' ', false);
 listenerHelper.keyPressed(event);
 listenerHelper.fire(9);
 }

 @Test
 public void testKeyPressedDelete() {
 KeyEvent event = new KeyEvent(component, 127, 1, KeyEvent.CHAR_DELETED, false);
 listenerHelper.keyPressed(event);
 listenerHelper.fire(127);
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent event = new KeyEvent(component, 155, 1, KeyEvent.CHAR_UNDEFINED, false);
 listenerHelper.keyPressed(event);
 listenerHelper.fire(155);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFireWithNullObject() {
 listenerHelper.fire(null);
 }

}