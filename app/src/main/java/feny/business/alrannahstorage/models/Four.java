package feny.business.alrannahstorage.models;

public class Four<T,E,B,A> {
    public T first;
    public E second;
    public B third;
    public A fourth;

    public Four(T first, E second, B third, A fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}