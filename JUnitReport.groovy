class JUnitReport {
    int numOfTestSuites
    def testSuites = []
    
    JUnitReport(testSuites) {
        this.testSuites = testSuites
    }
}