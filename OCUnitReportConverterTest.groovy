class OCUnitReportConverterTest extends GroovyTestCase {
	
	def converter
	
	void setUp() {
		converter = new OCUnitReportConverter()
	}
	
	void testInitialize() {
		assertNotNull converter
	}

}