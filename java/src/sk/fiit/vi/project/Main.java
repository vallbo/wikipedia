package sk.fiit.vi.project;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import sk.fiit.vi.project.formatter.ConsoleFormatter;
import sk.fiit.vi.project.model.InfoBox;
import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.parsers.BaseParser;
import sk.fiit.vi.project.parsers.InfoBoxParser;
import sk.fiit.vi.project.xml.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.carrot2.core.*;

public class Main {

    public static String testFile = "/shared/sample_enwiki-latest-pages-articles1.xml";
//    public static String testFile = "/shared/test1";

    public static void main(String[] args) throws Exception {
        Parser xmlParser = new Parser();
        xmlParser.parse(testFile);
        List<Page> parsedTexts = xmlParser.getResult();
        System.out.println(parsedTexts.size());
        System.out.println("=============");
        ArrayList<Document> documents = new ArrayList<Document>();
        for(Iterator<Page> i = parsedTexts.iterator(); i.hasNext(); ) {
            Page item = i.next();
            documents.add(new Document(item.getName(), item.getInfoBox().getType()));
//            documents.add(new Document(item.getName(), item.getInfoBox().getAttributes().keySet().toString()));
        }
        /* A controller to manage the processing pipeline. */
        final Controller controller = ControllerFactory.createSimple();

            /*
             * Perform clustering by topic using the Lingo algorithm. Lingo can
             * take advantage of the original query, so we provide it along with the documents.
             */
        final ProcessingResult byTopicClusters = controller.process(documents, null,
                LingoClusteringAlgorithm.class);
        final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
        ConsoleFormatter.displayClusters(clustersByTopic);
    }

}
