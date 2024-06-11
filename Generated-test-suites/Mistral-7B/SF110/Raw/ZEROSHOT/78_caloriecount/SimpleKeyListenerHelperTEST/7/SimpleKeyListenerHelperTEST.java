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
 // implement enter logic here
 }

 @Override
 public void insert(Component component) {
 // implement insert logic here
 }

 @Override
 public void delete(Component component) {
 // implement delete logic here
 }

 @Override
 public void tab(Component component) {
 // implement tab logic here
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testComponentIsAdded() {
 assertTrue(testComponent.getFocusTraversalKeysEnabled());
 assertTrue(testComponent.isFocusable());
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.FORWARD_TRAVERSAL));
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.BACKWARD_TRAVERSAL));
 assertNotNull(simpleKeyListenerHelper);
 assertSame(testComponent, simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyListenerIsAdded() {
 assertTrue(testComponent.getFocusTraversalKeysEnabled());
 assertTrue(testComponent.isFocusable());
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.FORWARD_TRAVERSAL));
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.BACKWARD_TRAVERSAL));
 assertNotNull(simpleKeyListener);
 assertEquals(1, testComponent.getListenerList().size());
 assertSame(simpleKeyListener, testComponent.getListenerList().get(0));
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 assertFalse(testComponent.getFocusTraversalKeysEnabled());
 assertFalse(testComponent.isFocusable());
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.FORWARD_TRAVERSAL));
 assertNotNull(testComponent.getFocusTraversalKeys(FocusTraversalPolicy.BACKWARD_TRAVERSAL));
 assertNull(simpleKeyListenerHelper);
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 10, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.enter(simpleKeyListenerHelper.getComponent());
 assertTrue(simpleKeyListener.isEnterPressed());
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 155, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.insert(simpleKeyListenerHelper.getComponent());
 assertTrue(simpleKeyListener.isInsertPressed());
 }

 @Test
 public void testKeyPressedBackspace() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 127, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.delete(simpleKeyListenerHelper.getComponent());
 assertTrue(simpleKeyListener.isBackspacePressed());
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent keyEvent = new KeyEvent(testComponent, 9, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.tab(simpleKeyListenerHelper.getComponent());
 assertTrue(simpleKeyListener.isTabPressed());
 }

 @Test(expected = NullPointerException.class)
 public void testKeyPressedNullComponent() {
 SimpleKeyListenerHelper nullComponentSimpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 KeyEvent keyEvent = new KeyEvent(null, 10, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 nullComponentSimpleKeyListenerHelper.keyPressed(keyEvent);
 }

 @Test
 public void testFireKeyCode() {
 int expectedKeyCode = 10;
 simpleKeyListenerHelper.fire(expectedKeyCode);
 simpleKeyListener.enter(simpleKeyListenerHelper.getComponent());
 assertEquals(expectedKeyCode, simpleKeyListener.getKeyCode());
 }
}