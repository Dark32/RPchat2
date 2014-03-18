package ru.dark32.chat.logger;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogAgentTest {
	private static LogAgent	log;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = new LogAgent("log.txt", "./");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void test() {
		log.warning("test warn");
		log.post("test post");
	}

}
