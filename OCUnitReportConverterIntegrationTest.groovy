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
}