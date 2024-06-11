package com.lts.event.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.lts.event.SimpleKeyListenerHelper;
import com.lts.event.SimpleKeyListener;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component testComponent;

 @Before
 public void setUp() {
 testComponent = new JFrame();
 simpleKeyListener = new SimpleKeyListener() {};
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 // Test case for constructor
 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 }

 // Test case for attach method
 @Test
 public void testAttach() {
 simpleKeyListenerHelper.attach(simpleKeyListener);
 assertTrue(testComponent.getKeyListeners().contains(simpleKeyListenerHelper));
 }

 // Test case for detach method
 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertFalse(testComponent.getKeyListeners().contains(simpleKeyListenerHelper));
 }

 // Test case for enter method in KeyListener
 @Test
 public void testEnterMethod() {
 simpleKeyListenerHelper.attach(simpleKeyListener);
 KeyEvent keyEvent = new KeyEvent(testComponent, 10, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.enter(testComponent);
 assertTrue(simpleKeyListener.isEnterPressed());
 simpleKeyListenerHelper.detach();
 }

 // Test case for insert method in KeyListener
 @Test
 public void testInsertMethod() {
 simpleKeyListenerHelper.attach(simpleKeyListener);
 KeyEvent keyEvent = new KeyEvent(testComponent, 155, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.insert(testComponent);
 assertTrue(simpleKeyListener.isInsertPressed());
 simpleKeyListenerHelper.detach();
 }

 // Test case for delete method in KeyListener
 @Test
 public void testDeleteMethod() {
 simpleKeyListenerHelper.attach(simpleKeyListener);
 KeyEvent keyEvent = new KeyEvent(testComponent, 127, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.delete(testComponent);
 assertTrue(simpleKeyListener.isDeletePressed());
 simpleKeyListenerHelper.detach();
 }

 // Test case for tab method in KeyListener
 @Test
 public void testTabMethod() {
 simpleKeyListenerHelper.attach(simpleKeyListener);
 KeyEvent keyEvent = new KeyEvent(testComponent, 9, KeyEvent.KEY_PRESSED, 0, 0, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 simpleKeyListener.tab(testComponent);
 assertTrue(simpleKeyListener.isTabPressed());
 simpleKeyListenerHelper.detach();
 }

 // Test case for keyPressed method with invalid keyCode
 @Test(expected = IllegalArgumentException.class)
 public void testKeyPressedInvalidKeyCode() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, 1, KeyEvent.KEY_PRESSED, 0, 0, ' '));
 }

 // Test case for keyPressed method with null KeyEvent
 @Test(expected = NullPointerException.class)
 public void testKeyPressedNullKeyEvent() {
 simpleKeyListenerHelper.keyPressed(null);
 }

 // Test case for notifyListener method with null listener
 @Test(expected = NullPointerException.class)
 public void testNotifyListenerNullListener() {
 simpleKeyListenerHelper.notifyListener(null, 10, null);
 }

}