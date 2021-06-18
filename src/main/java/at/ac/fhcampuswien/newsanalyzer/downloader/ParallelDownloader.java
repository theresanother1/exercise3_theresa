package at.ac.fhcampuswien.newsanalyzer.downloader;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelDownloader extends Downloader {
        //executorservice
        //future

    public ParallelDownloader() {
    }

    @Override
    public int process(List<String> urls) {
        //dynamic thread amount with machine processors
        int workerThreadsAmount = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(workerThreadsAmount);

        //callables to to create tasks dynamically

        List<Future<String>> futureTitles = new ArrayList<>();
        int count = 0;

        //option 1
      /*  while (count < urls.size()){
            String curURL = urls.get(count);
            Callable<String> task = () -> saveUrl2File(curURL);
            futureTitles.add(pool.submit(task));
            count++;
        }*/
        /* List<String> downloadResults = new ArrayList<>();
        for (Future<String> result : futureTitles) {
            try {
                if (result.get() != null) {
                    downloadResults.add(result.get());
                }
            } catch (InterruptedException interruptedException){
                System.out.println("A result could not be added to the list.");
                interruptedException.printStackTrace();
            } catch (ExecutionException executionException){
                System.out.println("Could not execute getting a specific download result.");
                executionException.printStackTrace();
            }
        }*/


        //option 2

        //create list of all callables to be executed by the thread pool
        List<Callable<String>> allCallables = new ArrayList<>();
        while (count < urls.size()){
            String curURL = urls.get(count);
            Callable<String> task = () -> saveUrl2File(curURL);
            allCallables.add(task);
            count++;
        }

        try {
            //instead of submit each one by one -> invokeAll
        List<Future<String>> downloadResults = pool.invokeAll(allCallables);
        for (Future<String> result : downloadResults){
            String curFile = result.get();
            System.out.println(curFile);
            }
        } catch (ExecutionException e){
            System.out.println("Error in Execution, couldn't get a result.");
            e.printStackTrace();
        } catch (InterruptedException i){
            System.out.println("Interruption while getting a result");
            i.printStackTrace();
        }

        //test
       /* List<Callable<String>> allCallables= urls.stream()
                .forEach(() -> {
                    saveUrl2File(urls.get(count));
                    count++;
                });*/

        pool.shutdown();

     /*   downloadResults
                .forEach(System.out::println);*/

        return 0;
    }
}
