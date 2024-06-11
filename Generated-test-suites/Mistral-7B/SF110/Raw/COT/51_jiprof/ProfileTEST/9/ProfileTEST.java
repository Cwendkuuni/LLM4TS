package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import com.mentorgen.tools.profile.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;
import static java.util.Arrays.asList;

public class ProfileTest {

    static Profile profile;

    @BeforeClass
    public static void setupClass() {
        Profile.initProfiler(); // Initialize the profiler
        profile = new Profile();
    }

    @Test
    public void testInitProfiler() {
        // Check if profiler is initialized
        assertTrue(Profile._controller != null);
    }

    @Test
    public void testInit() {
        // Check if data structures are initialized
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());

        Profile.init(); // Initialize the data structures

        // Check if data structures are initialized
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testClearWhileProfiling() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Try to clear while profiling
        Profile.clear();

        // Check if an exception is thrown
        fail("Expected an IllegalStateException, but no exception was thrown.");
    }

    @Test
    public void testStart() {
        // Start profiling
        Profile.start();

        // Check if profiling is started
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        // Start profiling
        Profile.start();

        // Wait for some time to let the profiler collect data
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop profiling
        Profile.stop();

        // Check if profiling is stopped
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        // Set the output file name
        Profile.setFileName("output.txt");

        // Check if the file name is set correctly
        assertEquals("output.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutDown() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Wait for some time to let the profiler collect data
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shut down the profiler
        Profile.shutdown();

        // Check if the data is dumped to the output file
        try {
            InputStream inputStream = ProfileDump.class.getResourceAsStream("/output.txt");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            String output = new String(outputStream.toByteArray());
            // Add your assertions here to check the contents of the output file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThreads() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Wait for some threads to be created
        Thread thread1 = new Thread(() -> {});
        Thread thread2 = new Thread(() -> {});
        thread1.start();
        thread2.start();

        // Get the list of threads and check its size
        List<Long> threads = Profile.threads();
        assertEquals(2, threads.size());
        assertTrue(threads.containsAll(asList(thread1.getId(), thread2.getId())));
    }

    @Test
    public void testInteractions() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Create some interactions
        String className1 = "Class1";
        String methodName1 = "method1";
        String className2 = "Class2";
        String methodName2 = "method2";
        Profile.start(className1, methodName1);
        Profile.end(className1, methodName1);
        Profile.start(className2, methodName2);
        Profile.end(className2, methodName2);

        // Get the interactions of a thread and check its size
        Long threadId = Thread.currentThread().getId();
        List<Frame> interactions = Profile.interactions(threadId);
        assertEquals(2, interactions.size());
    }

    @Test
    public void testFrameList() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Create some frames
        String className1 = "Class1";
        String methodName1 = "method1";
        String className2 = "Class2";
        String methodName2 = "method2";
        Profile.start(className1, methodName1);
        Profile.end(className1, methodName1);
        Profile.start(className2, methodName2);
        Profile.end(className2, methodName2);

        // Get the list of frames and check its size
        List<Frame> frameList = Profile.frameList();
        assertEquals(2, frameList.size());
    }

    @Test
    public void testAllocations() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Allocate some objects of different classes
        String className1 = "Class1";
        String className2 = "Class2";
        Profile.alloc(className1);
        Profile.alloc(className1);
        Profile.alloc(className2);
        Profile.alloc(className2);

        // Get the list of allocations and check its size
        List<ClassAllocation> allocations = new ArrayList<>(Profile._allocList.values());
        assertEquals(4, allocations.size());
        assertEquals(2, allocations.get(0).getAllocCount());
        assertEquals(2, allocations.get(1).getAllocCount());
        assertEquals(2, allocations.get(2).getAllocCount());
        assertEquals(2, allocations.get(3).getAllocCount());
    }

    @Test
    public void testGetThreadTotalTime() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Sleep for some time to let the profiler collect data for a thread
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        // Get the total time spent by the thread and check its value
        Long threadId = thread.getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Create some frames in random order
        String className1 = "Class1";
        String methodName1 = "method1";
        String className2 = "Class2";
        String methodName2 = "method2";
        Profile.start(className2, methodName2);
        Profile.end(className2, methodName2);
        Profile.start(className1, methodName1);
        Profile.end(className1, methodName1);

        // Sort the frame list and check if the sorted list is in the correct order
        Profile.sortFrameList((f1, f2) -> f1.getBeginTime() < f2.getBeginTime() ? -1 : (f1.getBeginTime() > f2.getBeginTime() ? 1 : 0));
        Frame frame1 = Profile.frameList().get(0);
        Frame frame2 = Profile.frameList().get(1);
        assertTrue(frame1.getBeginTime() < frame2.getBeginTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullClassName() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Try to start with null class name and check if an exception is thrown
        Profile.start(null, "method1");

        // Check if an exception is thrown
        fail("Expected an IllegalArgumentException, but no exception was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartNullMethodName() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Try to start with null method name and check if an exception is thrown
        Profile.start("Class1", null);

        // Check if an exception is thrown
        fail("Expected an IllegalArgumentException, but no exception was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndNullClassName() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Create a frame
        String className = "Class1";
        String methodName = "method1";
        Profile.start(className, methodName);

        // Try to end with null class name and check if an exception is thrown
        Profile.end(null, methodName);

        // Check if an exception is thrown
        fail("Expected an IllegalArgumentException, but no exception was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndNullMethodName() {
        // Start profiling
        Profile.initProfiler();
        Profile.start();

        // Create a frame
        String className = "Class1";
        String methodName = "method1";
        Profile.start(className, methodName);

        // Try to end with null method name and check if an exception is thrown
        Profile.end(className, null);

        // Check if an exception is thrown
        fail("Expected an IllegalArgumentException, but no exception was thrown.");
    }
}