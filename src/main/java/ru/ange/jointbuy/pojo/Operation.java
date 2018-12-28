package ru.ange.jointbuy.pojo;

public class Operation {

    public enum Type {
        PURCHASE, REMITTANCE
    }

    private Type type;
    private double sum;
    private String name;

    public Operation() {}

    public Operation(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Operation setType(Type type) {
        this.type = type;
        return this;
    }

    public double getSum() {
        return sum;
    }

    public Operation setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public String getName() {
        return name;
    }

    public Operation setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "type=" + type +
                ", sum=" + sum +
                ", name='" + name + '\'' +
                '}';
    }
}
