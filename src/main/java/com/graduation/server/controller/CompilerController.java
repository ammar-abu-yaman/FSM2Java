package com.graduation.server.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompilerController {

    private File jarFile;


    public CompilerController() throws IOException {
        jarFile = File.createTempFile("temp-compiler", ".jar");
        Files.copy(getClass().getResourceAsStream("/compiler.jar"), jarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    @PostMapping(value = "/generate", consumes = { MediaType.TEXT_PLAIN_VALUE }, produces = "application/zip")
    @CrossOrigin(originPatterns = { "*/*" })
    @ResponseBody
    ResponseEntity<Resource> generate(@RequestBody String body) throws Exception {
        String identifier = UUID.randomUUID().toString();
        Path specPath = writeSpecFile(identifier, body);
        Path compilerOutputPath = compileSpec(specPath, identifier);
        Path zippedOutputPath = zipOutput(compilerOutputPath, identifier);
        ResponseEntity<Resource> resp = getCompilerResponse(zippedOutputPath);
        cleanUpFiles(specPath, compilerOutputPath, zippedOutputPath);
        return resp;
    }

    private Path writeSpecFile(String identifier, String body) throws IOException {

        Path specPath = Paths.get(identifier + ".json");
        Files.createFile(specPath);
        Files.writeString(specPath, body);
        return specPath;
    }

    private Path compileSpec(Path specPath, String identifier) throws Exception {
        Path outputDirPath = Files.createDirectories(Paths.get(identifier));
        Process compilerProcess = new ProcessBuilder(
                "java", "-jar", "\"" + jarFile.getAbsolutePath().toString() + "\"",
                specPath.toAbsolutePath().toString(),
                "-f", "json",
                "-o", outputDirPath.toAbsolutePath().toString()).start();
        compilerProcess.waitFor();
        return outputDirPath;
    }

    private Path zipOutput(Path compilerOutputPath, String identifier) throws IOException {
        Path zipPath = Files.createFile(Paths.get(identifier + ".zip"));
        byte[] buffer = new byte[1024];
        try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Arrays
                    .stream(compilerOutputPath.toFile().listFiles())
                    .filter(file -> file.isFile() && file.getName().endsWith(".java"))
                    .forEach(file -> zipFile(zipStream, buffer, file));
        }
        return zipPath;
    }

    private void zipFile(ZipOutputStream zipStream, byte[] buffer, File file) {
        int length;
        ZipEntry entry = new ZipEntry(file.getName());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            zipStream.putNextEntry(entry);
            while ((length = fileInputStream.read(buffer)) != -1)
                zipStream.write(buffer, 0, length);
            zipStream.closeEntry();
        } catch (IOException e) {
        }
    }

    private ResponseEntity<Resource> getCompilerResponse(Path path) throws IOException {
        File file = path.toFile();
        ByteArrayResource body = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "\"" + file.getName() + "\"")
                .contentLength(body.contentLength())
                .body(body);
    }

    private void cleanUpFiles(Path... paths) throws IOException {
        for (Path path : paths) {
            if (path.toFile().isFile())
                Files.delete(path);
            else if (path.toFile().isDirectory()) {
                try (var fileStream = Files.walk(path)) {
                    fileStream.sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
            }
        }
    }
}
