package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint ce1, ce2;
 private Entry entry1, entry2;
 private Hero hero1;

 @Before
 public void setUp() {
 handler = new Handler();
 ce1 = new ConnectionEndpoint(1, "localhost", 1234);
 ce2 = new ConnectionEndpoint(2, "localhost", 5678);
 entry1 = new Entry();
 entry1.ce = ce1;
 entry1.name = "";
 entry2 = new Entry();
 entry2.ce = ce2;
 entry2.name = "";
 hero1 = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 assertEquals(2, handler.clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionException() {
 handler.addConnection(ce1);
 handler.addConnection(ce1);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.removeConnection(ce1);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRemoveConnectionNotFound() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.removeConnection(ce3);
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testHandleInputStringCommand() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new String("/message TestMessage")));
 String expected = "*TestHero: TestMessage";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce.getClientPort() == ce2.getClientPort()) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testHandleInputStringSystemMessage() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, new String("/system TestSystemMessage")));
 String expected = "TestSystemMessage";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce.getClientPort() == ce1.getClientPort()) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testHandleInputChallenge() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new Challenge(1, "TestTarget", false, null)));
 String expected = "TestTarget rolled a(n) 1";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce.getClientPort() == ce2.getClientPort()) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testHandleInputChallengeSilent() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new Challenge(1, null, true, null)));
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testHandleInputChallReq() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new ChallReq("TestTarget", "TestMessage")));
 String expected = "TestMessage";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).name.equals("TestTarget")) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testHandleInputHeroUpdate() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, hero1));
 String expected = "TestHero";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).hero != null && handler.clients.elementAt(i).hero.getName().equals(expected)) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testHandleInputGmConnection() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, new UserEvent(true, "Gamemaster")));
 String expected = "Gamemaster";
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).name.equals(expected)) {
 assertEquals(expected, handler.clients.elementAt(i).name);
 break;
 }
 }
 }

 @Test
 public void testSendHeros() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, new Hero("TestHero")));
 handler.handleInput(new InputEvent(ce1, new Hero("TestHero2")));
 Vector<Hero> heroes = handler.sendHeros(ce2);
 assertEquals(2, heroes.size());
 }
}