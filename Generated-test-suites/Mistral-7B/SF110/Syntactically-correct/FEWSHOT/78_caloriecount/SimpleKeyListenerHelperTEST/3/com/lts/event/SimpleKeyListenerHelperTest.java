package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper listenerHelper;
 private Component component;
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
 listenerHelper.setComponent(component);
 listenerHelper.fire(10);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 listenerHelper.setComponent(component);
 listenerHelper.fire(155);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 listenerHelper.setComponent(component);
 listenerHelper.fire(127);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 listenerHelper.setComponent(component);
 listenerHelper.fire(9);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 listenerHelper.setComponent(component);
 KeyEvent keyEvent = new KeyEvent(component, 10, false, false, KeyEvent.VK_ENTER, '');
 listenerHelper.keyPressed(keyEvent);
 listenerHelper.fire(10);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 listenerHelper.setComponent(component);
 KeyEvent keyEvent = new KeyEvent(component, 9, false, false, KeyEvent.VK_TAB, '');
 listenerHelper.keyPressed(keyEvent);
 listenerHelper.fire(9);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 listenerHelper.setComponent(component);
 KeyEvent keyEvent = new KeyEvent(component, 127, false, false, KeyEvent.VK_DELETE, '');
 listenerHelper.keyPressed(keyEvent);
 listenerHelper.fire(127);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressedInsert() {
 listenerHelper.setComponent(component);
 KeyEvent keyEvent = new KeyEvent(component, 155, false, false, KeyEvent.VK_INSERT, '');
 listenerHelper.keyPressed(keyEvent);
 listenerHelper.fire(155);
 verify(simpleKeyListener).insert(component);
 }
}