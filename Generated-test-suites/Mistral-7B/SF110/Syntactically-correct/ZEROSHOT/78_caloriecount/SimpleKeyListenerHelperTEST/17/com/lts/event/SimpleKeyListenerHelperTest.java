package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setUp() {
 component = new JFrame();
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // Implement enter logic here
 }

 @Override
 public void insert(Component component) {
 // Implement insert logic here
 }

 @Override
 public void delete(Component component) {
 // Implement delete logic here
 }

 @Override
 public void tab(Component component) {
 // Implement tab logic here
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 assertNotNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testComponentAddKeyListener() {
 assertNotNull(simpleKeyListenerHelper.component.getKeyboardFocusManager().getFocusLock());
 assertEquals(1, simpleKeyListenerHelper.component.getFocusTraversalKeys(FocusConstants.FORWARD).length);
 }

 @Test
 public void testKeyPressedEnterKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.enter(component);
 assertTrue(keyEvent.isConsumed());
 }

 @Test
 public void testKeyPressedTabKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.tab(component);
 assertTrue(keyEvent.isConsumed());
 }

 @Test
 public void testKeyPressedDeleteKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.delete(component);
 assertTrue(keyEvent.isConsumed());
 }

 @Test
 public void testKeyPressedInsertKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.insert(component);
 assertTrue(keyEvent.isConsumed());
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testFireKeyCode() {
 int keyCode = KeyEvent.VK_F2;
 simpleKeyListenerHelper.fire(keyCode);
 assertTrue(simpleKeyListener.getClass().isInstance(event.getSource()));
 assertEquals(keyCode, ((KeyEvent) event).getKeyCode());
 }

 @Test(expected = NullPointerException.class)
 public void testDetachWithoutComponent() {
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testNotifyListenerEnter() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
 simpleKeyListener.enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);
 simpleKeyListener.insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);
 simpleKeyListener.delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);
 simpleKeyListener.tab(component);
 }
}