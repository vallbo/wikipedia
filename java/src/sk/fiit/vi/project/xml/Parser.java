package sk.fiit.vi.project.xml;

import org.xml.sax.SAXException;
import sk.fiit.vi.project.model.InfoBox;
import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.parsers.BaseParser;
import sk.fiit.vi.project.parsers.InfoBoxParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VB on 21/11/14.
 */
public class Parser {

    public List<Page> result = new ArrayList();

    public void parse(String fileName) throws IOException, SAXException, ParserConfigurationException {
        XMLReader r = new XMLReader();
        r.addHandler("page", new
                        NodeHandler() {
                            @Override
                            public void process(StructuredNode node) throws XPathExpressionException {
                                String title = node.queryString("title/text()");
                                String text = node.queryString("//text/text()").replaceAll("[\r\n]+", " ");;
                                if (!text.matches("^(?i).*infobox.*$")) {
                                    return;
                                }
                                String infoboxString = BaseParser.parseString(text);
                                if(infoboxString.isEmpty()) {
                                    return;
                                }
                                result.add(new Page(title, InfoBoxParser.parseInfoBoxData(infoboxString)));
                            }
                        }
        );
        r.parse(new FileInputStream(fileName));
    }

    public List<Page> getResult() {
        return this.result;
    }

}
