package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setUp() {
 component = new JFrame();
 listener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // implementation
 }

 @Override
 public void insert(Component component) {
 // implementation
 }

 @Override
 public void delete(Component component) {
 // implementation
 }

 @Override
 public void tab(Component component) {
 // implementation
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertEquals("Component is not null", component, simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull("Component is not null", simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyPressed_EnterKey() {
 KeyEvent enterKeyEvent = new KeyEvent(component, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.CHAR_UNDEFINED, "Enter");
 simpleKeyListenerHelper.keyPressed(enterKeyEvent);
 assertTrue("Listener not notified for Enter key", listener.isEnterCalled());
 reset(listener);
 }

 @Test
 public void testKeyPressed_InsertKey() {
 KeyEvent insertKeyEvent = new KeyEvent(component, 155, KeyEvent.KEY_PRESSED, 0, KeyEvent.CHAR_UNDEFINED, "Insert");
 simpleKeyListenerHelper.keyPressed(insertKeyEvent);
 assertTrue("Listener not notified for Insert key", listener.isInsertCalled());
 reset(listener);
 }

 @Test
 public void testKeyPressed_DeleteKey() {
 KeyEvent deleteKeyEvent = new KeyEvent(component, 127, KeyEvent.KEY_PRESSED, 0, KeyEvent.CHAR_UNDEFINED, "Delete");
 simpleKeyListenerHelper.keyPressed(deleteKeyEvent);
 assertTrue("Listener not notified for Delete key", listener.isDeleteCalled());
 reset(listener);
 }

 @Test
 public void testKeyPressed_TabKey() {
 KeyEvent tabKeyEvent = new KeyEvent(component, 9, KeyEvent.KEY_PRESSED, 0, KeyEvent.CHAR_UNDEFINED, "Tab");
 simpleKeyListenerHelper.keyPressed(tabKeyEvent);
 assertTrue("Listener not notified for Tab key", listener.isTabCalled());
 reset(listener);
 }

 @Test
 public void testKeyPressed_OtherKey() {
 KeyEvent otherKeyEvent = new KeyEvent(component, 1, KeyEvent.KEY_PRESSED, 0, KeyEvent.CHAR_UNDEFINED, "Other");
 simpleKeyListenerHelper.keyPressed(otherKeyEvent);
 assertFalse("Listener should not be notified for other keys", listener.isEnterCalled());
 assertFalse("Listener should not be notified for other keys", listener.isInsertCalled());
 assertFalse("Listener should not be notified for other keys", listener.isDeleteCalled());
 assertFalse("Listener should not be notified for other keys", listener.isTabCalled());
 }

 @Test
 public void testKeyReleased() {
 KeyEvent keyEvent = new KeyEvent(component, 10, KeyEvent.KEY_RELEASED, 0, KeyEvent.CHAR_UNDEFINED, "Enter");
 simpleKeyListenerHelper.keyReleased(keyEvent);
 // KeyReleased event should not call listener method
 verifyNoMoreInteractions(listener);
 }

 @Test
 public void testKeyTyped() {
 KeyEvent keyEvent = new KeyEvent(component, 10, KeyEvent.KEY_TYPED, 0, 'A'.charCodeAt(0), "A");
 simpleKeyListenerHelper.keyTyped(keyEvent);
 // KeyTyped event should not call listener method
 verifyNoMoreInteractions(listener);
 }

 private void reset(SimpleKeyListener listener) {
 listener.setEnterCalled(false);
 listener.setInsertCalled(false);
 listener.setDeleteCalled(false);
 listener.setTabCalled(false);
 }

}