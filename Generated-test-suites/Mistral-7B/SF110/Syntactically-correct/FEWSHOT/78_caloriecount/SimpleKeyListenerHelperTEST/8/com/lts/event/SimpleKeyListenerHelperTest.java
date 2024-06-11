package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private Component testComponent;
 private SimpleKeyListener listener;
 private SimpleKeyListenerHelper keyHelper;

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 testComponent = new JFrame();
 listener = new SimpleKeyListener() {
 @Override
 public void enter(Component comp) {
 // implementation
 }

 @Override
 public void insert(Component comp) {
 // implementation
 }

 @Override
 public void delete(Component comp) {
 // implementation
 }

 @Override
 public void tab(Component comp) {
 // implementation
 }
 };
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 assertNotNull(keyHelper);
 assertSame(testComponent, keyHelper.component);
 }

 @Test
 public void testDetach() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 testComponent.removeKeyListener(keyHelper);
 assertNull(keyHelper.component);
 }

 @Test
 public void testNotifyListener() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 listener = new SimpleKeyListener() {
 @Override
 public void enter(Component comp) {
 fail("should not be called");
 }

 @Override
 public void insert(Component comp) {
 fail("should not be called");
 }

 @Override
 public void delete(Component comp) {
 fail("should not be called");
 }

 @Override
 public void tab(Component comp) {
 fail("should not be called");
 }

 @Override
 public void keyPressed(KeyEvent e) {
 assertEquals(10, e.getKeyCode()); // enter key
 keyHelper.notifyListener(listener, 10, null);
 }
 };
 keyHelper.fire(10);
 }

 @Test
 public void testKeyPressedEnter() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 listener = new SimpleKeyListener() {
 @Override
 public void enter(Component comp) {
 assertEquals(testComponent, comp);
 }
 };
 keyHelper.fire(10);
 }

 @Test
 public void testKeyPressedTab() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 listener = new SimpleKeyListener() {
 @Override
 public void tab(Component comp) {
 assertEquals(testComponent, comp);
 }
 };
 keyHelper.fire(9);
 }

 @Test
 public void testKeyPressedDelete() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 listener = new SimpleKeyListener() {
 @Override
 public void delete(Component comp) {
 assertEquals(testComponent, comp);
 }
 };
 keyHelper.fire(127);
 }

 @Test
 public void testKeyPressedInsert() {
 keyHelper = new SimpleKeyListenerHelper(testComponent);
 listener = new SimpleKeyListener() {
 @Override
 public void insert(Component comp) {
 assertEquals(testComponent, comp);
 }
 };
 keyHelper.fire(155);
 }
}