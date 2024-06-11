package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setUp() {
 component = mock(Component.class);
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @After
 public void tearDown() {
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testConstructor() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testAddKeyListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.component.addKeyListener(simpleKeyListener);
 verify(component).addKeyListener(simpleKeyListener);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListener_ENTER_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListener_INSERT_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListener_DELETE_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListener_TAB_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_Tab);
 verify(simpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressed_ENTER_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEventEnter = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.KEYCODE_ENTER, "ENTER");
 simpleKeyListenerHelper.keyPressed(keyEventEnter);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressed_INSERT_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEventInsert = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.KEYCODE_INSERT, "INSERT");
 simpleKeyListenerHelper.keyPressed(keyEventInsert);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testKeyPressed_DELETE_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEventDelete = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.KEYCODE_DELETE, "DELETE");
 simpleKeyListenerHelper.keyPressed(keyEventDelete);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressed_TAB_KEY() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 KeyEvent keyEventTab = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.KEYCODE_Tab, "TAB");
 simpleKeyListenerHelper.keyPressed(keyEventTab);
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_Tab);
 verify(simpleKeyListener).tab(component);
 }
}