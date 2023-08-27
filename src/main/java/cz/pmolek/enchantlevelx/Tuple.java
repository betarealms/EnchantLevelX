package cz.pmolek.enchantlevelx;

import java.util.Map;
import java.util.Objects;

/**
 * Tuple of two items.
 *
 * @param <A> the type of the first item.
 *
 * @param <B> the type of the second item.
 */
public class Tuple<A, B> {
  private final A valueA;
  private final B valueB;

  public Tuple(A a, B b) {
    this.valueA = a;
    this.valueB = b;
  }

  public static <A, B> Tuple<A, B> of(Map.Entry<A, B> entry) {
    return new Tuple<>(entry.getKey(), entry.getValue());
  }

  public static <A, B> Tuple<A, B> of(A a, B b) {
    return new Tuple<>(a, b);
  }

  public A getValueA() {
    return valueA;
  }

  public B getValueB() {
    return valueB;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tuple<?, ?> tuple = (Tuple<?, ?>) o;
    return Objects.equals(valueA, tuple.valueA) && Objects.equals(valueB, tuple.valueB);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valueA, valueB);
  }
}
