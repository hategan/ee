package ee.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import ee.common.EntanglementEntropyTest;
import ee.common.EntropyTest;
import ee.common.SimpleDensityMatrixTest;
import ee.z2.common.SpinsStateTest;

public class RunTests {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(
				SimpleDensityMatrixTest.class,
				EntanglementEntropyTest.class,
				EntropyTest.class,
				SpinsStateTest.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		if (result.wasSuccessful()) {
			System.out.println("All tests passed");
		}
	}
}
