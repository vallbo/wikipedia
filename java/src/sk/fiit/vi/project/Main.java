package sk.fiit.vi.project;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import sk.fiit.vi.project.formatter.SqlLite;
import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.parsers.ParallelParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.carrot2.core.*;

public class Main {

    public static int NUM_OF_THREADS = 2;
    public static String DATA_FILE = "data/data.xml";

    public static void main(String[] args) throws Exception {
        if(args.length>0) {
            validateArgs(args);
            NUM_OF_THREADS = Integer.valueOf(getNumOfThreads(args));
            DATA_FILE = getDataFile(args);
        }
        System.out.println("CONFIGURATION");
        System.out.println("NUM_OF_THREADS:" + NUM_OF_THREADS);
        System.out.println("DATA_FILE:"+DATA_FILE);
        System.out.println("=============");
        ParallelParser parser = new ParallelParser();
        List<Page> parsedTexts = parser.parseParallel(DATA_FILE, NUM_OF_THREADS);
        System.out.println("TOTAL IB:"+parsedTexts.size());
        System.out.println("=============");
        ArrayList<Document> documents = new ArrayList<Document>();
        for(Iterator<Page> i = parsedTexts.iterator(); i.hasNext(); ) {
            Page item = i.next();
            documents.add(new Document(item.getName(), item.getInfoBox().getType()));
        }
        final Controller controller = ControllerFactory.createPooling(NUM_OF_THREADS);
        final ProcessingResult byTopicClusters = controller.process(documents, null,
                LingoClusteringAlgorithm.class);
        SqlLite.writeResults(byTopicClusters.getClusters());
        SqlLite.printResults();
    }

    private static String getNumOfThreads(String[] args) {
        for (int i=0; i<args.length; i+=2) {
            if(args[i].contentEquals("-threads")) {
                return args[i+1];
            }
        }
        return String.valueOf(NUM_OF_THREADS);
    }

    private static String getDataFile(String[] args) {
        for (int i=0; i<args.length; i+=2) {
            if(args[i].contentEquals("-file")) {
                return args[i+1];
            }
        }
        return DATA_FILE;
    }

    private static void validateArgs(String[] args) {
        String arguments[] = {"-file","-threads"};
        for (int i=0; i<args.length; i+=2) {
            if(!Arrays.asList(arguments).contains(args[i])) {
                System.out.println("Usage: java -Xms2G -Xmm6G -jar <path to program> [-threads <number of threads>, -file <path to source file>] ");
                System.exit(123);
            }
        }
    }

}
