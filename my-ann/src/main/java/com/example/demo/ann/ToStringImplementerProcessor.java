package com.example.demo.ann;


import com.google.auto.service.AutoService;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.TypeName;
import org.springframework.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.demo.ann.ToStringImplementer")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ToStringImplementerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ToStringImplementer.class)) {

            TypeElement classElement = (TypeElement) element;
            String className = classElement.getSimpleName().toString();
            String generatedMethodName = "toString";
            String generatedMethodBody = "return \"Generated toString method for class " + className + "\"";
            MethodSpec generatedMethod = MethodSpec.methodBuilder(generatedMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement(generatedMethodBody)
                    .build();
            TypeSpec generatedClass = TypeSpec.classBuilder(className + "Impl")
                    .addSuperinterface(TypeName.get(classElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(generatedMethod)
                    .build();
            JavaFile javaFile = JavaFile.builder("com.example.generated", generatedClass)
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}