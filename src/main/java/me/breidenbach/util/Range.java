package me.breidenbach.util;


import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
public interface Range<E> extends Iterable<E>, Iterator<E> {

    void reset();

    E[] generateRest();

    List<E> generateRestList();

    Stream<E> stream();
}
