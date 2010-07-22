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
}