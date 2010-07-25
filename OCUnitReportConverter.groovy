import java.util.regex.Matcher

class OCUnitReportConverter {
    final String LINE_BREAK = System.getProperty("line.separator")

    def parse(String ocunitOutput) {
        def expSuite = /^Test Suite '([^']+)' started at (.*)/
        def expTestCount = /^Executed ([\d]+) tests, with ([\d]+) failure[s]? \([\d]+ unexpected\) in [\S]+ \(([^\)]+)\).*/
        def expTestCase = /^Test Case '-[\S]+ ([^\]]+)\]' (passed|failed) \(([\S]*) seconds\).*/
		def expTestFailure = /(.+): error: -\[[^]]+\] : (.*)/

		def testSuites = []
		def lastFailureCallstack
		def lastFailureMessage
        
        ocunitOutput.split(LINE_BREAK).each {
            switch (it) {
                case ~expSuite:
					def matcher = Matcher.getLastMatcher()
                    testSuites << new TestSuite(name: matcher[0][1], timestamp: matcher[0][2])
                    break
                case ~expTestCount:
                    // Use -1 as the flag to skip the very last test/failure count (which we don't need)
					def matcher = Matcher.getLastMatcher()
                    if (testSuites[-1].numberOfTests == -1) 
                        testSuites[-1].numberOfTests = matcher[0][1] as int
					if (testSuites[-1].numberOfFailures == -1)
						testSuites[-1].numberOfFailures = matcher[0][2] as int
					if (testSuites[-1].time == -1)
						testSuites[-1].time = matcher[0][3] as float
					testSuites[-1].numberOfErrors = 0
                    break
				case ~expTestFailure:
					def matcher = Matcher.getLastMatcher()
					lastFailureCallstack = matcher[0][1]
					lastFailureMessage = matcher[0][2]
					break
				case ~expTestCase:
					def matcher = Matcher.getLastMatcher()
					def passed = matcher[0][2] == "passed" ? true : false
					def testCase = new TestCase(name: matcher[0][1], passed: passed, time: matcher[0][3] as float)
					if (!passed) {
						def failure = new Failure(message: lastFailureMessage, callstack: lastFailureCallstack)
						testCase.failure = failure
						lastFailureMessage = null
						lastFailureCallstack = null
					}
					testSuites[-1].testCases << testCase
					break
            }        
        }
        
        // remove the first element since we don't need that one
        testSuites.remove(0)
        new JUnitReport(testSuites)
    }    
}