package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.ProxyDto;
import com.eleren.jobofferorganiser.dto.ProxyListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class RequestExecutor {

    private final ReentrantLock lock = new ReentrantLock();
    private final ProxyListDto proxyListDto;
    private WebDriver driver;


    public RequestExecutor(Environment env) throws Exception {
        if (Objects.isNull(env.getProperty("chromedriverpath")))
            throw new Exception("Environment property [chromedriverpath] needs to be set");
        else
            System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(env.getProperty("chromedriverpath")));

        System.setProperty("webdriver.chrome.silentOutput", "true");
        proxyListDto = getProxy();
        driver = getDriver();
    }

    public String execute(String url) throws Exception {
        lock.lock();
        try {
            System.out.println("Processing url: [" + url + "]");
            Thread t = new Thread(() -> driver.get(Thread.currentThread().getName()), url);
            t.start();
            t.join(8000);
            if (t.isAlive()) {
                t.interrupt();
                throw new Exception("ERR_TIMEOUT");
            } else if (driver.getTitle().toLowerCase().contains("captcha") ||
                    driver.findElements(By.name("captcha-bypass")).size() > 0 || driver.getPageSource().length() < 100) {
                throw new Exception("ERR_NO_READABLE_DATA");
            }

            return driver.getPageSource();
        } catch (Exception e) {
            if (e.getMessage().contains("ERR_TUNNEL_CONNECTION_FAILED") ||
                    e.getMessage().contains("ERR_PROXY_CONNECTION_FAILED") ||
                    e.getMessage().contains("ERR_CONNECTION_RESET") ||
                    e.getMessage().contains("ERR_CONNECTION_CLOSED") ||
                    e.getMessage().equals("ERR_NO_READABLE_DATA") ||
                    e.getMessage().equals("ERR_TIMEOUT")
            ) {
                nextDriver();
                return execute(url);
            } else
                throw new Exception(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        this.driver.quit();
    }

    private ProxyListDto getProxy() throws Exception {
        try {
            ProxyListDto proxyListDto = new ProxyListDto();
            Document doc = Jsoup.connect("https://hidemy.name/en/proxy-list/?country=ALAMATBYBEBABGHRCYCZFIFRGEDEGRHUIEITKZLVMTMENLNOPLPTRORURSSKSIESSETRUAGB&maxtime=600&type=h&anon=1#list").get();
            Elements elements = doc.select("div[class=table_block]").first().getElementsByTag("tbody").first().getElementsByTag("tr");
            for (int i = 1; i < elements.size(); i++) {
                Elements e2 = elements.get(i).getElementsByTag("td");

                ProxyDto proxyDto = new ProxyDto();
                proxyDto.setAddress(e2.get(0).text());
                proxyDto.setPort(Integer.parseInt(e2.get(1).text()));
                proxyListDto.add(proxyDto);
            }
            proxyListDto.add(new ProxyDto());
            proxyListDto.add(null);

            return proxyListDto;
        } catch (Exception e) {
            throw new Exception("Failed to build proxy: " + e.getMessage());
        }
    }

    private WebDriver getDriver() throws Exception {
        ChromeOptions options = new ChromeOptions().addArguments(
                "--headless",
                "--no-sandbox",
                "--ignore-certificate-errors",
                "--disable-extensions",
                "--disable-web-security",
                "--silent");

        if (proxyListDto.getCurrent() == null)
            throw new Exception("Can't execute request");
        else if (proxyListDto.getCurrent().getAddress() != null) {
            System.out.println("Setting up proxy: " + proxyListDto.getCurrentString());

            Proxy proxy = new Proxy();
            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            proxy.setAutodetect(false);
            proxy.setHttpProxy(proxyListDto.getCurrentString());
            proxy.setSslProxy(proxyListDto.getCurrentString());
            options.setCapability("proxy", proxy);
        }
        return new ChromeDriver(options);
    }

    private void nextDriver() throws Exception {
        close();
        proxyListDto.next();
        driver = getDriver();
    }


}
