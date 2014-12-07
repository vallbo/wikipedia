package sk.fiit.vi.project.parsers;

import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.xml.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by VB on 24/11/14.
 */
public class ParallelParser {

    public List<Page> parseParallel(String fileName, int numOfThreads) {
        final ExecutorService service;
        service = Executors.newFixedThreadPool(2);
        List<Future<List<Page>>> tasks = new ArrayList<Future<List<Page>>>();
        for(int threadNum = 0; threadNum<numOfThreads; threadNum++) {
            tasks.add(service.submit(new Parser(fileName, threadNum, numOfThreads)));
        }
        List<Page> parsedTexts = new ArrayList<Page>();
        try {
            for(int threadNum = 0; threadNum<numOfThreads; threadNum++) {
                parsedTexts.addAll(tasks.get(threadNum).get());
            }
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();
        }
        service.shutdownNow();
        return parsedTexts;
    }

}
