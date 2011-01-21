
import org.ccil.cowan.tagsoup.Parser;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;
import org.testng.annotations.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import com.thoughtworks.selenium.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by IntelliJ IDEA.
 * User: wei
 * Date: Jan 19, 2011
 * Time: 8:41:08 AM
 * To change this template use File | Settings | File Templates.
 */


public class DealCrawler {

    public DefaultSelenium selenium;

//    @BeforeClass
//    public void setUp() throws Exception {
//        selenium = new DefaultSelenium("127.0.0.1", 4444, "*firefox", "http://www.tigerairways.com/");
//        selenium.start();
//    }

    @Test
    public void testCheapGetawaysTigerAirways() throws Exception {

        //setup
        selenium = new DefaultSelenium("127.0.0.1", 4444, "*firefox", "http://www.tigerairways.com/");
        selenium.start();

        //page 1
        selenium.open("/sg/en/index.php");
        selenium.click("oneWayRadio");
        //from-to
        //TODO:xpath contains
        selenium.select("from1Select", "label=Singapore (SIN)");
        selenium.select("to1Select", "label=Bangkok Intl (BKK)");
        //day
        selenium.select("departDay1Select", "label=25");
        selenium.click("//option[@value='25']");
        //month year
        selenium.select("departMonth1Select", "label=Jan 2011");
        selenium.click("//option[@value='201101']");
        //adults
        selenium.click("ADULTSelect");
        selenium.select("ADULTSelect", "label=2");
        selenium.click("searchButton");
        selenium.waitForPageToLoad("30000");
//        selenium.captureEntirePageScreenshot("/Users/wei/Documents/year4sem2/sandbox/25jan.png", "");

        //page 2
        String source = selenium.getHtmlSource();
        Node node = cleanupHtml(source);
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//input[starts-with(@value, '20110125') and @type!='hidden']";
        NodeList nodes = (NodeList)xpath.evaluate(expression, node, XPathConstants.NODESET);

        if(nodes == null || nodes.getLength()==0)
            return;

        ArrayList<String> priceList = new ArrayList<String>();
        String[] ids = new String[nodes.getLength()];
        String checkPriceUpdateJS = "selenium.isElementPresent(\"//div[@id='inclusivePriceSummary']/dl[4]/dt/font[2]/b\");";

        for(int i=0; i<nodes.getLength(); i++){
            String att = nodes.item(i).getAttributes().getNamedItem("id").toString();
            ids[i] = att.substring(4, att.length()-1);
            //TODO:log
//            System.out.println(ids[i]);

            selenium.click(ids[i]);
            selenium.waitForCondition(checkPriceUpdateJS, "30000");

            String price = selenium.getText("//div[@id='inclusivePriceSummary']/dl[4]/dt/font[2]/b");
            //TODO:log
//            System.out.println(price);
            priceList.add(price);
        }

        assert priceList.size() == nodes.getLength() : priceList.size();
        Collections.sort(priceList);
        System.out.println("Lowest from TigerAirways: " + priceList.get(0));

    }

    @Test
    public void testCheapGetawaysJetStar() throws Exception {

        selenium = new DefaultSelenium("127.0.0.1", 4444, "*firefox", "http://www.jetstar.com/");
        selenium.start();

        //page 1
        selenium.open("/sg/en/index.aspx");
        selenium.click("one-way");
        //from-to
        //TODO:xpath contains
		selenium.select("from1", "label=Singapore");
		selenium.select("to1", "label=Bangkok");
        //day
		selenium.select("depart1-day-dep", "label=25");
//		selenium.click("//option[@value='25']");
        //month year
        selenium.select("depart1-month-dep", "label=Jan 2011");
        //adults
        selenium.select("adult-select", "label=2");
        selenium.click("Button1");
        selenium.waitForPageToLoad("30000");

        //page 2
        String checkPriceUpdateJS = "selenium.isElementPresent(\"stotalvalue\");";
        selenium.waitForCondition(checkPriceUpdateJS, "30000");

        String price = selenium.getText("stotalvalue");
        System.out.println("Lowest from JetStar: " + " " + price);

    }

    private Node cleanupHtml(String html) throws IOException, SAXException, TransformerException{
        InputStream is = new ByteArrayInputStream(html.getBytes("UTF-8"));
        XMLReader reader = new Parser();
        InputSource inputSource = new InputSource(is);

        reader.setFeature(Parser.namespacesFeature, false);
        reader.setFeature(Parser.namespacePrefixesFeature, false);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        DOMResult result = new DOMResult();
        transformer.transform(new SAXSource(reader, inputSource),
                result);


        return result.getNode();

    }

    @AfterClass
    public void tearDown() throws Exception {
        selenium.stop();
    }

}
