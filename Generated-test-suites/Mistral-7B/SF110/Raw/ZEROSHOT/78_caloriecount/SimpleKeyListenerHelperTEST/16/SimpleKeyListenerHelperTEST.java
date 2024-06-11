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
 public void setup() {
 testComponent = new JFrame();
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // implementation for testing enter method
 }

 @Override
 public void insert(Component component) {
 // implementation for testing insert method
 }

 @Override
 public void delete(Component component) {
 // implementation for testing delete method
 }

 @Override
 public void tab(Component component) {
 // implementation for testing tab method
 }
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 }

 @Test
 public void testNotifyListener() {
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
 assertTrue(simpleKeyListener.isCalledEnter); // Assuming you have a boolean flag isCalledEnter in SimpleKeyListener
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);
 assertTrue(simpleKeyListener.isCalledInsert);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);
 assertTrue(simpleKeyListener.isCalledDelete);
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);
 assertTrue(simpleKeyListener.isCalledTab);
 }

 @Test(expected = NullPointerException.class)
 public void testDetachAndAddNullComponent() {
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 // this test will throw an exception if the component passed is null
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(testComponent);
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testKeyPressed() {
 KeyEvent event = new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, 1, 1, KeyEvent.VK_ENTER, '');
 simpleKeyListenerHelper.keyPressed(event);
 assertTrue(simpleKeyListener.isCalledEnter);
 simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, 1, 1, KeyEvent.VK_DELETE, ''));
 assertTrue(simpleKeyListener.isCalledDelete);
 simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, 1, 1, KeyEvent.VK_BACK_SPACE, ''));
 assertTrue(simpleKeyListener.isCalledDelete);
 simpleKeyListenerHelper.keyPressed(new KeyEvent(testComponent, KeyEvent.KEY_PRESSED, 1, 1, KeyEvent.VK_TAB, ''));
 assertTrue(simpleKeyListener.isCalledTab);
}

 @Test
 public void testKeyReleased() {
 // Add test cases for keyReleased method if required
 }

 @Test
 public void testKeyTyped() {
 // Add test cases for keyTyped method if required
 }
}