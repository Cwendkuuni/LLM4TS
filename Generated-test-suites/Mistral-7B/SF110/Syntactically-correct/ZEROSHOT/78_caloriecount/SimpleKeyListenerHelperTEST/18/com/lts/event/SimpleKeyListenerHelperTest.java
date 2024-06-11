package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setup() {
 component = mock(Component.class);
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testAttach() {
 simpleKeyListenerHelper.attach();
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
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_Tab);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, true, false, KeyEvent.KEY_CODE_ENTER));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, true, false, KeyEvent.KEY_CODE_Tab));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_Tab);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, true, false, KeyEvent.KEY_CODE_DELETE));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressedInsert() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, true, false, KeyEvent.KEY_CODE_INSERT));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }
}