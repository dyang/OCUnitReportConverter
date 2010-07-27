class OCUnitReportConverterIntegrationTest extends GroovyTestCase {

    def converter
	String outputTwoSuitesTwoFail

    void setUp() {
        converter = new OCUnitReportConverter()
        outputTwoSuitesTwoFail = new File("data/output-f-2.log").text

		def reportDir = new File("test-reports")
		if (reportDir.exists())
			reportDir.deleteDir()
    }

	void testShouldCreateAReportForEachTestSuite() {
		converter.parse(outputTwoSuitesTwoFail).toXml()

		assertTrue "[TEST-DummyTests.xml] should be created", new File("test-reports", "TEST-DummyTests.xml").exists()
		assertTrue "[TEST-SimpleTests.xml] should be created", new File("test-reports", "TEST-SimpleTests.xml").exists()
	}

	void testShouldWriteTestSuite() {
		converter.parse(outputTwoSuitesTwoFail).toXml()

		def testSuite = new XmlParser().parse("test-reports/TEST-DummyTests.xml")
		assertEquals 5, testSuite.@tests as int
		assertEquals 0, testSuite.@errors.toInteger()
		assertEquals 2, testSuite.@failures.toInteger()
		assertEquals "DummyTests", testSuite.@name
		assertEquals 0.001f, testSuite.@time as float
		assertEquals "2010-07-25 15:45:56 +0800", testSuite.@timestamp
	}

	void testShouldWriteTestCases() {
		converter.parse(outputTwoSuitesTwoFail).toXml()

		def testSuite = new XmlParser().parse("test-reports/TEST-DummyTests.xml")
		assertEquals 5, testSuite.testcase.size()
		testSuite.testcase.each {
			assertEquals "DummyTests", it.@classname
			assertEquals 0.000f, it.@time as float
		}

		assertEquals "testFail2", testSuite.testcase[0].@name
		assertEquals '"FALSE" should be true. False is not true', testSuite.testcase[0].failure[0].@message
		assertEquals "failure", testSuite.testcase[0].failure[0].@type
		assertEquals "/Users/dyang/Workspace/TestApp/DummyTests.m:39", testSuite.testcase[0].failure[0].text()

		assertEquals "testNumber", testSuite.testcase[1].@name
		assertEquals "testString", testSuite.testcase[2].@name
		assertEquals "testStringLength", testSuite.testcase[3].@name
		assertEquals "testxFail1", testSuite.testcase[4].@name
		assertEquals '"TRUE" should be false. True is not false', testSuite.testcase[4].failure[0].@message
		assertEquals "failure", testSuite.testcase[4].failure[0].@type
		assertEquals "/Users/dyang/Workspace/TestApp/DummyTests.m:34", testSuite.testcase[4].failure[0].text()
	}
}