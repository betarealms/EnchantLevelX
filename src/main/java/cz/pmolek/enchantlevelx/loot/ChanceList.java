package cz.pmolek.enchantlevelx.loot;

import cz.pmolek.enchantlevelx.Tuple;

import javax.annotation.Nullable;
import java.util.*;

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
        double weight = tuple.getA();
        if (weight < 0 || weight > 1) {
            throw new IllegalArgumentException("Weight must be in the range [0, 1]");
        }
        internalList.add(tuple);
    }

    private void sort()
    {
        //Sort in reverse order
        internalList.sort(Comparator.comparingDouble(Tuple::getA));
    }

    @Nullable
    public V getWeightedRandom(Random rng) {
        if (internalList.isEmpty()) {
            throw new IllegalStateException("WeightedList is empty and no random value could be returned");
        }

        double randomValue = rng.nextDouble();

        for (Tuple<Double, V> tuple : internalList) {
            if (tuple.getA() >= randomValue) {
                return tuple.getB();
            }
        }

        return null;
    }

    public boolean removeByValue(V value) {
        Iterator<Tuple<Double, V>> iterator = internalList.iterator();
        while (iterator.hasNext()) {
            Tuple<Double, V> tuple = iterator.next();
            if (tuple.getB().equals(value)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeByWeight(double threshold) {
        Iterator<Tuple<Double, V>> iterator = internalList.iterator();
        while (iterator.hasNext()) {
            Tuple<Double, V> tuple = iterator.next();
            if (tuple.getA() <= threshold) {
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
                && (tuple.getA() instanceof Double && tuple.getB() != null)) {
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
        for (Tuple<Double, V> entry : c)
        {
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