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
 testComponent = new Label("Test Component");
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // Implementation for enter event
 }

 @Override
 public void insert(Component component) {
 // Implementation for insert event
 }

 @Override
 public void delete(Component component) {
 // Implementation for delete event
 }

 @Override
 public void tab(Component component) {
 // Implementation for tab event
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 assertNotNull(simpleKeyListenerHelper.component);
 assertEquals(testComponent, simpleKeyListenerHelper.component);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListenerEnter() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, "ENTER");
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, keyEvent);
 simpleKeyListener.enter(testComponent);
 assertTrue(simpleKeyListener.isEnterCalled);
 }

 @Test
 public void testNotifyListenerInsert() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 155, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, "INSERT");
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, keyEvent);
 simpleKeyListener.insert(testComponent);
 assertTrue(simpleKeyListener.isInsertCalled);
 }

 @Test
 public void testNotifyListenerDelete() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 127, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, "BACK_SPACE");
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, keyEvent);
 simpleKeyListener.delete(testComponent);
 assertTrue(simpleKeyListener.isDeleteCalled);
 }

 @Test
 public void testNotifyListenerTab() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 9, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, "TAB");
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, keyEvent);
 simpleKeyListener.tab(testComponent);
 assertTrue(simpleKeyListener.isTabCalled);
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, "ENTER");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertTrue(simpleKeyListener.isEnterCalled);
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 155, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, "INSERT");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertTrue(simpleKeyListener.isInsertCalled);
 }

 @Test
 public void testKeyPressedDelete() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 127, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, "BACK_SPACE");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertTrue(simpleKeyListener.isDeleteCalled);
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 9, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, "TAB");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertTrue(simpleKeyListener.isTabCalled);
 }

 @Test
 public void testKeyPressedNonSupportedKey() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 1, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_A, "A");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertFalse(simpleKeyListener.isEnterCalled);
 assertFalse(simpleKeyListener.isInsertCalled);
 assertFalse(simpleKeyListener.isDeleteCalled);
 assertFalse(simpleKeyListener.isTabCalled);
 }
}