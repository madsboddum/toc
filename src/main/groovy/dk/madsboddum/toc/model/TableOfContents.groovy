package dk.madsboddum.toc.model

class TableOfContents {
	private final List<String> trees
	private final List<Item> items
	
	TableOfContents() {
		trees = new ArrayList<>()
		items = new ArrayList<>()
	}
	
	def addTree(String tree) {
		trees.add(tree)
	}
	
	def getTree(int index) {
		return trees.get(index)
	}
	
	def getTrees() {
		return Collections.unmodifiableList(trees)
	}
	
	def addItem(Item item) {
		items.add(item)
	}
	
	def getItem(int index) {
		return items.get(index)
	}
	
	def getItems() {
		return Collections.unmodifiableList(items)
	}
}
