package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component component;

 @Before
 public void setUp() {
 component = new JFrame();
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
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertNotNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListenerEnter() {
 KeyEvent keyEvent = new KeyEvent(component, 10, 1, ' ');
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, keyEvent);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();

 keyEvent = new KeyEvent(component, 10, 1, ' ');
 simpleKeyListenerHelper.fire(10);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();
 }

 @Test
 public void testNotifyListenerInsert() {
 KeyEvent keyEvent = new KeyEvent(component, 155, 1, ' ');
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, keyEvent);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();

 keyEvent = new KeyEvent(component, 155, 1, ' ');
 simpleKeyListenerHelper.fire(155);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();
 }

 @Test
 public void testNotifyListenerDelete() {
 KeyEvent keyEvent = new KeyEvent(component, 127, 1, ' ');
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, keyEvent);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();

 keyEvent = new KeyEvent(component, 127, 1, ' ');
 simpleKeyListenerHelper.fire(127);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();
 }

 @Test
 public void testNotifyListenerTab() {
 KeyEvent keyEvent = new KeyEvent(component, 9, 1, ' ');
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, keyEvent);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();

 keyEvent = new KeyEvent(component, 9, 1, ' ');
 simpleKeyListenerHelper.fire(9);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testKeyPressed() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEvent = new KeyEvent(component, 9, 1, ' ');
 simpleKeyListenerHelper.keyPressed(keyEvent);
 assertFalse(simpleKeyListener.isInvoked());

 simpleKeyListenerHelper.fire(9);
 assertTrue(simpleKeyListener.isInvoked());
 simpleKeyListener.reset();
 }

 @Test
 public void testKeyReleased() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.keyReleased(new KeyEvent(component, 1, 1, ' '));
 assertFalse(simpleKeyListener.isInvoked());
 }

 @Test
 public void testKeyTyped() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.keyTyped(new KeyEvent(component, 1, 1, ' '));
 assertFalse(simpleKeyListener.isInvoked());
 }

 private class SimpleKeyListener implements KeyListener {
 private boolean invoked = false;

 @Override
 public void enter(Component component) {
 // Do nothing
 }

 @Override
 public void insert(Component component) {
 // Do nothing
 }

 @Override
 public void delete(Component component) {
 // Do nothing
 }

 @Override
 public void tab(Component component) {
 // Do nothing
 }

 @Override
 public void keyPressed(KeyEvent e) {
 invoked = true;
 }

 @Override
 public void keyReleased(KeyEvent e) {
 // Do nothing
 }

 @Override
 public void keyTyped(KeyEvent e) {
 // Do nothing
 }

 public void reset() {
 invoked = false;
 }

 public boolean isInvoked() {
 return invoked;
 }
 }
}