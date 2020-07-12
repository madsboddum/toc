package dk.madsboddum.toc

class Application {
	static void main(String[] args) {
		def cli = new CLI(System.out, System.err, (String path) -> new FileInputStream(path))
		
		def exitCode = cli.execute(args.toList())
		
		System.exit(exitCode)
	}
}
