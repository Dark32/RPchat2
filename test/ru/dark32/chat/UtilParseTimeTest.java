package ru.dark32.chat;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilParseTimeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void timeParseTest() {
		assertEquals(Util.day + 2 * Util.hour + 3 * Util.minute + 4, Util.timeParse("1d2h3m4s"));
		assertEquals(10 * Util.day + 10 * Util.hour + 10 * Util.minute + 10, Util.timeParse("10d10h10m10s"));
		assertEquals(1, Util.timeParse("1s"));
		assertEquals(Util.minute, Util.timeParse("1m"));
		assertEquals(Util.minute + 1, Util.timeParse("1m1s"));
		assertEquals(Util.hour, Util.timeParse("1h"));
		assertEquals(Util.hour + 1, Util.timeParse("1h1s"));
		assertEquals(Util.hour + Util.minute, Util.timeParse("1h1m"));
		assertEquals(Util.hour + Util.minute + 1, Util.timeParse("1h1m1s"));
		assertEquals(Util.day, Util.timeParse("1d"));
		assertEquals(Util.day + 1, Util.timeParse("1d1s"));
		assertEquals(Util.day + Util.minute, Util.timeParse("1d1m"));
		assertEquals(Util.day + Util.minute + 1, Util.timeParse("1d1m1s"));
		assertEquals(Util.day + Util.hour, Util.timeParse("1h1d"));
		assertEquals(Util.day + Util.hour + 1, Util.timeParse("1d1h1s"));
		assertEquals(Util.day + Util.hour + Util.minute + 1, Util.timeParse("1d1h1m1s"));
		assertEquals(Util.time_inf, Util.timeParse("inf"));
		assertEquals(0, Util.timeParse("hahahaha"));
		assertEquals(4*Util.hour, Util.timeParse("1h1h1h1h1"));
	}
}
