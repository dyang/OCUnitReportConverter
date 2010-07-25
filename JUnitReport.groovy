class JUnitReport {
    int numOfTestSuites
    def testSuites = []
	final String REPORTS_DIR = "test-reports"
    
    JUnitReport(testSuites) {
        this.testSuites = testSuites
    }

	void toXml() {
		new File(REPORTS_DIR).mkdir()
		testSuites.each {
			new File(REPORTS_DIR, "TEST-${it.name}.xml").write("")
		}
	}
}