package dsa.datastructures.lists;

public interface List<T> {

    // Adds an element to the end of the list
    void add(T element);

    // Inserts an element at the specified index
    void add(int index, T element);

    // Returns the element at the specified index
    T get(int index);

    // Replaces the element at the specified index
    T set(int index, T element);

    // Removes and returns the element at the specified index
    T remove(int index);

    // Removes the first occurrence of the specified element
    boolean remove(T element);

    // Returns the number of elements in the list
    int size();

    // Returns true if the list contains no elements
    boolean isEmpty();

    // Returns true if the list contains the specified element
    boolean contains(T element);

    // Returns the index of the first occurrence of the element,
    // or -1 if the element is not found
    int indexOf(T element);

    // Removes all elements from the list
    void clear();
}