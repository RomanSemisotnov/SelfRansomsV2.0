package ru.marketboost.ransom.selenium.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DriverFactory {

    public WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-autofill-keyboard-accessory-view[8]");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("window-size=1600,900");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

        return new ChromeDriver(options);
    }

    public WebDriver getDriver(String proxyIp, String proxyPort, String login, String pass) throws IOException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-autofill-keyboard-accessory-view[8]");
        options.addArguments("window-size=1600,900");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        String manifest_json = """
                {
                  "version": "1.0.0",
                  "manifest_version": 2,
                  "name": "Chrome Proxy",
                  "permissions": [
                    "proxy",
                    "tabs",
                    "unlimitedStorage",
                    "storage",
                    "<all_urls>",
                    "webRequest",
                    "webRequestBlocking"
                  ],
                  "background": {
                    "scripts": ["background.js"]
                  },
                  "minimum_chrome_version":"22.0.0"
                }""";

        String background_js = String.format("""
                var config = {
                  mode: "fixed_servers",
                  rules: {
                    singleProxy: {
                      scheme: "http",
                      host: "%s",
                      port: parseInt(%s)
                    },
                    bypassList: ["localhost"]
                  }
                };

                chrome.proxy.settings.set({value: config, scope: "regular"}, function() {});

                function callbackFn(details) {
                return {
                authCredentials: {
                username: "%s",
                password: "%s"
                }
                };
                }

                chrome.webRequest.onAuthRequired.addListener(
                callbackFn,
                {urls: ["<all_urls>"]},
                ['blocking']
                );""", proxyIp, proxyPort, login, pass);

        FileOutputStream fos = new FileOutputStream("proxy_auth_plugin.zip");
        ZipOutputStream zipOS = new ZipOutputStream(fos);

        createFile("manifest.json", manifest_json);
        createFile("background.js", background_js);

        File file = new File("proxy_auth_plugin.zip");
        writeToZipFile("manifest.json", zipOS);
        writeToZipFile("background.js", zipOS);
        zipOS.close();
        fos.close();
        options.addExtensions(file);

        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    private static void writeToZipFile(String path, ZipOutputStream zipStream) throws IOException {
        System.out.println("Writing file : '" + path + "' to zip file");
        File aFile = new File(path);
        FileInputStream fis = new FileInputStream(aFile);
        ZipEntry zipEntry = new ZipEntry(path);
        zipStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fis.close();
    }

    private static void createFile(String filename, String text) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(text);
        }
    }

}
