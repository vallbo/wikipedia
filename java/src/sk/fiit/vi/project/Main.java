package sk.fiit.vi.project;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import sk.fiit.vi.project.corrector.Storage;
import sk.fiit.vi.project.model.Record;
import sk.fiit.vi.project.output.SqlLite;
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
    public static boolean PRINT_RESULTS = false;
    public static Class CLUSTER_ALG = STCClusteringAlgorithm.class;

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            validateArgs(args);
            NUM_OF_THREADS = Integer.valueOf(getNumOfThreads(args));
            DATA_FILE = getDataFile(args);
            PRINT_RESULTS = getPrintResults(args);
            CLUSTER_ALG = getClusteringAlg(args);
        }
        System.out.println("CONFIGURATION");
        System.out.println("NUM_OF_THREADS:" + NUM_OF_THREADS);
        System.out.println("DATA_FILE:" + DATA_FILE);
        System.out.println("PRINT:" + PRINT_RESULTS);
        System.out.println("ALG:" + CLUSTER_ALG);
        System.out.println("=============");
        ParallelParser parser = new ParallelParser();
        List<Page> parsedTexts = parser.parseParallel(DATA_FILE, NUM_OF_THREADS);
        System.out.println("TOTAL IB:" + parsedTexts.size());
        System.out.println("=============");
        Storage.writeData(parsedTexts);
        Storage.correctRecords();
        List<Record> correctedRecords = Storage.getRecords();
        ArrayList<Document> documents = new ArrayList<Document>();
        for (Iterator<Record> i = correctedRecords.iterator(); i.hasNext(); ) {
            Record item = i.next();
            documents.add(new Document(item.getTitle(), item.getType()));
        }
        final Controller controller = ControllerFactory.createPooling(NUM_OF_THREADS);
        final ProcessingResult byTopicClusters = controller.process(documents, null, CLUSTER_ALG);
        SqlLite.writeResults(byTopicClusters.getClusters());
        if (PRINT_RESULTS) {
            SqlLite.printResults();
        }
    }

    private static Class getClusteringAlg(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].contentEquals("-alg")) {
                if (args[i + 1].contentEquals("lingo")) {
                    return LingoClusteringAlgorithm.class;
                }
                return CLUSTER_ALG;
            }
        }
        return CLUSTER_ALG;
    }

    private static String getNumOfThreads(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].contentEquals("-threads")) {
                return args[i + 1];
            }
        }
        return String.valueOf(NUM_OF_THREADS);
    }

    private static void validateArgs(String[] args) {
        String arguments[] = {"-file", "-threads", "-print", "-alg"};
        for (int i = 0; i < args.length; i += 2) {
            if (!Arrays.asList(arguments).contains(args[i])) {
                System.out.println("Usage: java -jar <path to program> [-threads <number of threads>, -file <path to source file>, -print <1/0>, -alg <stc|lingo>]");
                System.exit(123);
            }
        }
    }

    private static boolean getPrintResults(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].contentEquals("-print")) {
                return (args[i + 1].contentEquals("1"));
            }
        }
        return false;
    }

    private static String getDataFile(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].contentEquals("-file")) {
                return args[i + 1];
            }
        }
        return DATA_FILE;
    }

}
