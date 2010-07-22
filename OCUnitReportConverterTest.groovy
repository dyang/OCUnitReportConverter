class OCUnitReportConverterTest extends GroovyTestCase {
    
    def converter
    
    void setUp() {
        converter = new OCUnitReportConverter()
    }
    
    void testShouldParseTestSuites() {        
        def result = converter.parse(new File("data/output-f-2.log").text)
        assertEquals 2, result.numOfTestSuites
    }

}