package Newzgrabber;

import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.junit.runners.BlockJUnit4ClassRunner;import java.util.Hashtable;import static org.junit.Assert.assertArrayEquals;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class) public class BatchDriverTest { private static final String NEWZINI = "test.ini"; private static final String BATCHFILE = "test.ini"; private BatchDriver batchDriver;

@Before public void setUp() throws IOException, NullPointerException { batchDriver = new BatchDriver(NEWZINI, BATCHFILE); }

@Test public void testGetGroups() { String[] groups = batchDriver.getGroups(); Assert.assertNotEquals(0, groups.length); assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups); }

@Test public void testGetBatchJob() { String group = "group1"; BatchJob batchJob = batchDriver.getBatchJob(group);

assertNull(batchJob);

group = "group2"; batchJob = batchDriver.getBatchJob(group);

assertNotNull(batchJob); assertEquals("server1", batchJob.getServer()); assertEquals("path/to/directory1", batchJob.getDirectory()); assertEquals(1, batchJob.getFirstId()); assertEquals(10, batchJob.getLastId()); }

@Test public void testGetSubjectLines() { String[] subjectLines = batchDriver.getSubjectLines(); assertNotNull(subjectLines); assertArrayEquals(new String[]{"Subject 1", "Subject 2", "Subject 3"}, subjectLines); }

@Test(expected = ArrayIndexOutOfBoundsException.class) public void testGetGroupsWithEmptyVector() { batchDriver = new BatchDriver(NEWZINI, BATCHFILE); String[] groups = batchDriver.getGroups(); groups = new String[0]; batchDriver.BatchgroupsVector.addAll(Arrays.asList(groups)); assertArrayEquals(new String[]{}, batchDriver.getGroups()); }
}