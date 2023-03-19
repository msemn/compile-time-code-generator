package com.example.demo.ann;

import com.google.auto.service.AutoService;
import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.TypeName;
import org.springframework.javapoet.TypeSpec;
import org.springframework.stereotype.Service;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.demo.ann.AllFieldsNullChecker")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AllFieldsNullCheckerProcessor extends AbstractProcessor {

    public List<String> getFieldNameList(TypeElement classElement) {
        List<String> fieldNames = new ArrayList<>();
        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind().isField()) {
                VariableElement variableElement = (VariableElement) element;
                TypeMirror type = variableElement.asType();
                System.out.println("Field name: " + variableElement.getSimpleName());
                System.out.println("Field type: " + type);
                if (type.getKind().isPrimitive()) {
                    continue;
                }
                fieldNames.add(variableElement.getSimpleName().toString());
            }
        }
        return fieldNames;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AllFieldsNullChecker.class)) {

            TypeElement classElement = (TypeElement) element;
            String className = classElement.getSimpleName().toString();

            List<TypeElement> classNames = new ArrayList<>();
            for (TypeMirror typeMirror : getArrayTypeMirror(classElement)) {
                TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
                classNames.add(typeElement);
            }

            List<MethodSpec> methodList = new ArrayList<>();
            for (TypeElement source : classNames) {
                String generatedMethodName = "isEvery" + source.getSimpleName() + "FieldNull";

                List<String> fieldNames = getFieldNameList(source);
                StringBuilder generatedMethodBody = new StringBuilder("return ");
                if (fieldNames.size() == 0)
                    generatedMethodBody.append("true");
                else {
                    for (String fieldName : fieldNames) {
                        generatedMethodBody
                                .append(source.getSimpleName().toString().toLowerCase())
                                .append(".")
                                .append("get")
                                .append(fieldName.substring(0, 1).toUpperCase())
                                .append(fieldName.substring(1))
                                .append("()")
                                .append(" == null &&\n");
                    }
                }
                String methodBody = generatedMethodBody.toString();
                methodBody = methodBody.substring(0, methodBody.length() - 4);

                MethodSpec generatedMethod = MethodSpec.methodBuilder(generatedMethodName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.get(source), source.getSimpleName().toString().toLowerCase())
                        .returns(Boolean.class)
                        .addStatement(methodBody)
                        .build();
                methodList.add(generatedMethod);
            }

            TypeSpec generatedClass = TypeSpec.classBuilder(className + "Impl")
                    .addAnnotation(Service.class)
                    .addSuperinterface(TypeName.get(classElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethods(methodList)
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

    public TypeMirror[] getArrayTypeMirror(TypeElement typeElement) {
        var am = typeElement.getAnnotationMirrors()
                .stream()
                .filter(m -> m.getAnnotationType().toString().equals(AllFieldsNullChecker.class.getName()))
                .findFirst()
                .orElse(null);

        if (am == null)
            return new TypeMirror[0];

        var ev = am.getElementValues()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getSimpleName().toString().equals("sources"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (ev == null)
            return new TypeMirror[0];

        List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) ev.getValue();
        TypeMirror[] typeMirrors = new TypeMirror[values.size()];

        for (int i = 0; i < values.size(); i++) {
            typeMirrors[i] = (TypeMirror) values.get(i).getValue();
        }

        return typeMirrors;
    }

}