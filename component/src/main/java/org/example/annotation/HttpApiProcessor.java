package org.example.annotation;

import org.example.Handler;
import org.example.enums.HttpMethod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author chenqian
 * @date 2024/11/14 14:36
 **/
@SupportedAnnotationTypes("org.example.annotation.HttpApi")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HttpApiProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if(roundEnv.processingOver()) {
//            return false;
//        }
//
//        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HttpApi.class);
//        for (Element element : elements) {
//            HttpApi httpApi = element.getAnnotation(HttpApi.class);
//            String path = httpApi.path();
//            HttpMethod method = httpApi.method();
//            Handler handler = new Handler();
//        }
//        return false;
//    }
}
