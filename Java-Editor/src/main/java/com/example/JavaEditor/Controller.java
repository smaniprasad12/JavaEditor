package com.example.JavaEditor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.reflect.Method;

@RestController
public class Controller {

    @PostMapping("/run")
    public ResponseEntity<String> runCode(@RequestBody String code) {
        String className = "Code";
        String fileName = className + ".java";
        String expectedOutput = "Hello, world!";

        try {
            // Create the source file
            FileWriter writer = new FileWriter(fileName);
            writer.write(code);
            writer.close();

            // Compile the source file
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, fileName);

            // Load the compiled class and run the code
            //getting class
            Class<?> cls = Class.forName(className);
            //getting main method
            Method method = cls.getDeclaredMethod("main", String[].class);
            //getting output of the code
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PrintStream ps = new PrintStream(baos);
            System.setOut(ps);
            method.invoke(null, new Object[] { null });

            // Checking  the output
            String output = baos.toString();
            if (output.equals(expectedOutput)) {
                return ResponseEntity.ok("Test cases passed");
            } else {
                return ResponseEntity.ok("Test cases failed");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Compiler error: " + e.getMessage());

        }
    }

}

