class OCUnitReportConverter {
    final String LINE_BREAK = "\r\n"

    def parse(String ocunitOutput) {
       
        def testSuites = []
        def expSuite = /^Test Suite '([^']+)' started.*/
        ocunitOutput.split(LINE_BREAK).each {
            def matcher = (it =~ expSuite)
            if (matcher.matches())
                testSuites << new TestSuite(name: matcher[0][1])
        }
        // remove the first element since we don't need that one
        testSuites.remove(0)
        new JUnitReport(testSuites)
    }    
}