package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

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
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 simpleKeyListenerHelper.addKeyListener(listener);
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testAddKeyListener() {
 simpleKeyListenerHelper.addKeyListener(listener);
 verify(component).addKeyListener(listener);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 verify(component).removeKeyListener(listener);
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testEnter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 10);
 verify(listener).enter(component);
 }

 @Test
 public void testInsert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 155);
 verify(listener).insert(component);
 }

 @Test
 public void testDelete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 127);
 verify(listener).delete(component);
 }

 @Test
 public void testTab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 9);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressed_EnterKey() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 10);
 verify(listener).enter(component);
 }

 @Test
 public void testKeyPressed_TabKey() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 9);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressed_DeleteKey() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 127);
 verify(listener).delete(component);
 }

 @Test
 public void testKeyPressed_InsertKey() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 155);
 verify(listener).insert(component);
 }

 @Test
 public void testKeyPressed_OtherKeys() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.addKeyListener(listener);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_PRESSED, 1);
 // Other keys will not invoke the listener method, since they are not handled in the switch statement in the keyPressed method.
 verify(listener, never()).enter(component);
 verify(listener, never()).insert(component);
 verify(listener, never()).delete(component);
 verify(listener, never()).tab(component);
 }
}