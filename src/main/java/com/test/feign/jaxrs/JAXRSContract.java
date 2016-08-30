package com.test.feign.jaxrs;

import feign.Contract;
import feign.MethodMetadata;
import feign.Util;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by ren.xiaobo on 2016/8/29.
 */
public final class JAXRSContract extends Contract.BaseContract {
    static final String ACCEPT = "Accept";
    static final String CONTENT_TYPE = "Content-Type";

    public JAXRSContract() {
    }

    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        Path path = (Path)clz.getAnnotation(Path.class);
        if(path != null) {
            String consumes = Util.emptyToNull(path.value());
            Util.checkState(consumes != null, "Path.value() was empty on type %s", clz.getName());
            if(consumes == null) {
                return;
            }

            if(!consumes.startsWith("/")) {
                consumes = "/" + consumes;
            }

            if(consumes.endsWith("/")) {
                consumes = consumes.substring(0, consumes.length() - 1);
            }

            data.template().insert(0, consumes);
        }

        Consumes consumes1 = clz.getAnnotation(Consumes.class);
        if(consumes1 != null) {
            this.handleConsumesAnnotation(data, consumes1, clz.getName());
        }

        Produces produces = clz.getAnnotation(Produces.class);
        if(produces != null) {
            this.handleProducesAnnotation(data, produces, clz.getName());
        }

    }

    protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
        Class annotationType = methodAnnotation.annotationType();
        HttpMethod http = (HttpMethod)annotationType.getAnnotation(HttpMethod.class);
        if(http != null) {
            Util.checkState(data.template().method() == null, "Method %s contains multiple HTTP methods. Found: %s and %s", method.getName(), data.template().method(), http.value());
            data.template().method(http.value());
        } else if(annotationType == Path.class) {
            String pathValue = Util.emptyToNull((Path.class.cast(methodAnnotation)).value());
            Util.checkState(pathValue != null, "Path.value() was empty on method %s", method.getName());
            String methodAnnotationValue = ((Path)Path.class.cast(methodAnnotation)).value();
            if(!methodAnnotationValue.startsWith("/") && !data.template().url().endsWith("/")) {
                methodAnnotationValue = "/" + methodAnnotationValue;
            }

            methodAnnotationValue = methodAnnotationValue.replaceAll("\\{\\s*(.+?)\\s*(:.+?)?\\}", "\\{$1\\}");
            data.template().append(methodAnnotationValue);
        } else if(annotationType == Produces.class) {
            this.handleProducesAnnotation(data, (Produces)methodAnnotation, "method " + method.getName());
        } else if(annotationType == Consumes.class) {
            this.handleConsumesAnnotation(data, (Consumes)methodAnnotation, "method " + method.getName());
        }

    }

    private void handleProducesAnnotation(MethodMetadata data, Produces produces, String name) {
        String[] serverProduces = produces.value();
        String clientAccepts = serverProduces.length == 0?null:Util.emptyToNull(serverProduces[0]);
        Util.checkState(clientAccepts != null, "Produces.value() was empty on %s", name);
        data.template().header("Accept", new String[]{null});
        data.template().header("Accept", clientAccepts);
    }

    private void handleConsumesAnnotation(MethodMetadata data, Consumes consumes, String name) {
        String[] serverConsumes = consumes.value();
        String clientProduces = serverConsumes.length == 0?null:Util.emptyToNull(serverConsumes[0]);
        Util.checkState(clientProduces != null, "Consumes.value() was empty on %s", name);
        data.template().header("Content-Type", new String[]{null});
        data.template().header("Content-Type", clientProduces);
    }

    protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
        boolean isHttpParam = false;
        Annotation[] var5 = annotations;
        int var6 = annotations.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Annotation parameterAnnotation = var5[var7];
            Class annotationType = parameterAnnotation.annotationType();
            String name;
            if(annotationType == PathParam.class) {
                name = ((PathParam)PathParam.class.cast(parameterAnnotation)).value();
                Util.checkState(Util.emptyToNull(name) != null, "PathParam.value() was empty on parameter %s", new Object[]{Integer.valueOf(paramIndex)});
                this.nameParam(data, name, paramIndex);
                isHttpParam = true;
            } else {
                Collection header;
                if(annotationType == QueryParam.class) {
                    name = ((QueryParam)QueryParam.class.cast(parameterAnnotation)).value();
                    Util.checkState(Util.emptyToNull(name) != null, "QueryParam.value() was empty on parameter %s", new Object[]{Integer.valueOf(paramIndex)});
                    header = this.addTemplatedParam((Collection)data.template().queries().get(name), name);
                    data.template().query(name, header);
                    this.nameParam(data, name, paramIndex);
                    isHttpParam = true;
                } else if(annotationType == HeaderParam.class) {
                    name = ((HeaderParam)HeaderParam.class.cast(parameterAnnotation)).value();
                    Util.checkState(Util.emptyToNull(name) != null, "HeaderParam.value() was empty on parameter %s", new Object[]{Integer.valueOf(paramIndex)});
                    header = this.addTemplatedParam((Collection)data.template().headers().get(name), name);
                    data.template().header(name, header);
                    this.nameParam(data, name, paramIndex);
                    isHttpParam = true;
                } else if(annotationType == FormParam.class) {
                    name = ((FormParam)FormParam.class.cast(parameterAnnotation)).value();
                    Util.checkState(Util.emptyToNull(name) != null, "FormParam.value() was empty on parameter %s", new Object[]{Integer.valueOf(paramIndex)});
                    data.formParams().add(name);
                    this.nameParam(data, name, paramIndex);
                    isHttpParam = true;
                }
            }
        }

        return isHttpParam;
    }
}
