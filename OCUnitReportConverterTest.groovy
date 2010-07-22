class OCUnitReportConverterTest extends GroovyTestCase {
    
    def converter
    String outputTwoSuitesOneFail
    
    void setUp() {
        converter = new OCUnitReportConverter()
        outputTwoSuitesOneFail = new File("data/output-f-2.log").text
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
}