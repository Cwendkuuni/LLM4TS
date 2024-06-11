import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.Profile;

public class ProfileTest {
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        assertTrue(Profile._controllerThread != null);
    }

    @Test(expected = Exception.class)
    public void testInitProfilerException() {
        Profile.initProfiler();
        Profile.initProfiler(); // This should throw an exception
    }

    @Test
    public void testInit() {
        Profile.init();
        assertEquals(1001, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testThreads() {
        Profile.init();
        Profile.start();
        List<Long> threads = Profile.threads();
        assertTrue(threads.size() > 0);
    }

    @Test
    public void testInteractions() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        List<Frame> interactions = Profile.interactions(threadId);
        assertTrue(interactions.size() > 0);
    }

    @Test
    public void testFrameList() {
        Profile.init();
        Profile.start();
        List<Frame> frameList = Profile.frameList();
        assertTrue(frameList.size() > 0);
    }

    @Test
    public void testAllocations() {
        Profile.init();
        Profile.start();
        String className = "com.mentorgen.tools.profile.runtime.Profile";
        Iterable<ClassAllocation> allocations = Profile.allocations();
        ClassAllocation ca = null;
        for (Object obj : allocations) {
            ca = (ClassAllocation) obj;
            assertEquals(className, ca.getClassName());
            break;
        }
        assertTrue(ca != null);
    }

    @Test
    public void testGetThreadTotalTime() {
        Profile.init();
        Profile.start();
        long threadId = Thread.currentThread().getId();
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        assertTrue(threadTotalTime > 0);
    }

    @Test
    public void testSortFrameList() {
        Profile.init();
        Profile.start();
        Frame frame1 = new Frame(null, null, 1L);
        Frame frame2 = new Frame(null, null, 2L);
        Frame frame3 = new Frame(null, null, 1L);
        Profile._frameList.add(frame1);
        Profile._frameList.add(frame3);
        Profile._frameList.add(frame2);
        Comparator<Frame> comp = new Comparator<Frame>() {
            @Override
            public int compare(Frame o1, Frame o2) {
                return Long.compare(o1.getBeginTime(), o2.getBeginTime());
            }
        };
        Profile.sortFrameList(comp);
        Frame sortedFrame1 = Profile._frameList.get(0);
        Frame sortedFrame2 = Profile._frameList.get(1);
        Frame sortedFrame3 = Profile._frameList.get(2);
        assertEquals(sortedFrame1.getBeginTime(), frame1.getBeginTime());
        assertEquals(sortedFrame2.getBeginTime(), frame2.getBeginTime());
        assertEquals(sortedFrame3.getBeginTime(), frame3.getBeginTime());
    }

    @Test
    public void testStart() {
        Profile.init();
        Profile.start();
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.init();
        Profile.start();
        Profile.stop();
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        Profile.init();
        String fileName = "output.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.init();
        Profile.start();
        Profile.shutdown();
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
    }

    @Test
    public void testStartEnd() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        // Perform some actions in the method
        Profile.end(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.isMeasured());
    }

    @Test
    public void testBeginWaitEndWait() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        // Perform some actions that might involve waiting
        Profile.endWait(className, methodName);
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.isMeasured());
    }

    @Test
    public void testUnwind() {
        Profile.init();
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";
        Profile.start(className, methodName);
        try {
            throw new TestException();
        } catch (TestException e) {
            Profile.unwind(className, methodName, exception);
        }
        Frame frame = findFrame(className, methodName);
        assertTrue(frame != null);
        assertTrue(frame.isExceptionThrown());
        assertEquals(exception, frame.getException());
    }

    @Test
    public void testAlloc() {
        Profile.init();
        String className = "TestClass";
        Profile.start();
        Profile.alloc(className);
        ClassAllocation ca = findClassAllocation(className);
        assertEquals(1, ca.getAllocCount());
    }

    private Frame findFrame(String className, String methodName) {
        Frame frame = null;
        for (Frame f : Profile._frameList) {
            if (f.getClassName().equals(className) && f.getMethodName().equals(methodName)) {
                frame = f;
                break;
            }
        }
        return frame;
    }

    private ClassAllocation findClassAllocation(String className) {
        for (Object obj : Profile._allocList.values()) {
            ClassAllocation ca = (ClassAllocation) obj;
            if (ca.getClassName().equals(className)) {
                return ca;
            }
        }
        return null;
    }
}