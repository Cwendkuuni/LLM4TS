package com.mentorgen.tools.profile.runtime;

import com.mentorgen.tools.profile.Controller;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProfileTest {

private Controller _controller;

@Before public void setUp() { _controller = mock(Controller.class); }

@Test public void testInitProfiler() { Profile.initProfiler(); assertTrue(Profile._controllerThread.isAlive()); assertFalse(Profile._controllerThread.isInterrupted()); Profile._controller = _controller; verify(Profile._controller).start(); }

@Test public void testInit() { Profile.init(); assertEquals(1001, Profile._threadDictionary.size()); assertEquals(2003, Profile._methodDictionary.size()); assertEquals(1001, Profile._frameList.size()); assertEquals(0, Profile._threadActiveFrame.size()); }

@Test public void testStart() { Profile.init(); Profile._controller = _controller; Profile.start(); verify(Profile._controller).start(); }

}