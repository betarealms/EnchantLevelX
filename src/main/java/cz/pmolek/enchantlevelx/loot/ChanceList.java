package cz.pmolek.enchantlevelx.loot;

import cz.pmolek.enchantlevelx.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

/**
 * List of weighted values, used for picking items by chance.
 *
 * @param <V> type of the value.
 */
public class ChanceList<V> implements Collection<Tuple<Double, V>> {
  private List<Tuple<Double, V>> internalList = new ArrayList<>();

  public ChanceList() {

  }

  public ChanceList(Collection<Tuple<Double, V>> list) {
    addAll(list);
  }

  @Override
  public boolean add(Tuple<Double, V> tuple) {
    internalAdd(tuple);
    sort();
    return true;
  }

  private void internalAdd(Tuple<Double, V> tuple) {
    double weight = tuple.getValueA();
    if (weight < 0 || weight > 1) {
      throw new IllegalArgumentException("Weight must be in the range [0, 1]");
    }
    internalList.add(tuple);
  }


  /**
   * Sorts the internal list in ascending order based on the weight of the values.
   */
  private void sort() {
    internalList.sort(Comparator.comparingDouble(Tuple::getValueA));
  }

  /**
   * Returns a weighted random value from the list.
   *
   * @param  rng  the random number generator to use
   * @return      the randomly selected value
   */
  @Nullable
  public V getWeightedRandom(Random rng) {
    if (internalList.isEmpty()) {
      throw new IllegalStateException(
          "WeightedList is empty and no random value could be returned");
    }

    double randomValue = rng.nextDouble();

    for (Tuple<Double, V> tuple : internalList) {
      if (tuple.getValueA() >= randomValue) {
        return tuple.getValueB();
      }
    }

    return null;
  }

  /**
   * Removes the first occurrence of the specified value from the internal list.
   *
   * @param  value  the value to be removed
   * @return        true if the value is found and removed, false otherwise
   */
  public boolean removeByValue(V value) {
    Iterator<Tuple<Double, V>> iterator = internalList.iterator();
    while (iterator.hasNext()) {
      Tuple<Double, V> tuple = iterator.next();
      if (tuple.getValueB().equals(value)) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  /**
   * Removes elements from the internal list based on a weight threshold.
   *
   * @param  threshold  the weight threshold used to determine which elements to remove
   * @return         true if any elements were removed, false otherwise
   */
  public boolean removeByWeight(double threshold) {
    Iterator<Tuple<Double, V>> iterator = internalList.iterator();
    while (iterator.hasNext()) {
      Tuple<Double, V> tuple = iterator.next();
      if (tuple.getValueA() <= threshold) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }


  @Override
  public int size() {
    return internalList.size();
  }

  @Override
  public boolean isEmpty() {
    return internalList.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return internalList.contains(o);
  }

  @Override
  public Iterator<Tuple<Double, V>> iterator() {
    return internalList.iterator();
  }

  @Override
  public Object[] toArray() {
    return internalList.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return internalList.toArray(a);
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof Tuple<?, ?> tuple
        && (tuple.getValueA() instanceof Double && tuple.getValueB() != null)) {
      return this.removeIf(t -> t.equals(tuple));
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return internalList.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends Tuple<Double, V>> c) {
    for (Tuple<Double, V> entry : c) {
      internalAdd(entry);
    }
    sort();
    return !c.isEmpty();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    internalList.clear();
  }
}