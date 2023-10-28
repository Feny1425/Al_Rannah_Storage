package feny.business.alrannahstorage.models;

public class Three<T,E,B> {
    public T first;
    public E second;
    public B third;

    public Three(T t, E e, B b) {
        this.first = t;
        this.second = e;
        this.third = b;
    }
}