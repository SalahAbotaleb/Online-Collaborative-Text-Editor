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
        if (item.left == null) {
            String firstItemId = firstItem == null ? null : firstItem.id;
            String RightItemId = item.right == null ? null : item.right.id;
            System.out.println(firstItem);
            System.out.println(firstItemId);
            System.out.println(RightItemId);
            if (!Objects.equals(firstItemId, RightItemId) && firstItem.id.split("@")[1].compareTo(item.id.split("@")[1]) > 0) {
                System.out.println("here7");
                item.left = firstItem;
                System.out.println(item);
            } else {
                System.out.println("here1");
                item.right = firstItem;
                System.out.println("here2");
                if (firstItem != null) firstItem.left = item;
                System.out.println("here3");
                firstItem = item;
                System.out.println("here4");
                crdtMap.put(item.id, item);
                System.out.println("here");
                return;
            }
        }
        while (item.left.right != item.right && item.left.right.id.split("@")[1].compareTo(item.id.split("@")[1]) > 0) {
            item.left = item.left.right;
        }

        item.right = item.left.right;
        crdtMap.put(item.id, item);
        item.left.right = item;
        if (item.right != null) item.right.left = item;

    }

    public void delete(String key) {
        Item item = crdtMap.get(key);
        item.isDeleted = true;
    }

    public void format(String key, boolean bold, boolean italic) {
        Item item = crdtMap.get(key);
        item.isBold = bold;
        item.isItalic = italic;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Item current = firstItem;
        while (current != null) {
            if (!current.isDeleted)  sb.append(current.content);
            current = current.right;
        }
        return sb.toString();
    }

    public List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        Item current = firstItem;
        while (current != null) {
                items.add(current);
            current = current.right;
        }
        return items;
    }
}


