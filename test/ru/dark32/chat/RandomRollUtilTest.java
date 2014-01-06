package ru.dark32.chat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RandomRollUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testRandomRoll() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Util.randomRoll("Он подошёл к обрыву, *упал* и *встал*,"
					+ " пошёл дальше и снова *упал* и не *встал*, а может *не встал"));
			System.out.println(Util.randomRoll("Он подошёл к обрыву, *упал* и *встал*, пошёл дальше "));
			System.out.println(Util.randomRoll("Он подошёл к обрыву, *упал* и *встал*, пошёл*"));
			
		};
	}
}
