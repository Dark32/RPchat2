package ru.dark32.chat;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParseUTF8UtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testParseUTF8() {
		System.out.println(Util.parseUTF8("--\\u0123--"));
		System.out.println(Util.parseUTF8("--\\u4567--"));
		System.out.println(Util.parseUTF8("--\\u89ab--"));
		System.out.println(Util.parseUTF8("--\\ucdef--"));
		System.out.println(Util.parseUTF8("e-\\u0ttb-e"));
		System.out.println(Util.parseUTF8("--\\u26E7--"));
		System.out.println(Util.parseUTF8("--\\u2764--"));
		System.out.println(Util.parseUTF8("--\\u2701--"));
		System.out.println(Util.parseUTF8("--\\u27ff--"));
	}

}
