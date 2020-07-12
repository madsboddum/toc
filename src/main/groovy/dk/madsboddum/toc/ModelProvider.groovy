package dk.madsboddum.toc

import dk.madsboddum.toc.model.Item
import dk.madsboddum.toc.model.TableOfContents

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class ModelProvider {
	private final InputStream inputStream
	
	ModelProvider(InputStream inputStream) {
		this.inputStream = inputStream
	}
	
	TableOfContents get() {
		def dataStream = new DataInputStream(inputStream)
		
		headerCheck(dataStream)	// Sanity check, ensure the data stream actually represents a Table of Contents
		
		def compression = readUnsignedInteger(dataStream)	// Read unsigned int
		
		if (compression != 0) {
			throw new UnsupportedOperationException("Compressed TOC headers are not supported");
		}
		
		def itemCount = readUnsignedInteger(dataStream)
		def itemIndexSize = readUnsignedInteger(dataStream)
		def fileNamesSize = readUnsignedInteger(dataStream)
		
		dataStream.skipBytes(Integer.BYTES)	// Perhaps uncompressed fileNamesSize, if compression = 1
		
		def treFileNamesCount = readUnsignedInteger(dataStream)
		def treFileNamesSize = readUnsignedInteger(dataStream)
		
		// Begin constructing the TableOfContents object
		def toc = new TableOfContents()
		
		// Read TRE file names
		for (def i = 0; i < treFileNamesCount; i++) {
			def treFileName = readNullTerminatedString(dataStream)
			
			toc.addTree(treFileName)
		}
		
		// Read items
		for (def i = 0; i < itemCount; i++) {
			def compressed = dataStream.readUnsignedShort()
			
			if (compressed == 2) {
				throw new UnsupportedOperationException("Compressed items are not supported. Failed at item with index " + i + ".")
			}
			
			def item = new Item()
			
			item.treFileIndex = dataStream.readShort() & 0xFF
			item.fileNameCrc = readUnsignedInteger(dataStream)
			item.fileNameLength = readUnsignedInteger(dataStream)
			item.treFileOffset = readUnsignedInteger(dataStream)
			item.size = readUnsignedInteger(dataStream)
			item.compressedSize = readUnsignedInteger(dataStream)
			
			toc.addItem(item)
		}
		
		// Read item filenames
		for (def i = 0; i < itemCount; i++) {
			def item = toc.getItem(i)
			
			item.fileName = readNullTerminatedString(dataStream)
		}
		
		return toc
	}
	
	private static def headerCheck(DataInputStream dataStream) {
		def actualHeaderBytes = new byte[8]
		
		dataStream.read(actualHeaderBytes)
		
		def expectedHeader = " COT1000"
		def expectedHeaderBytes = expectedHeader.getBytes(StandardCharsets.UTF_8) as List<Byte>
		
		if (expectedHeaderBytes != actualHeaderBytes.toList()) {
			def actualHeader = new String(actualHeaderBytes, StandardCharsets.UTF_8)
			
			throw new RuntimeException("Header check failed. Should be " + expectedHeader + " but was " + actualHeader + ".")
		}
	}
	
	private static def readUnsignedInteger(DataInputStream dataStream) {
		byte[] intData = new byte[Integer.BYTES];
		
		dataStream.read(intData)
		
		return ByteBuffer.wrap(intData)
				.order(ByteOrder.LITTLE_ENDIAN)
				.getInt()
	}
	
	private static def readNullTerminatedString(DataInputStream dataStream) {
		byte b
		
		def builder = new StringBuilder()
		
		while ((b = (byte) dataStream.read()) != 0) {
			builder.append((char) b)
		}
		
		return builder.toString()
	}
}