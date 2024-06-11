package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component testComponent;

 @Before
 public void setUp() {
 testComponent = new JFrame(); // Assuming JFrame is the Component type used in SimpleKeyListenerHelper
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // implementation for enter event
 }

 @Override
 public void insert(Component component) {
 // implementation for insert event
 }

 @Override
 public void delete(Component component) {
 // implementation for delete event
 }

 @Override
 public void tab(Component component) {
 // implementation for tab event
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
 assertTrue(simpleKeyListener.isInvoked); // Assuming SimpleKeyListener has an isInvoked flag to check if the method was invoked
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent keyEventEnter = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
 simpleKeyListenerHelper.keyPressed(keyEventEnter);
 simpleKeyListener.isInvoked = false; // Reset the flag
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent keyEventInsert = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' ');
 simpleKeyListenerHelper.keyPressed(keyEventInsert);
 simpleKeyListener.isInvoked = false;
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testKeyPressedDelete() {
 KeyEvent keyEventDelete = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
 simpleKeyListenerHelper.keyPressed(keyEventDelete);
 simpleKeyListener.isInvoked = false;
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent keyEventTab = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');
 simpleKeyListenerHelper.keyPressed(keyEventTab);
 simpleKeyListener.isInvoked = false;
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);
 assertTrue(simpleKeyListener.isInvoked);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }
}