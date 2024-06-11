package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component component;

 @Before
 public void setup() {
 component = new Component() {
 @Override
 public boolean isFocusable() {
 return true;
 }
 };
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // Implement your logic here
 }

 @Override
 public void insert(Component component) {
 // Implement your logic here
 }

 @Override
 public void delete(Component component) {
 // Implement your logic here
 }

 @Override
 public void tab(Component component) {
 // Implement your logic here
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testKeyEventsAreForwardedToSimpleKeyListener() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 'Enter', false, false, false, false));
 assertEquals(KeyEvent.VK_ENTER, simpleKeyListener.getLastKeyEvent().getKeyCode());

 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 155, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, 'Delete', false, false, false, false));
 assertEquals(KeyEvent.VK_DELETE, simpleKeyListener.getLastKeyEvent().getKeyCode());

 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 127, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_BACK_SPACE, 'Back Space', false, false, false, false));
 assertEquals(KeyEvent.VK_BACK_SPACE, simpleKeyListener.getLastKeyEvent().getKeyCode());

 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 9, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, 'Tab', false, false, false, false));
 assertEquals(KeyEvent.VK_TAB, simpleKeyListener.getLastKeyEvent().getKeyCode());
 }

 @Test
 public void testDetachKeyListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());

 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 'Enter', false, false, false, false));
 assertNull(simpleKeyListener.getLastKeyEvent());
 }

 @Test(expected = NullPointerException.class)
 public void testAttachNullComponent() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttachNonFocusableComponent() {
 component.setFocusable(false);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }
}