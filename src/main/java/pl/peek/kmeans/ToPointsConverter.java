package pl.peek.kmeans;

import pl.peek.kmeans.impl.Point;

import java.util.List;

public interface ToPointsConverter<T> {

    List<Point> convert(T o);

}
