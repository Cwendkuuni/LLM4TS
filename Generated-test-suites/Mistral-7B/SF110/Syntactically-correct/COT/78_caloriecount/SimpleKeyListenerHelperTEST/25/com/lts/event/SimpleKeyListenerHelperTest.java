package com.lts.event.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.event.SimpleKeyListenerHelper;
import com.lts.event.SimpleKeyListener;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 // Test case for constructor
 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 assertNotNull(simpleKeyKeyListenerHelper);
 }

 // Test case for attach method
 @Test
 public void testAttach() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 assertNull(simpleKeyListenerHelper.component);
 }

 // Test case for detach method
 @Test
 public void testDetach() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 assertNull(simpleKeyListenerHelper.component);
 }

 // Test case for notifyListener method with enter event
 @Test
 public void testNotifyListenerEnter() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 SimpleKeyListener listener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // implementation not needed for test
 }
 };
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.component.requestFocus();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(listener).enter(simpleKeyListenerHelper.component);
 }

 // Test case for notifyListener method with insert event
 @Test
 public void testNotifyListenerInsert() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 SimpleKeyListener listener = new SimpleKeyListener() {
 @Override
 public void insert(Component component) {
 // implementation not needed for test
 }
 };
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
 verify(listener).insert(simpleKeyListenerHelper.component);
 }

 // Test case for notifyListener method with delete event
 @Test
 public void testNotifyListenerDelete() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 SimpleKeyListener listener = new SimpleKeyListener() {
 @Override
 public void delete(Component component) {
 // implementation not needed for test
 }
 };
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 verify(listener).delete(simpleKeyListenerHelper.component);
 }

 // Test case for notifyListener method with tab event
 @Test
 public void testNotifyListenerTab() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 SimpleKeyListener listener = new SimpleKeyListener() {
 @Override
 public void tab(Component component) {
 // implementation not needed for test
 }
 };
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.component.requestFocus();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_TAB);
 verify(listener).tab(simpleKeyListenerHelper.component);
 }

 // Test case for keyPressed method with valid key codes
 @Test
 public void testKeyPressedValidKeyCodes() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_INSERT);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_TAB);
 }

 // Test case for keyPressed method with invalid key code
 @Test(expected = IllegalArgumentException.class)
 public void testKeyPressedInvalidKeyCode() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_A);
 }

}