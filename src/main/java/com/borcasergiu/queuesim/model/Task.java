package com.borcasergiu.queuesim.model;

public class Task implements Comparable<Task> {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getServiceTime() { return serviceTime; }
    public void setServiceTime(int serviceTime) { this.serviceTime = serviceTime; }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }
}