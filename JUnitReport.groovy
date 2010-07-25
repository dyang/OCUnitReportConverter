class JUnitReport {
    int numOfTestSuites
    def testSuites = []
	final String REPORTS_DIR = "test-reports"
    
    JUnitReport(testSuites) {
        this.testSuites = testSuites
    }

	void toXml() {
		new File(REPORTS_DIR).mkdir()
		testSuites.each { suite ->
			def xml = new groovy.xml.StreamingMarkupBuilder().bind {
				mkp.xmlDeclaration()
				testSuite(name: suite.name,
					tests: suite.numberOfTests,
					errors: suite.numberOfErrors,
					failures: suite.numberOfFailures,
					time: suite.time,
					timestamp: suite.timestamp) {
						
					}
			}
			new File(REPORTS_DIR, "TEST-${suite.name}.xml").write(xml.toString())
		}
	}
}