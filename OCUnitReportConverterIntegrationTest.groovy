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

	void testShouldWriteOCUnitResultIntoJUnitReport() {
		converter.parse(outputTwoSuitesTwoFail).toXml()

		def testSuite = new XmlParser().parse("test-reports/TEST-DummyTests.xml")
		assertEquals 5, testSuite.@tests as int
		assertEquals 0, testSuite.@errors.toInteger()
		assertEquals 2, testSuite.@failures.toInteger()
		assertEquals "DummyTests", testSuite.@name
		assertEquals 0.001f, testSuite.@time as float
		assertEquals "2010-07-25 15:45:56 +0800", testSuite.@timestamp
	}
}