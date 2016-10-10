package com.myseriousorganization.application;

import org.junit.Test;

import java.io.File;

/**
 * Created by asfernando on 10/10/16.
 */
public class SessionTimeComputerTest {

	@Test
	public void test() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File input = new File(classLoader.getResource("inputFile1.txt").getFile());
		SessionTimeComputer stc = new SessionTimeComputer(input, "/tmp/output1.txt");
	}
}
