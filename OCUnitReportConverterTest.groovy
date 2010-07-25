class OCUnitReportConverterTest extends GroovyTestCase {
    
    def converter
    String outputTwoSuitesOneFail
	String outputTwoSuitesTwoFail
    
    void setUp() {
        converter = new OCUnitReportConverter()
        outputTwoSuitesOneFail = new File("data/output-f-1.log").text
        outputTwoSuitesTwoFail = new File("data/output-f-2.log").text
    }
       
    void testShouldParseTestSuiteName() {
        def result = converter.parse(outputTwoSuitesOneFail)
        assertEquals 2, result.testSuites.size
        assertEquals "DummyTests", result.testSuites[0].name
        assertEquals "SimpleTests", result.testSuites[1].name
    }
    
    void testShouldParseTestSuiteFailureCount() {
        def result = converter.parse(outputTwoSuitesOneFail)
        assertEquals 1, result.testSuites[0].numberOfFailures
        assertEquals 0, result.testSuites[1].numberOfFailures
    }

	void testShouldDefaultErrorCountToZeroUntilIFigureItOut() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals 0, result.testSuites[0].numberOfErrors
		assertEquals 0, result.testSuites[1].numberOfErrors
	}
	
	void testShouldParseTotalTestCount() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals 4, result.testSuites[0].numberOfTests
		assertEquals 2, result.testSuites[1].numberOfTests
	}
	
	void testShouldParseTimestamp() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals "2010-07-21 23:46:58 +0800", result.testSuites[0].timestamp
		assertEquals "2010-07-21 23:46:58 +0800", result.testSuites[1].timestamp
	}
	
	void testShouldParseExecutionTime() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals 0.001f, result.testSuites[0].time
		assertEquals 0.00f, result.testSuites[1].time
	}
	
	void testShouldParseTestCaseName() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals 4, result.testSuites[0].testCases.size
		assertEquals "testFail", result.testSuites[0].testCases[0].name
		assertEquals "testNumber", result.testSuites[0].testCases[1].name
		assertEquals "testString", result.testSuites[0].testCases[2].name
		assertEquals "testStringLength", result.testSuites[0].testCases[3].name
		
		assertEquals 2, result.testSuites[1].testCases.size
		assertEquals "testNumber", result.testSuites[1].testCases[0].name
		assertEquals "testString", result.testSuites[1].testCases[1].name
	}
	
	void testShouldParseTestCaseExecutionTime() {
		def result = converter.parse(outputTwoSuitesOneFail)
		assertEquals 0.000f, result.testSuites[0].testCases[0].time
		assertEquals 0.002f, result.testSuites[0].testCases[1].time
		assertEquals 0.001f, result.testSuites[0].testCases[2].time
		assertEquals 0.000f, result.testSuites[0].testCases[3].time

		assertEquals 0.010f, result.testSuites[1].testCases[0].time
		assertEquals 0.020f, result.testSuites[1].testCases[1].time
	}

	void testShouldParseTestFailures() {
		def result = converter.parse(outputTwoSuitesTwoFail)
		assertEquals false, result.testSuites[0].testCases[0].passed
		assertEquals true, result.testSuites[0].testCases[1].passed
		assertEquals true, result.testSuites[0].testCases[2].passed
		assertEquals true, result.testSuites[0].testCases[3].passed
		assertEquals false, result.testSuites[0].testCases[4].passed

		assertEquals true, result.testSuites[1].testCases[0].passed
		assertEquals true, result.testSuites[1].testCases[1].passed
	}

	void testShouldParseFailureMessage() {
		def result = converter.parse(outputTwoSuitesTwoFail)
		assertEquals 'testFail2', result.testSuites[0].testCases[0].name
		assertEquals '"FALSE" should be true. False is not true', result.testSuites[0].testCases[0].failure.message
		assertEquals 'testxFail1', result.testSuites[0].testCases[4].name
		assertEquals '"TRUE" should be false. True is not false', result.testSuites[0].testCases[4].failure.message
	}

	void testShouldParseFailureCallstack() {
		def result = converter.parse(outputTwoSuitesTwoFail)
		assertEquals '/Users/dyang/Workspace/TestApp/DummyTests.m:39', result.testSuites[0].testCases[0].failure.callstack
		assertEquals '/Users/dyang/Workspace/TestApp/DummyTests.m:34', result.testSuites[0].testCases[4].failure.callstack
	}
}