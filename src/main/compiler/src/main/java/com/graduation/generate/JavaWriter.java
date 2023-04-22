package com.graduation.generate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.graduation.fsm.Fsm;

import static java.lang.String.format;

public final class JavaWriter implements AutoCloseable {
    private File outputFile;
    private PrintWriter writer;

    public JavaWriter(Path path) throws IOException {
        outputFile = path.toFile();
        outputFile.createNewFile();
        writer = new PrintWriter(outputFile);
    }

    public void writePackage(String packageName) {
        writer.println("package " + packageName + ";");
    }

    public void writeImport(String importName) {
        writer.println(format("import %s;", importName));
    }

    public void writeClassHeader(String classname, List<String> modifiers) {
        writeClassHeader(classname, modifiers, Optional.empty(), List.of());
    }

    public void writeClassHeader(String classname, List<String> modifiers, Optional<String> baseClass) {
        writeClassHeader(classname, modifiers, baseClass, List.of());
    }

    public void writeClassHeader(String classname, List<String> modifiers, Optional<String> baseClass,
            List<String> interfaces) {
        String baseClassDecl = baseClass.isPresent() ? " extends " + baseClass.get() : "";
        String modifiersDecl = generateModifiersDecl(modifiers);
        String implementedInterfacesDecl = !interfaces.isEmpty()
                ? " implements " + joinList(interfaces, ", ")
                : "";
        writer.println(
                format("%sclass %s%s%s {",
                        modifiersDecl,
                        classname,
                        baseClassDecl,
                        implementedInterfacesDecl));
    }

    public void writeInterfaceHeader(String name, List<String> modifiers, List<String> interfaces) {
        String modifiersDecl = generateModifiersDecl(modifiers);
        String extendedInterfacesDecl = !interfaces.isEmpty()
                ? " extends " + joinList(interfaces, ", ")
                : "";

        writer.println(format("%sinterface %s%s {", modifiersDecl, name, extendedInterfacesDecl));
    }

    public void writeClosingBracket(int indentationLevel) {
        writer.println(indent("}", indentationLevel));
    }

    public void writeCode(String code, int indentationLevel) {
        writer.println(indent(code, indentationLevel));
    }

    public void writeEmptyLine() {
        writer.println();
    }

    public void writeProperty(String name, String type, String visibilityModifier) {
        writer.println(
                indent(format("%s %s %s;", visibilityModifier, type, name), 1));
    }

    public void writerConstructor(String name, List<String> modifiers,
            List<Fsm.Param> paramList) {
        writeMethodHeader(name, "", modifiers, paramList);
    }

    public void writeMethodHeader(String name, String returnType, List<String> modifiers,
            List<Fsm.Param> paramList) {
        String modifiersDecl = generateModifiersDecl(modifiers);
        String params = generateParamList(paramList);

        writer.println(
                indent(format("%s%s %s(%s) {", modifiersDecl, returnType, name, params), 1));
    }

    public void writeAbstractMethodHeader(String name, String returnType, List<String> modifiers,
            List<Fsm.Param> paramList) {
        String modifiersDecl = generateModifiersDecl(modifiers);
        String params = generateParamList(paramList);

        writer.println(
                indent(format("%s%s %s(%s);", modifiersDecl, returnType, name, params), 1));
    }

    private String generateModifiersDecl(List<String> modifiers) {
        return !modifiers.isEmpty() ? joinList(modifiers, " ") + " " : "";
    }

    private String joinList(List<String> list, String delimiter) {
        return list
                .stream()
                .collect(Collectors.joining(delimiter));
    }

    private String generateParamList(List<Fsm.Param> paramList) {
        return paramList.stream()
                .map(param -> format("%s %s", param.type(), param.name()))
                .collect(Collectors.joining(", "));
    }

    private String indent(String text, int indentationLevel) {
        String indentation = getIndentation(indentationLevel);

        return Arrays.stream(text.split("\n"))
                .map(line -> indentation + line.stripTrailing() + "\n")
                .collect(Collectors.joining());
    }

    private String getIndentation(int indentationLevel) {
        String indentation = IntStream
                .range(0, indentationLevel)
                .mapToObj(i -> "\t")
                .collect(Collectors.joining());
        return indentation;
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
