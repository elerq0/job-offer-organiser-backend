package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.ProxyDto;
import com.eleren.jobofferorganiser.dto.ProxyListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.env.Environment;

import java.util.concurrent.locks.ReentrantLock;

public class RequestExecutor {

    private final Environment env;
    private final ProxyListDto proxyListDto;
    private WebDriver driver;
    private final ReentrantLock lock = new ReentrantLock();

    public RequestExecutor(Environment env) throws Exception {
        this.env = env;
        proxyListDto = getProxy();
        driver = getDriver();
    }

    public void close(){
        this.driver.close();
        this.driver.quit();
    }

    public String execute(String url) throws Exception {
        lock.lock();
        System.out.println("Processing url: [" + url + "]");
        String html = "";
        try {
            do {
                try {
                    driver.get(url);

                    html = driver.getPageSource();
                } catch (Exception e) {
                    driver = getDriver();
                }
            } while (html.equals(""));
        } finally {
            lock.unlock();
        }
        return html;
    }

    private ProxyListDto getProxy() throws Exception {
        try {
            ProxyListDto proxyListDto = new ProxyListDto();
            Document doc = Jsoup.connect("https://hidemy.name/en/proxy-list/?maxtime=600&type=h#list").get();
            Elements elements = doc.select("div[class=table_block]").first().getElementsByTag("tbody").first().getElementsByTag("tr");
            for (int i = 1; i < elements.size(); i++) {
                Elements e2 = elements.get(i).getElementsByTag("td");

                ProxyDto proxyDto = new ProxyDto();
                proxyDto.setAddress(e2.get(0).text());
                proxyDto.setPort(Integer.parseInt(e2.get(1).text()));
                proxyListDto.add(proxyDto);
            }

            return proxyListDto;
        } catch (Exception e) {
            throw new Exception("Failed to build proxy: " + e.getMessage());
        }
    }

    private WebDriver getDriver() throws Exception {
        System.setProperty("webdriver.chrome.driver", env.getProperty("chromedriverpath"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--silent");
        driver = null;

        do {
            try {
                Proxy proxy = new Proxy();
                proxy.setAutodetect(false);
                proxy.setHttpProxy(proxyListDto.getCurrent().getAddress() + ":" + proxyListDto.getCurrent().getPort());
                options.setCapability("proxy", proxy);
                driver = new ChromeDriver(options);

            } catch (Exception e) {
                proxyListDto.next();
            }
        } while (driver == null);
        return driver;
    }

}
