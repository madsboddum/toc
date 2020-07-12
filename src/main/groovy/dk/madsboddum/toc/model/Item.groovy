package dk.madsboddum.toc.model

class Item {
	private int treFileIndex
	private int fileNameCrc
	private int fileNameLength
	private int treFileOffset
	private int size
	private int compressedSize	// 0 if not compressed
	private String fileName
	
	Item() {
	}
	
	int getTreFileIndex() {
		return treFileIndex
	}
	
	void setTreFileIndex(int treFileIndex) {
		this.treFileIndex = treFileIndex
	}
	
	int getFileNameCrc() {
		return fileNameCrc
	}
	
	void setFileNameCrc(int fileNameCrc) {
		this.fileNameCrc = fileNameCrc
	}
	
	int getFileNameLength() {
		return fileNameLength
	}
	
	void setFileNameLength(int fileNameLength) {
		this.fileNameLength = fileNameLength
	}
	
	int getTreFileOffset() {
		return treFileOffset
	}
	
	void setTreFileOffset(int treFileOffset) {
		this.treFileOffset = treFileOffset
	}
	
	int getSize() {
		return size
	}
	
	void setSize(int size) {
		this.size = size
	}
	
	int getCompressedSize() {
		return compressedSize
	}
	
	void setCompressedSize(int compressedSize) {
		this.compressedSize = compressedSize
	}
	
	String getFileName() {
		return fileName
	}
	
	void setFileName(String fileName) {
		this.fileName = fileName
	}
}
