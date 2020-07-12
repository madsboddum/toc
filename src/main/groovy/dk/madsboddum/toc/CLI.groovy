package dk.madsboddum.toc

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

import java.util.function.Function

class CLI {
	private final OutputStream out
	private final OutputStream err
	private final Function<String, InputStream> streamCreator
	
	CLI(OutputStream out, OutputStream err, Function<String, InputStream> streamCreator) {
		this.out = out
		this.err = err
		this.streamCreator = streamCreator
	}
	
	def execute(List<String> args) {
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
		def cmd
		
		try {
			cmd = parser.parse(options, args.toArray() as String[])
		} catch (ParseException e) {
			// Invalid syntax
			err.println(e.getMessage())
			printHelp(err, options)	// Let's try to help the user along
			
			return 1
		}
		
		if (cmd.hasOption("input")) {
			def inputValue = cmd.getOptionValue("input")
			
			def inputStream = streamCreator.apply(inputValue)
			def modelProvider = new ModelProvider(inputStream)
			
			def tableOfContents = modelProvider.get()
			
			if (cmd.hasOption("print")) {
				def items = tableOfContents.getItems()
				items.each { item ->
					out.println(item.fileName + "@" + tableOfContents.getTree(item.treFileIndex))
				}
			}
			
			return 0
		} else if (cmd.hasOption("version")) {
			def versionProps = new Properties()
			
			versionProps.load(Application.getClassLoader().getResourceAsStream("version.properties"))
			
			def versionId = versionProps.get("version");
			
			out.println("toc version " + versionId)
			return 0
		} else if (cmd.hasOption("help")) {
			// They explicitly asked for help
			printHelp(out, options)
			return 0
		}
		
		return 1
	}
	
	private static def printHelp(OutputStream out, Options options) {
		def formatter = new HelpFormatter()
		def writer = new PrintWriter(out)
		formatter.printHelp(writer, formatter.getWidth(), "toc", null, options, formatter.getLeftPadding(), formatter.getDescPadding(), null, false)
		writer.flush()
	}
}
