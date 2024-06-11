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
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertEquals(testComponent, simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyPressed_EnterKey() {
 KeyEvent enterKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, false);
 simpleKeyListenerHelper.keyPressed(enterKeyEvent);
 simpleKeyListener.enter(testComponent);
 assertTrue(enterKeyEvent.isConsumed());
 }

 @Test
 public void testKeyPressed_TabKey() {
 KeyEvent tabKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, false);
 simpleKeyListenerHelper.keyPressed(tabKeyEvent);
 simpleKeyListener.tab(testComponent);
 assertTrue(tabKeyEvent.isConsumed());
 }

 @Test
 public void testKeyPressed_DeleteKey() {
 KeyEvent deleteKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, false);
 simpleKeyListenerHelper.keyPressed(deleteKeyEvent);
 simpleKeyListener.delete(testComponent);
 assertTrue(deleteKeyEvent.isConsumed());
 }

 @Test
 public void testKeyPressed_InsertKey() {
 KeyEvent insertKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, false);
 simpleKeyListenerHelper.keyPressed(insertKeyEvent);
 simpleKeyListener.insert(testComponent);
 assertTrue(insertKeyEvent.isConsumed());
 }

 @Test
 public void testKeyPressed_OtherKey() {
 KeyEvent otherKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, false);
 simpleKeyListenerHelper.keyPressed(otherKeyEvent);
 assertFalse(otherKeyEvent.isConsumed());
 }

 @Test
 public void testNotifyListener_EnterKey() {
 KeyEvent enterKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, false);
 simpleKeyListenerHelper.keyPressed(enterKeyEvent);
 simpleKeyListenerHelper.notifyListeners(enterKeyEvent, 10);
 simpleKeyListener.enter(testComponent);
 verify(simpleKeyListener).enter(testComponent);
 }

 @Test
 public void testNotifyListener_TabKey() {
 KeyEvent tabKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, false);
 simpleKeyListenerHelper.keyPressed(tabKeyEvent);
 simpleKeyListenerHelper.notifyListeners(tabKeyEvent, 9);
 simpleKeyListener.tab(testComponent);
 verify(simpleKeyListener).tab(testComponent);
 }

 @Test
 public void testNotifyListener_DeleteKey() {
 KeyEvent deleteKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, false);
 simpleKeyListenerHelper.keyPressed(deleteKeyEvent);
 simpleKeyListenerHelper.notifyListeners(deleteKeyEvent, 127);
 simpleKeyListener.delete(testComponent);
 verify(simpleKeyListener).delete(testComponent);
 }

 @Test
 public void testNotifyListener_InsertKey() {
 KeyEvent insertKeyEvent = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, false);
 simpleKeyListenerHelper.keyPressed(insertKeyEvent);
 simpleKeyListenerHelper.notifyListeners(insertKeyEvent, 155);
 simpleKeyListener.insert(testComponent);
 verify(simpleKeyListener).insert(testComponent);
 }
}