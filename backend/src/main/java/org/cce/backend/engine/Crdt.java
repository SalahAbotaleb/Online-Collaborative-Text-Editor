package org.cce.backend.engine;

import org.springframework.stereotype.Component;

import java.io.*;
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

    public Crdt(byte[] bytes) {
        InitCrdt(bytes);
    }

    public void InitCrdt(byte[] bytes) {
        List<Item> items = (List<Item>) getDeserializedCrdt(bytes);
        crdtMap = getCrdtMap(items);
        if (items.size() != 0) {
            firstItem = items.get(0);
        } else {
            firstItem = null;
        }
    }

    public Item getItem(String id) {
        return crdtMap.getOrDefault(id, null);
    }

    public void insert(String key, Item item) {
        if (item.getLeft() == null) {
            String firstItemId = firstItem == null ? null : firstItem.getId();
            String RightItemId = item.getRight() == null ? null : item.getRight().getId();
            if (!Objects.equals(firstItemId, RightItemId)
                    && firstItem.getId().split("@")[1].compareTo(item.getId().split("@")[1]) > 0) {
                item.setRight(firstItem);
            } else {
                item.setRight(firstItem);
                if (firstItem != null)
                    firstItem.setLeft(item);
                firstItem = item;
                crdtMap.put(item.getId(), item);
                return;
            }
        }
        while (item.getLeft().getRight() != item.getRight()
                && item.getLeft().getLeft().getId().split("@")[1].compareTo(item.getId().split("@")[1]) > 0) {
            item.setLeft(item.getLeft().getRight());
        }

        item.setRight(item.getLeft().getRight());
        crdtMap.put(item.getId(), item);
        item.getLeft().setRight(item);
        if (item.getRight() != null)
            item.getRight().setLeft(item);

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
            if (!current.isIsdeleted())
                sb.append(current.getContent());
            current = current.getRight();
        }
        return sb.toString();
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        Item current = firstItem;
        while (current != null) {
            items.add(current);
            current = current.getRight();
        }
        return items;
    }

    private List<Item> getClearData() {
        List<Item> items = new ArrayList<>();
        int cnt = 0;
        Item current = firstItem;
        while (current != null) {
            if (!current.isIsdeleted()) {
                Item left = cnt == 0 ? null : items.get(cnt - 1);
                Item item = Item.builder()
                        .id(cnt + "@_")
                        .left(left)
                        .isbold(current.isIsbold()).isdeleted(current.isIsdeleted())
                        .isitalic(current.isIsitalic()).content(current.getContent())
                        .operation(current.getOperation()).right(null).build();
                if (cnt != 0)
                    items.get(cnt - 1).setRight(item);
                items.add(item);
                cnt++;
            }
            current = current.getRight();
        }
        return items;
    }

    public byte[] getSerializedCrdt() {
        Object obj = getClearData();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    private HashMap<String, Item> getCrdtMap(List<Item> items) {
        HashMap<String, Item> crdtMap = new HashMap<>();
        for (Item item : items) {
            crdtMap.put(item.getId(), item);
        }
        return crdtMap;
    }

    private Object getDeserializedCrdt(byte[] bytes) {
        if (bytes.length == 0) {
            return new ArrayList<>();
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }
}
