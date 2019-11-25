package search.system.searcher.service;

import org.springframework.stereotype.Service;
import search.system.searcher.model.Metric;

@Service
public class MetricService {

    public double calculateRecall(int a, int c) {
        return (double) a / ((double) a + (double) c);
    }


    public double calculatePrecision(int a, int b) {
        return (double) a / ((double) a + (double) b);
    }


    public double calculateAccuracy(int a, int b, int c, int d) {
        return ((double) a + (double) d) / ((double) a + (double) b + (double) c + (double) d);
    }


    public double calculateError(int a, int b, int c, int d) {
        return ((double) b + (double) c) / ((double) a + (double) b + (double) c + (double) d);
    }


    public double calculateFMeasure(int a, int b, int c) {
        return 2 / (1 / calculateRecall(a, c) + 1 / calculatePrecision(a, b));
    }

    public Metric generateMetric(int a, int b, int c, int d){
        return new Metric(calculateRecall(a,c), calculatePrecision(a,b), calculateAccuracy(a,b,c,d), calculateError(a,b,c,d), calculateFMeasure(a,b,c));
    }
}
