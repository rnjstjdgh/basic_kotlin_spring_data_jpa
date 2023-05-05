package com.example.demo.batch;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;


// https://discuss.kotlinlang.org/t/noclassdeffound-with-kotlin-java-project/20458/11
public class CustomJavaLineMapper<T> extends DefaultLineMapper<T> {

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        if (line.startsWith("T")) {
            return null;
        }
        return super.mapLine(line, lineNumber);
    }
}
