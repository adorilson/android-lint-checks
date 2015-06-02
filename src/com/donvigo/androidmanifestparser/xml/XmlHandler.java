package com.donvigo.androidmanifestparser.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Created by vgaidarji on 12.03.14.
 */
public class XmlHandler <T>{
    public T parse(Class<T> resultClass, String xmlString){
        Serializer serializer = new Persister();

        try {
            return serializer.read(resultClass, xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
