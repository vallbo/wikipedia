package sk.fiit.vi.project.xml;

import org.xml.sax.SAXException;
import sk.fiit.vi.project.model.InfoBox;
import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.parsers.BaseParser;
import sk.fiit.vi.project.parsers.InfoBoxParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by VB on 21/11/14.
 */
public class Parser implements Callable<List<Page>> {

    private List<Page> result = new ArrayList();
    private String fileName;
    private int threadNum;
    private int numOfThreads;
    private int i = 0;

    public Parser(String fileName, int threadNum, int numOfThreads) {
        this.fileName = fileName;
        this.threadNum = threadNum;
        this.numOfThreads = numOfThreads;
    }

    public List<Page> call() throws IOException, SAXException, ParserConfigurationException {
        XMLReader r = new XMLReader();
        r.addHandler("page", new
                        NodeHandler() {
                            @Override
                            public void process(StructuredNode node) throws XPathExpressionException {
                                i++;
                                if ((i % numOfThreads) != threadNum) {
                                    return;
                                }
                                String title = node.queryString("title/text()");
                                String text = node.queryString("//text/text()").replaceAll("[\r\n]+", " ");
                                ;
                                if (!text.matches("^(?i).*infobox.*$")) {
                                    return;
                                }
                                String infoboxString = BaseParser.parseString(text);
                                if (infoboxString.isEmpty()) {
                                    return;
                                }
                                InfoBox ib = InfoBoxParser.parseInfoBoxData(infoboxString);
                                if (ib == null) {
                                    return;
                                }
                                result.add(new Page(title, ib));
                            }
                        }
        );
        r.parse(new FileInputStream(fileName));
        System.out.println("Parser-" + threadNum + " found:" + this.result.size());
        return this.result;
    }

}
