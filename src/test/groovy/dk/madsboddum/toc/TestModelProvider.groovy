package dk.madsboddum.toc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import java.nio.charset.StandardCharsets

import static org.junit.jupiter.api.Assertions.assertEquals

class TestModelProvider {
	
	@Nested
	class HeaderCheck {
		
		@Test
		void testValidHeader() {
			def tocStream = TestModelProvider.getResourceAsStream("sku3_client.toc")
			def provider = new ModelProvider(tocStream)
			
			provider.get()	// This call simply shouldn't throw an exception
		}
		
		@Test
		void testInvalidHeader() {
			def tocStream = TestModelProvider.getResourceAsStream("invalid_header.toc")
			def provider = new ModelProvider(tocStream)
			
			Assertions.assertThrows(RuntimeException.class, { ->
				provider.get()
			})
		}
	}
	
	@Nested
	class Compression {
		
		@Test
		void testCompressedHeaderNotSupported() {
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			DataOutputStream stream = new DataOutputStream(baos)
			
			stream.write(" COT1000".getBytes(StandardCharsets.UTF_8))	// Write header
			stream.writeByte(2)	// 2 = compressed header
			stream.close()
			
			def array = baos.toByteArray()
			
			ByteArrayInputStream bais = new ByteArrayInputStream(array)
			def provider = new ModelProvider(bais)
			
			Assertions.assertThrows(UnsupportedOperationException.class, {
				provider.get()
			})
		}
	}
	
	@Nested
	class Trees {
		
		@Test
		void testAmount() {
			def tocStream = TestModelProvider.getResourceAsStream("sku3_client.toc")
			def provider = new ModelProvider(tocStream)
			def toc = provider.get()
			def trees = toc.getTrees()
			
			assertEquals(8, trees.size(), "The file has 8 trees, so the object should also have 8")
		}
		
		@Test
		void testOrder() {
			def tocStream = TestModelProvider.getResourceAsStream("sku3_client.toc")
			def provider = new ModelProvider(tocStream)
			def toc = provider.get()
			def actual = toc.getTrees()
			def expected = [
			        "patch_sku3_24_shared_00.tre",
					"patch_sku3_24_client_00.tre",
					"patch_sku3_25_shared_00.tre",
					"patch_sku3_25_client_00.tre",
					"patch_sku3_27_shared_00.tre",
					"patch_sku3_27_client_00.tre",
					"patch_sku3_33_shared_00.tre",
					"patch_sku3_33_client_00.tre"
			]
			
			assertEquals(expected, actual, "Order should be the one that's defined in the file")
		}
	}
}
