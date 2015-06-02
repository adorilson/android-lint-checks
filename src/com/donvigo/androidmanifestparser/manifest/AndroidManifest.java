package com.donvigo.androidmanifestparser.manifest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.donvigo.androidmanifestparser.xml.AndroidXMLDecompress;
import com.donvigo.androidmanifestparser.xml.XmlHandler;

/**
 * Created by vgaidarji on 12.03.14.
 */
@Root(name = "manifest", strict = false)
public class AndroidManifest {
    @Attribute(name = "package", required = true)
    private String packageName;
    @Element(name = "application")
    private ApplicationEntry application;
    @Element(name = "uses-sdk", required = false)
    private UsesSdkEntry usesSdk;
    private boolean isLauncher;

    private static final String ANDROID_MANIFEST_FILE = "AndroidManifest.xml";
    
    public boolean isLauncher() {
        return isLauncher;
    }

    public void setLauncher(boolean isLauncher) {
        this.isLauncher = isLauncher;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ApplicationEntry getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntry application) {
        this.application = application;
    }
    
    public UsesSdkEntry getUsesSdk() {
		return usesSdk;
	}

	public void setUsesSdk(UsesSdkEntry usesSdk) {
		this.usesSdk = usesSdk;
	}

	public static AndroidManifest getManifestFromXML(String pathToFile){
		String manifestXmlString = null;
		AndroidManifest androidManifest = null;
		try {
			manifestXmlString = parseManifestFile(pathToFile);
			androidManifest = new XmlHandler<AndroidManifest>().parse(AndroidManifest.class, manifestXmlString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return androidManifest;
	}
    
	public static AndroidManifest getManifestFromAPK(String pathToFile){
		String manifestXmlString = null;
		AndroidManifest androidManifest = null;
		
		manifestXmlString = parseManifestFileAPK(pathToFile);
		androidManifest = new XmlHandler<AndroidManifest>().parse(AndroidManifest.class, manifestXmlString);
		return androidManifest;
	}
	
    private static String parseManifestFile(String path) throws IOException {
		
		FileInputStream entrada = new FileInputStream(path);
		InputStreamReader entradaFormatada = new InputStreamReader(entrada);
		BufferedReader br = new BufferedReader(entradaFormatada);
		
		String linha = br.readLine();
		StringBuffer buffer = new StringBuffer();
		while(linha != null) {
		   buffer.append(linha);
		   linha = br.readLine();
		}
		
		br.close();
		return buffer.toString();
	}
    
    public static String parseManifestFileAPK(String path) {
        try {
            JarFile jf = new JarFile(path);
            InputStream is = jf.getInputStream(jf.getEntry(ANDROID_MANIFEST_FILE));
            byte[] buf = new byte[is.available()];
            int br = is.read(buf);
            String manifestXml = AndroidXMLDecompress.decompressXML(buf);
            buf.clone();
            is.close();
            jf.close();

            return manifestXml;
        } catch (Exception ex) {
            return null;
        }
    }
}

