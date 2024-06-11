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
 testComponent = new JFrame();
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 failure("enter method should not be called in tests.");
 }

 @Override
 public void insert(Component component) {
 failure("insert method should not be called in tests.");
 }

 @Override
 public void delete(Component component) {
 failure("delete method should not be called in tests.");
 }

 @Override
 public void tab(Component component) {
 failure("tab method should not be called in tests.");
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 assertTrue(testComponent.isFocusable());
 assertFalse(testComponent.isFocusOwner());
 }

 @Test
 public void testComponentAddKeyListener() {
 assertNotNull(simpleKeyListenerHelper.getComponent());
 assertTrue(simpleKeyListenerHelper.getComponent().isFocusable());
 assertFalse(simpleKeyListenerHelper.getComponent().isFocusOwner());
 }

 @Test
 public void testComponentRemoveKeyListener() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.enter(testComponent);
 assertTrue(simpleKeyListener.isEnterCalled);
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.tab(testComponent);
 assertTrue(simpleKeyListener.isTabCalled);
 }

 @Test
 public void testKeyPressedDelete() {
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.delete(testComponent);
 assertTrue(simpleKeyListener.isDeleteCalled);
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.insert(testComponent);
 assertTrue(simpleKeyListener.isInsertCalled);
 }

 @Test
 public void testKeyPressedOtherKey() {
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 0);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertTrue(simpleKeyListener.isOtherKeyCalled);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testFireKeyEvent() {
 SimpleKeyListener listener = spy(simpleKeyListener);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 simpleKeyListenerHelper.addListener(listener);
 KeyEvent keyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0);
 simpleKeyListenerHelper.fire(keyEvent.getKeyCode());
 verify(listener).enter(testComponent);
 }

 private static class SimpleKeyListener implements KeyListener {
 boolean isEnterCalled = false;
 boolean isTabCalled = false;
 boolean isDeleteCalled = false;
 boolean isInsertCalled = false;
 boolean isOtherKeyCalled = false;

 @Override
 public void enter(Component component) {
 isEnterCalled = true;
 }

 @Override
 public void insert(Component component) {
 isInsertCalled = true;
 }

 @Override
 public void delete(Component component) {
 isDeleteCalled = true;
 }

 @Override
 public void tab(Component component) {
 isTabCalled = true;
 }

 @Override
 public void keyTyped(KeyEvent e) {
 isOtherKeyCalled = true;
 }
 }
}