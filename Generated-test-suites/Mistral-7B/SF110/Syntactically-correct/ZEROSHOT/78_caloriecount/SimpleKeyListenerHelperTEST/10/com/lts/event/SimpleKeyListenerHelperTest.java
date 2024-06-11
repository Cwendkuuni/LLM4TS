package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.awt.event.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 @Mock
 private SimpleKeyListener simpleKeyListener;

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testComponentAddKeyListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 assertTrue(simpleKeyListenerHelper.getComponent().isFocusable());
 }

 @Test
 public void testComponentRemoveKeyListener() {
 simpleKeylistenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 MockitoAnnotations.initMocks(this);
 simpleKeyListenerHelper.setListener(simpleKeyListener);
 simpleKeyListener.enter = mock(Action.class);
 KeyEvent keyEventEnter = new KeyEvent(new Container(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '');
 simpleKeyListenerHelper.keyPressed(keyEventEnter);
 simpleKeyListener.enter.invoke();
 verify(simpleKeyListener.enter).invoke();
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 MockitoAnnotations.initMocks(this);
 simpleKeyListenerHelper.setListener(simpleKeyListener);
 simpleKeyListener.tab = mock(Action.class);
 KeyEvent keyEventTab = new KeyEvent(new Container(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, '');
 simpleKeyListenerHelper.keyPressed(keyEventTab);
 simpleKeyListener.tab.invoke();
 verify(simpleKeyListener.tab).invoke();
 }

 @Test
 public void testKeyPressedBackspace() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 MockitoAnnotations.initMocks(this);
 simpleKeyListenerHelper.setListener(simpleKeyListener);
 simpleKeyListener.delete = mock(Action.class);
 KeyEvent keyEventBackspace = new KeyEvent(new Container(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, '');
 simpleKeyListenerHelper.keyPressed(keyEventBackspace);
 simpleKeyListener.delete.invoke();
 verify(simpleKeyListener.delete).invoke();
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 MockitoAnnotations.initMocks(this);
 simpleKeyListenerHelper.setListener(simpleKeyListener);
 simpleKeyListener.delete = mock(Action.class);
 KeyEvent keyEventDelete = new KeyEvent(new Container(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, '');
 simpleKeyListenerHelper.keyPressed(keyEventDelete);
 simpleKeyListener.delete.invoke();
 verify(simpleKeyListener.delete).invoke();
 }

 @Test
 public void testKeyPressedInsert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 MockitoAnnotations.initMocks(this);
 simpleKeyListenerHelper.setListener(simpleKeyListener);
 simpleKeyListener.insert = mock(Action.class);
 KeyEvent keyEventInsert = new KeyEvent(new Container(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, '');
 simpleKeyListenerHelper.keyPressed(keyEventInsert);
 simpleKeyListener.insert.invoke();
 verify(simpleKeyListener.insert).invoke();
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testNullComponent() {
 assertThrows(NullPointerException.class, () -> new SimpleKeyListenerHelper(null));
 }

 private Component getComponent() {
 return simpleKeyListenerHelper.getComponent();
 }
}