package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallest = customers.firstEntry();
        return smallest == null ? null : Map.entry(smallest.getKey().clone(), smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = customers.higherEntry(customer);
        return higherEntry == null ? null : Map.entry(higherEntry.getKey().clone(), higherEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer.clone(), data);
    }
}
