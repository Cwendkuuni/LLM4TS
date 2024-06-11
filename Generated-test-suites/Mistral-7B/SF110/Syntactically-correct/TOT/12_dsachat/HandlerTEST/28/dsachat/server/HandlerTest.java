package dsachat.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallengeReq;
import dsachat.share.hero.HeroFactory;

import static org.junit.Assert.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockCon;
 private InputStream mockInput;
 private OutputStream mockOutput;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 mockCon = mock(ConnectionEndpoint.class);
 clients = new Vector<>();
 }

 @Test
 public void testAddConnection() {
 // Given
 mockInput = new ByteArrayInputStream("connection data".getBytes());
 mockOutput = new ByteArrayOutputStream();
 when(mockCon.getClientPort()).thenReturn(1);
 when(mockCon.isConnected()).thenReturn(false);
 when(mockCon.send(anyObject())).thenReturn(true);

 // When
 handler.addConnection(mockCon);

 // Then
 assertEquals(1, clients.size());
 verify(mockCon).send(anyObject());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddConnectionWithNullConnection() {
 // Given
 when(mockCon.getClientPort()).thenReturn(null);

 // When
 handler.addConnection(mockCon);

 // Then
 throwException.expect(IllegalArgumentException.class);
 }

 @Test(expected = IOException.class)
 public void testAddConnectionWithIOException() {
 // Given
 when(mockCon.getClientPort()).thenReturn(1);
 when(mockCon.isConnected()).thenThrow(new IOException());

 // When
 handler.addConnection(mockCon);

 // Then
 verify(mockCon).send(anyObject()); // Not called
 throwException.expect(IOException.class);
 }

 // ... (Continue with similar tests for other methods)

 @After
 public void tearDown() {
 handler = null;
 mockCon = null;
 mockInput = null;
 mockOutput = null;
 clients = null;
 }
}