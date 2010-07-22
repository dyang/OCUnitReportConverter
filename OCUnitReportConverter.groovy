class OCUnitReportConverter {
    final String LINE_BREAK = "\r\n"

    def parse(String ocunitOutput) {
       
        def testSuites = []
        def expSuite = /^Test Suite '([^']+)' started.*/
        def expFailureCount = /^Executed [\d]+ tests, with ([\d]+) failure.*/
        ocunitOutput.split(LINE_BREAK).each {
            def matcher = (it =~ expSuite)
            if (matcher.matches())
                testSuites << new TestSuite(name: matcher[0][1])
            else {
                matcher = (it =~ expFailureCount)
                if (matcher.matches()) {                
                    // Use -1 as the flag to skip the very last failure count (which 
                    // we don't need)
                    if (testSuites[-1].numberOfFailures == -1)
                        testSuites[-1].numberOfFailures = matcher[0][1] as int
                }
            }
        }
        // remove the first element since we don't need that one
        testSuites.remove(0)
        new JUnitReport(testSuites)
    }    
}