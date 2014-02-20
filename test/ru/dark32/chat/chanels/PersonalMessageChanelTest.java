/**
 * 
 */
package ru.dark32.chat.chanels;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Andrew
 * 
 */
public class PersonalMessageChanelTest {
	PersonalMessageChanel	chanel	= new PersonalMessageChanel("PM");

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * Test method for
	 * {@link ru.dark32.chat.chanels.PersonalMessageChanel#hasNameTarget(java.lang.String)}
	 * .
	 */
	@Test
	public void testHasNameTarget() {
		assertEquals(false, chanel.hasNameTarget(""));
		assertEquals(false, chanel.hasNameTarget("@"));
		assertEquals(true, chanel.hasNameTarget("@dark32"));
		assertEquals(true, chanel.hasNameTarget("@dark32 test message"));
	}

	/**
	 * Test method for
	 * {@link ru.dark32.chat.chanels.PersonalMessageChanel#getNameTarget(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetNameTarget() {
		assertEquals("dark32", chanel.getNameTarget("@dark32"));
		assertEquals("dark32", chanel.getNameTarget("@dark32 test message"));
	}

	/**
	 * Test method for
	 * {@link ru.dark32.chat.chanels.PersonalMessageChanel#hasMessage(java.lang.String)}
	 * .
	 */
	@Test
	public void testHasMessage() {
		assertEquals(-1, chanel.hasMessage(""));
		assertEquals(-1, chanel.hasMessage("@"));
		assertEquals(-1, chanel.hasMessage("@dark32"));
		assertEquals(7, chanel.hasMessage("@dark32 test message"));
		assertEquals(5, chanel.hasMessage("@dark test message"));
	}

	/**
	 * Test method for
	 * {@link ru.dark32.chat.chanels.PersonalMessageChanel#getMessage(java.lang.String, int)}
	 * .
	 */
	@Test
	public void testGetMessage() {
		assertEquals("", chanel.getMessage("", -1));
		assertEquals("", chanel.getMessage("@", -1));
		assertEquals("", chanel.getMessage("@dark32", -1));
		assertEquals("test message", chanel.getMessage("@dark32 test message", 7));
		assertEquals("test message", chanel.getMessage("@dark test message", 5));
	}

}
