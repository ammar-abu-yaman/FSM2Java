package com.graduation;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.graduation.fsm.Fsm;
import com.graduation.generate.Generator;
import com.graduation.generate.JavaGenerator;
import com.graduation.parse.FsmJParser;
import com.graduation.parse.FsmParser;
import com.graduation.parse.JsonParser;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class App {

	public static void main(String[] args) {
		try {
			ArgumentParser argParser = getArgParser();
			Namespace results = argParser.parseArgs(args);
			Path specPath = getSpecPath(results.<String>getList("spec").get(0));
			Path outputPath = createOutputDir(results.getString("output"));
			FsmParser parser = getParser(results.getString("format"));

			Fsm fsm = parser.parse(specPath.toFile());
			Generator generator = getGenerator(outputPath, fsm, results.getString("language"));
			generator.generate();

		} catch (ArgumentParserException | FileNotFoundException exc) {
			System.out.println(exc.getMessage());
			System.exit(1);
		} catch (Exception exc) {
			System.out.println("Unexpected Error occured: " + exc.getMessage());
			System.exit(1);
		}
	}

	private static JavaGenerator getGenerator(Path outputPath, Fsm fsm, String language) {
		switch (language) {
			case "java":
				return new JavaGenerator(fsm, outputPath);
		}
		return null;
	}

	private static Path createOutputDir(String path) throws IOException {
		if (path == null)
			path = "output";
		return Files.createDirectories(Paths.get(path));
	}

	private static FsmParser getParser(String format) {
		switch (format) {
			case "fsmj":
				return new FsmJParser();
			case "json":
				return new JsonParser();
		}
		return null;
	}

	private static Path getSpecPath(String spec) throws FileNotFoundException {
		System.out.println(spec);
		Path path = Paths.get(spec);
		if (!path.toFile().exists() || !path.toFile().exists())
			throw new FileNotFoundException(format("Error: file '%s' not found", spec));
		return path;
	}

	private static ArgumentParser getArgParser() {
		ArgumentParser parser = ArgumentParsers.newFor("Compiler")
				.build()
				.description("Compiler for Fsm2J project")
				.defaultHelp(true);

		parser.addArgument("spec")
				.nargs(1)
				.help("Specifies the input spec file path");

		parser.addArgument("-f", "--format")
				.choices("json", "fsmj")
				.setDefault("fsmj")
				.help("Specify the format of the input spec file");

		parser.addArgument("-l", "--language")
				.choices("java")
				.setDefault("java")
				.help("Specify the output language of the generated code");

		parser.addArgument("-o", "--output")
				.type(String.class)
				.setDefault("")
				.help("Specify output directory of the compiler");

		return parser;
	}

}
