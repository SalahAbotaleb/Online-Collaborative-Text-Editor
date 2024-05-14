package org.cce.backend.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class Crdt {
    private HashMap<String, Item> crdtMap;
    private Item firstItem;

    public Crdt() {
        crdtMap = new HashMap<>();
    }

    public Item getItem(String id){
        return crdtMap.getOrDefault(id,null);
    }
    public void insert(String key, Item item) {
        if (item.getLeft() == null) {
            String firstItemId = firstItem == null ? null : firstItem.getId();
            String RightItemId = item.getRight() == null ? null : item.getRight().getId();
            System.out.println(firstItem);
            System.out.println(firstItemId);
            System.out.println(RightItemId);
            if (!Objects.equals(firstItemId, RightItemId) && firstItem.getId().split("@")[1].compareTo(item.getId().split("@")[1]) > 0) {
                System.out.println("here7");
                item.setRight(firstItem);
                System.out.println(item);
            } else {
                System.out.println("here1");
                item.setRight(firstItem);
                System.out.println("here2");
                if (firstItem != null) firstItem.setLeft(item);
                System.out.println("here3");
                firstItem = item;
                System.out.println("here4");
                crdtMap.put(item.getId(), item);
                System.out.println("here");
                return;
            }
        }
        while (item.getLeft().getRight() != item.getRight() && item.getLeft().getLeft().getId().split("@")[1].compareTo(item.getId().split("@")[1]) > 0) {
            item.setLeft(item.getLeft().getRight());
        }

        item.setRight(item.getLeft().getRight());
        crdtMap.put(item.getId(), item);
        item.getLeft().setRight(item);
        if (item.getRight() != null) item.getRight().setLeft(item);

    }

    public void delete(String key) {
        Item item = crdtMap.get(key);
        item.setIsdeleted(true);
        item.setOperation("delete");
    }

    public void format(String key, boolean bold, boolean italic) {
        Item item = crdtMap.get(key);
        item.setIsbold(bold);
        item.setIsitalic(italic);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Item current = firstItem;
        while (current != null) {
            if (!current.isIsdeleted())  sb.append(current.getContent());
            current = current.getRight();
        }
        return sb.toString();
    }

    public List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        Item current = firstItem;
        while (current != null) {
                items.add(current);
            current = current.getRight();
        }
        return items;
    }

    public List<Item> getClearData(){
        List<Item> items = new ArrayList<>();
        Item current = firstItem;
        while (current != null) {
            if (!current.isIsdeleted()) items.add(current);
            current = current.getRight();
        }
        return items;
    }
}


