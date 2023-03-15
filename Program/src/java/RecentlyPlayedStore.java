package java;

import java.util.HashMap;
import java.util.Map;

public class RecentlyPlayedStore {
	
    private final int capacity;
    private final Map<String, Node> map = new HashMap<>();
    private Node head;
    private Node tail;

    public RecentlyPlayedStore(int capacity) {
        this.capacity = capacity;
    }

    public void playSong(String user, String song) {
        Node node = map.get(user);
        if (node == null) {
            node = new Node(user);
            map.put(user, node);
        }
        node.addSong(song);
        promote(node);
        if (map.size() > capacity) {
            evict();
        }
    }

    public String[] getRecentlyPlayed(String user) {
        Node node = map.get(user);
        return (node != null) ? node.getRecentSongs() : new String[0];
    }

    private void promote(Node node) {
        if (node == tail) {
            return;
        }
        if (node == head) {
            head = head.next;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        node.prev = tail;
        node.next = null;
        tail.next = node;
        tail = node;
    }

    private void evict() {
        map.remove(head.user);
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
    }

    private static class Node {
        private final String user;
        private final Map<String, SongNode> songMap = new HashMap<>();
        private SongNode head;
        private SongNode tail;

        private Node prev;
        private Node next;

        private Node(String user) {
            this.user = user;
        }

        private void addSong(String song) {
            SongNode node = songMap.get(song);
            if (node == null) {
                node = new SongNode(song);
                songMap.put(song, node);
            }
            if (node == tail) {
                return;
            }
            if (node == head) {
                head = head.next;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            node.prev = tail;
            node.next = null;
            tail.next = node;
            tail = node;
        }

        private String[] getRecentSongs() {
            String[] songs = new String[songMap.size()];
            SongNode node = tail;
            for (int i = songs.length - 1; i >= 0; i--) {
                songs[i] = node.song;
                node = node.prev;
            }
            return songs;
        }
    }

    private static class SongNode {
        private final String song;
        private SongNode prev;
        private SongNode next;

        private SongNode(String song) {
            this.song = song;
        }
    }
}



