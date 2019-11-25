package search.system.searcher.model;

public class Metric {
    private double recall;
    private double precision;
    private double accuracy;
    private double error;
    private double fMeasure;

    public Metric(double recall, double precision, double accuracy, double error, double fMeasure) {
        this.recall = recall;
        this.precision = precision;
        this.accuracy = accuracy;
        this.error = error;
        this.fMeasure = fMeasure;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }
}
