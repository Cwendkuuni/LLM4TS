package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BasePeriodTest {

 @Test
 public void testConstructionFromValues() {
 PeriodType pt = PeriodType.standard();
 BasePeriod bp = new BasePeriod(pt, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 assertEquals(pt, bp.getPeriodType());
 assertEquals(1, bp.getValue(DurationFieldType.years()));
 assertEquals(2, bp.getValue(DurationFieldType.months()));
 // ... check remaining fields
 }

 @Test
 public void testConstructionFromInstant() {
 PeriodType pt = PeriodType.standard();
 Instant instant = new DateTime(2012, 1, 1, 0, 0, 0, 0).toInstant();
 BasePeriod bp = new BasePeriod(pt, instant, Instant.now(DateTimeUtils.UTC));
 assertEquals(pt, bp.getPeriodType());
 // ... check values obtained from instant
 }

 @Test
 public void testConstructionFromReadableInstant() {
 PeriodType pt = PeriodType.standard();
 ReadableInstant ri1 = new DateTime(2012, 1, 1, 0, 0, 0, 0).toReadableInstant();
 ReadableInstant ri2 = new DateTime(2012, 1, 2, 0, 0, 0, 0).toReadableInstant();
 BasePeriod bp = new BasePeriod(pt, ri1, ri2);
 assertEquals(pt, bp.getPeriodType());
 // ... check values obtained from interval between instants
 }

 @Test
 public void testConstructionFromReadablePartial() {
 PeriodType pt = PeriodType.standard();
 BaseLocal dl1 = new DateTime(2012, 1, 1, 0, 0, 0, 0).toLocalDateTime().toBaseLocal();
 BaseLocal dl2 = new DateTime(2012, 1, 2, 0, 0, 0, 0).toLocalDateTime().toBaseLocal();
 BasePeriod bp = new BasePeriod(pt, dl1, dl2);
 assertEquals(pt, bp.getPeriodType());
 // ... check values obtained from interval between locals
 }

 @Test
 public void testConstructionFromReadableDurationAndReadableInstant() {
 PeriodType pt = PeriodType.standard();
 ReadableDuration rd = Duration.days(3);
 ReadableInstant ri = new DateTime(2012, 1, 1, 0, 0, 0, 0).toReadableInstant();
 BasePeriod bp = new BasePeriod(pt, rd, ri);
 assertEquals(pt, bp.getPeriodType());
 // ... check values obtained from duration and instant
 }

 @Test
 public void testConstructionFromReadableDurationAndReadableInstantReversed() {
 PeriodType pt = PeriodType.standard();
 ReadableDuration rd = Duration.days(3);
 ReadableInstant ri = new DateTime(2012, 1, 1, 0, 0, 0, 0).toReadableInstant();
 BasePeriod bp = new BasePeriod(pt, ri, rd);
 assertEquals(pt, bp.getPeriodType());
 // ... check values obtained from instant and duration are reversed
 }

 @Test
 public void testAddingPeriods() {
 PeriodType pt = PeriodType.standard();
 BasePeriod bp1 = new BasePeriod(pt, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 BasePeriod bp2 = new BasePeriod(pt, new int[] {3, 5, 7, 2, 1, 6, 5, 1});
 BasePeriod bpSum = new BasePeriod(pt);
 bpSum.addPeriod(bp1);
 bpSum.addPeriod(bp2);
 assertArrayEquals(new int[] {4, 7, 10, 6, 7, 12, 12, 9}, bpSum.getValues());
 }

 @Test
 public void testAddingReadablePeriod() {
 PeriodType pt = PeriodType.standard();
 BasePeriod bp1 = new BasePeriod(pt, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 ReadablePeriod rp = new BasePeriod(pt, new int[] {3, 5, 7, 2, 1, 6, 5, 1});
 BasePeriod bpSum = new BasePeriod(pt);
 bpSum.addPeriod(bp1);
 bpSum.addPeriod(rp);
 assertArrayEquals(new int[] {4, 7, 10, 6, 7, 12, 12, 9}, bpSum.getValues());
 }

 @Test
 public void testMergingPeriods() {
 PeriodType pt = PeriodType.standard();
 BasePeriod bp1 = new BasePeriod(pt, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 BasePeriod bp2 = new BasePeriod(pt, new int[] {3, 5, 7, 2, 1, 6, 5, 1});
 BasePeriod bpMerged = new BasePeriod(pt);
 bpMerged.mergePeriod(bp1);
 bpMerged.mergePeriod(bp2);
 assertArrayEquals(new int[] {4, 5, 6, 6, 7, 12, 12, 9}, bpMerged.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncorrectConstructionFromReadablePartial() {
 PeriodType pt = PeriodType.standard();
 BaseLocal dl1 = new DateTime(2012, 1, 1, 0, 0, 0, 0).toLocalDateTime().toBaseLocal();
 BaseLocal dl2 = new DateTime(2012, 1, 2, 12, 0, 0, 0).toLocalDateTime().toBaseLocal(); // wrong chronology
 BasePeriod bp = new BasePeriod(pt, dl1, dl2);
 }
}