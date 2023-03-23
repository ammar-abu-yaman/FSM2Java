package com.graduation;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item;
import com.graduation.generate.JavaGenerator;
import com.graduation.parse.StandaloneCompilerVisitor;
import com.graduation.parse.compilerLexer;
import com.graduation.parse.compilerParser;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

public class App {
  public static void main(String[] args) throws Exception {
    // System.out.println(System.getProperty("name"));

    var inputFileName = System.getProperty("srcname");
    var inputFileFormat = System.getProperty("srcformat") == null ? "txt" : System.getProperty("srcformat");
    var outputFileName = System.getProperty("zipname") == null ? inputFileName : System.getProperty("zipname");

    if(inputFileFormat.equals("json"))
      generateByJSON(inputFileName);
    else if(inputFileFormat.equals("txt") || inputFileFormat.isEmpty())
      generateByTxt(inputFileName);
    else{
      System.out.println("Invalid file format!");
      System.exit(-1);
    }
    compressGeneratedFiles(inputFileName, outputFileName);
  }

  public static void generateByJSON(String inputFileName) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    String json = """
    {
    "name": "State1",
    "transitions": [
    {
    "trigger": {
    "name": "open",
    "params": [
    {
    "name": "param",
    "type": "String"
    }
    ]
    },
    "guard": "param.isEmpty()",
    "nextState": "State2",
    "code": "System.out.println(param.trim());"
    }
    ],
    "enterCode": "",
    "exitCode": ""
    }
    """;
    Item.State state = mapper.readValue(json, new TypeReference<Item.State>() {

    });
    System.out.println(state.transitions().get(0));
  }

  public static void generateByTxt(String inputFileName) throws Exception {
    var lexer = new compilerLexer(CharStreams.fromFileName(String.format("samples/%s.txt",
    inputFileName)));
    var tokens = new CommonTokenStream(lexer);
    var parser = new compilerParser(tokens);
    var tree = parser.program();
    var visitor = new StandaloneCompilerVisitor();
    Fsm fsm = (Fsm) visitor.visit(tree);
    Path path = Paths.get("outputs", inputFileName);
    JavaGenerator generator = new JavaGenerator(fsm, path);
    generator.generate();
  }

  public static void compressGeneratedFiles(String inputFileName, String outputFileName) throws Exception {
    File fileToBeCreated = new File(String.format("outputs/%s.zip", outputFileName));
    File directory = new File(String.format("outputs/%s", inputFileName));
    ZipFile zipFile = new ZipFile(fileToBeCreated);
    ZipParameters params = new ZipParameters();
    params.setIncludeRootFolder(false);
    fileToBeCreated.delete();
    zipFile.addFolder(directory, params);
    String[]entries = directory.list();
    for(String s: entries){
      File currentFile = new File(directory.getPath(),s);
      currentFile.delete();
    }
    directory.delete();
  }
}
