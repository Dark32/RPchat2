package ru.dark32.chat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilSuffixLatterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void suffixLatterTest() {
		System.out.println(Util.suffixLatter("$(1|2|over 9000|1)-1"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|2)-2"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|4)-4"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|5)-5"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|6)-6"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|10)-10"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|11)-11"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|12)-12"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|19)-19"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|20)-20"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|21)-21"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|22)-22"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|23)-23"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|24)-24"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|25)-25"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|29)-29"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|100)-100"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|101)-101"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|104)-104"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|105)-105"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|109)-109"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|111)-111"));
		System.out.println(Util.suffixLatter("$(1|2|over 9000|no_parse)-not"));
	}

}
