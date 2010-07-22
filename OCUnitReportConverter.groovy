import java.util.regex.Matcher

class OCUnitReportConverter {
    final String LINE_BREAK = System.getProperty("line.separator")

    def parse(String ocunitOutput) {
       
        def testSuites = []
        def expSuite = /^Test Suite '([^']+)' started.*/
        def expFailureCount = /^Executed ([\d]+) tests, with ([\d]+) failure.*/
        
        ocunitOutput.split(LINE_BREAK).each {
            switch (it) {
                case ~expSuite:
                    testSuites << new TestSuite(name: Matcher.getLastMatcher()[0][1])
                    break
                case ~expFailureCount:
                    // Use -1 as the flag to skip the very last failure count (which 
                    // we don't need)
                    if (testSuites[-1].numberOfFailures == -1) {
						def matcher = Matcher.getLastMatcher()
                        testSuites[-1].numberOfTests = matcher[0][1] as int
						testSuites[-1].numberOfFailures = matcher[0][2] as int
					}
                    break                
            }        
        }
        
        // remove the first element since we don't need that one
        testSuites.remove(0)
        new JUnitReport(testSuites)
    }    
}