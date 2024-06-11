package com.mentorgen.tools.profile.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ProfileTest {

    private static Profile _profile;
    private static long _threadId1;
    private static long _threadId2;

    @BeforeClass
    public static void initProfiler() {
        Profile.initProfiler();
    }

    @Test
    public void testInitAndClear() {
        Profile.init();
        assertEquals(0, Profile.threads().size());
        Profile.clear();
        assertEquals(0, Profile.threads().size());
    }

    @Test
    public void testThreadInteractions() {
        _threadId1 = Thread.currentThread().getId();
        Profile.start("com.mentorgen.tools.profile.runtime.ProfileTest", "testThreadInteractions");
        Thread thread2 = new Thread(() -> Profile.start("com.mentorgen.tools.profile.runtime.ProfileTest", "testThreadInteractions"));
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Profile.end("com.mentorgen.tools.profile.runtime.ProfileTest", "testThreadInteractions");
        Profile.end("com.mentorgen.tools.profile.runtime.ProfileTest", "testThreadInteractions");
        List<Frame> interactions1 = Profile.interactions(_threadId1);
        List<Frame> interactions2 = Profile.interactions(_threadId2);
        assertEquals(1, interactions1.size());
        assertEquals(1, interactions2.size());
        Frame interaction1 = interactions1.get(0);
        Frame interaction2 = interactions2.get(0);
        assertEquals(_threadId1, interaction1.getThreadId());
        assertEquals(_threadId2, interaction2.getThreadId());
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.alloc("java.lang.String");
        Profile.alloc("java.util.ArrayList");
        List<ClassAllocation> allocations = Profile.allocations();
        assertEquals(2, allocations.size());
        ClassAllocation stringAllocation = null;
        ClassAllocation arrayListAllocation = null;
        for (ClassAllocation allocation : allocations) {
            if (allocation.getClassName().equals("java.lang.String")) {
                stringAllocation = allocation;
            } else if (allocation.getClassName().equals("java.util.ArrayList")) {
                arrayListAllocation = allocation;
            }
        }
        assertEquals(1, stringAllocation.getAllocationCount());
        assertEquals(1, arrayListAllocation.getAllocationCount());
    }

    @Test
    public void testStartAndStop() {
        _threadId1 = Thread.currentThread().getId();
        Profile.start("com.mentorgen.tools.profile.runtime.ProfileTest", "testStartAndStop");
        Thread.sleep(10);
        Profile.stop();
        List<Frame> frameList = Profile.frameList();
        assertEquals(1, frameList.size());
        Frame frame = frameList.get(0);
        assertEquals(_threadId1, frame.getThreadId());
    }

    @AfterClass
    public static void shutdown() {
        Profile.shutdown();
    }
}