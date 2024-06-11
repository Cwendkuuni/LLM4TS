package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component component;

 @Before
 public void setUp() {
 simpleKeyListener = mock(SimpleKeyListener.class);
 component = mock(Component.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @After
 public void tearDown() {
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testKeyListenerAttachment() {
 verify(component, times(1)).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 verify(component, times(1)).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(simpleKeyListener, times(1)).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 verify(simpleKeyListener, times(1)).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
 verify(simpleKeyListener, times(1)).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 verify(simpleKeyListener, times(1)).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_ENTER, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(simpleKeyListener, times(1)).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.VK_TAB, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 verify(simpleKeyListener, times(1)).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_DELETE, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
 verify(simpleKeyListener, times(1)).delete(component);
 }

 @Test
 public void testKeyPressedBackSpace() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_BACK_SPACE, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 verify(simpleKeyListener, times(1)).insert(component);
 }
}