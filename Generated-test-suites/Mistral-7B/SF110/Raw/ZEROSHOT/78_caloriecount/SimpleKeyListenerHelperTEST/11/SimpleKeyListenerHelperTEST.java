package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setup() {
 component = mock(Component.class);
 listener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testAddKeyListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.notifyListener(listener, 0, null);
 verify(component).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.detach();
 verify(component).removeKeyListener(simpleKeyListenerHelper);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.notifyListener(listener, 10, null);
 verify(listener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.notifyListener(listener, 155, null);
 verify(listener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.notifyListener(listener, 127, null);
 verify(listener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.notifyListener(listener, 9, null);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(listener, 0, null);
 verify(listener).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, '');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(listener, 0, null);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, '');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(listener, 0, null);
 verify(listener).delete(component);
 }

 @Test
 public void testKeyPressedInsert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, '');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(listener, 0, null);
 verify(listener).insert(component);
 }
}