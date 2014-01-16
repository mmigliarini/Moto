package com.moto.testing;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JUnitTesting {

	public static Test suite() {
		TestSuite suite = new TestSuite(JUnitTesting.class.getName());
		//$JUnit-BEGIN$
		
		suite.addTestSuite(JUnitTestingLoginRegistration.class);
		suite.addTestSuite(JUnitTestingMovieShow.class);
		
		//$JUnit-END$
		return suite;
	}

}