package dk.madsboddum.toc

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import java.nio.charset.StandardCharsets

import static org.junit.jupiter.api.Assertions.*

class TestCLI {
	
	@Nested
	class Print {
		
		@Test
		void testWithoutInput() {
			def out = new ByteArrayOutputStream()
			def err = new ByteArrayOutputStream()
			def cli = new CLI(out, err, () -> new ByteArrayInputStream())
			def actualExitCode = cli.execute(["--print"])
			def expectedExitCode = 1
			
			assertEquals(actualExitCode, expectedExitCode, "If --input is not specified, then the application should fail")
		}
		
		@Test
		void testWithInput() {
			def out = new ByteArrayOutputStream()
			def err = new ByteArrayOutputStream()
			def cli = new CLI(out, err, (String path) -> TestCLI.getResourceAsStream(path))
			def actualExitCode = cli.execute(["--input", "sku3_client.toc", "--print"])
			def expectedExitCode = 0
			
			assertEquals(expectedExitCode, actualExitCode, "If --input is specified, then the application should run successfully")
		}
	}
	
	@Nested
	class Version {
		
		@Test
		void testMessageContainsVersion() {
			def properties = new Properties()
		
			properties.load(TestCLI.classLoader.getResourceAsStream("version.properties"))
			
			String version = properties.get("version")
			
			def out = new ByteArrayOutputStream()
			def cli = new CLI(out, System.err, null)
			
			cli.execute(["--version"])
			def message = new String(out.toByteArray(), StandardCharsets.UTF_8)
			
			assertTrue(message.contains(version), "The printed message should contain the version")
		}
	}
	
}
