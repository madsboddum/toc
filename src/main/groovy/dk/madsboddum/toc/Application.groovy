package dk.madsboddum.toc

import org.apache.commons.cli.*

class Application {
	static void main(String[] args) {
		def options = new Options()

		// Options
		def print = new Option("p", "print", false, "print entries in the TOC file specified by --input")
		print.setRequired(false)
		options.addOption(print)
		
		def input = new Option("i", "input", true, "specifies TOC file to work on")
		input.setRequired(false)
		input.setArgName("toc file")
		options.addOption(input)
		
		def help = new Option("h", "help", false, "lists possible options (what you're seeing now)")
		help.setRequired(false)
		options.addOption(help)
		
		def version = new Option("v", "version", false, "displays version of the tool")
		version.setRequired(false)
		options.addOption(version)
		
		def parser = new DefaultParser()
		def formatter = new HelpFormatter()
		def cmd
		
		try {
			cmd = parser.parse(options, args)
		} catch (ParseException e) {
			System.err.println(e.getMessage())
			formatter.printHelp("toc", options)
			
			System.exit(1)
			return
		}
		
		if (cmd.hasOption("input")) {
			def inputValue = cmd.getOptionValue("input")
			
			def inputStream = new FileInputStream(inputValue)
			def modelProvider = new ModelProvider(inputStream)
			
			def tableOfContents = modelProvider.get()
			
			if (cmd.hasOption("print")) {
				def items = tableOfContents.getItems()
				items.each { item ->
					println item.fileName + "@" + tableOfContents.getTree(item.treFileIndex)
				}
			}
		} else if (cmd.hasOption("version")) {
			def versionProps = new Properties()
			
			versionProps.load(Application.getClassLoader().getResourceAsStream("version.properties"))
			
			def versionId = versionProps.get("version");
			
			println "toc version " + versionId
		} else {
			// Unrecognized option OR they explicitly asked for help.
			formatter.printHelp("toc", options)
		}
	}
}
