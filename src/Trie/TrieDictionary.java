package Trie;

import java.util.Map;
import java.util.HashMap;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    int wordCount; // 이 노드를 루트로 하는 서브트리에 포함된 단어 수 (중복 단어는 1번만 카운트)

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        wordCount = 0;
    }
}

public class TrieDictionary {
    private TrieNode root;

    public TrieDictionary() {
        root = new TrieNode();
    }

    // 단어 추가
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.wordCount++; // 새 문자 추가 시, 서브트리의 단어 개수 증가
        }
        node.isEndOfWord = true;
    }

    // 단어 삭제
    public boolean delete(String word) {
        return delete(root, word, 0);
    }

    private boolean delete(TrieNode node, String word, int index) {
        if (index == word.length()) {
            if (!node.isEndOfWord) return false; // 단어가 존재하지 않음
            node.isEndOfWord = false;
            return node.children.isEmpty(); // 자식이 없는 경우, 현재 노드 삭제 가능
        }
        char c = word.charAt(index);
        TrieNode child = node.children.get(c);
        if (child == null) return false; // 단어가 존재하지 않음

        boolean shouldDeleteChild = delete(child, word, index + 1);

        if (shouldDeleteChild) {
            node.children.remove(c); // 자식 노드 삭제
        }
        child.wordCount--; // 삭제 시 서브트리의 단어 개수 감소
        return node.children.isEmpty() && !node.isEndOfWord; // 자식이 없고, 단어의 끝이 아니면 노드 삭제 가능
    }

    // Trie에 있는 단어 수 계산
    private int countWords(TrieNode node) {
        return node.wordCount;
    }

    // 특정 단어가 사전 순으로 몇 번째인지 계산
    public int getWordRank(String word) {
        TrieNode node = root;
        int rank = 0;

        for (char c = 'a'; c < word.charAt(0); c++) {
            if (node.children.containsKey(c)) {
                rank += countWords(node.children.get(c)); // 사전 순으로 앞에 있는 단어 개수 추가
            }
        }

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!node.children.containsKey(c)) {
                return -1; // 단어가 존재하지 않음
            }

            node = node.children.get(c);

            if (i < word.length() - 1) {
                // 현재 문자보다 작은 문자로 시작하는 모든 단어들 카운트
                for (char smallerChar = 'a'; smallerChar < word.charAt(i + 1); smallerChar++) {
                    if (node.children.containsKey(smallerChar)) {
                        rank += countWords(node.children.get(smallerChar));
                    }
                }
            }
        }

        return node.isEndOfWord ? rank + 1 : -1; // 단어가 존재하면 순위 반환
    }

    // 특정 접두사로 시작하는 단어 중에서 몇 번째인지 계산
    public int getWordRankWithPrefix(String word, String prefix) {
        TrieNode node = root;
        int rank = 0;

        // 접두사 탐색
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return -1; // 접두사가 존재하지 않음
            }
            node = node.children.get(c);
        }

        // 접두사 내에서 사전 순으로 단어 찾기
        for (int i = prefix.length(); i < word.length(); i++) {
            char c = word.charAt(i);
            if (!node.children.containsKey(c)) {
                return -1; // 단어가 접두사 내에 존재하지 않음
            }

            node = node.children.get(c);

            if (i < word.length() - 1) {
                for (char smallerChar = 'a'; smallerChar < word.charAt(i + 1); smallerChar++) {
                    if (node.children.containsKey(smallerChar)) {
                        rank += countWords(node.children.get(smallerChar));
                    }
                }
            }
        }

        return node.isEndOfWord ? rank + 1 : -1; // 단어가 존재하면 순위 반환
    }

    public static void main(String[] args) {
        TrieDictionary trie = new TrieDictionary();

        // 단어 추가
        trie.insert("apple");
        trie.insert("app");
        trie.insert("banana");
        trie.insert("ball");
        trie.insert("cat");

        // 단어 순위 확인
        System.out.println(trie.getWordRank("apple")); // 3번째 단어
        System.out.println(trie.getWordRank("banana")); // 4번째 단어
        System.out.println(trie.getWordRank("cat")); // 5번째 단어

        // 특정 접두사 내에서 단어 순위 확인
        System.out.println(trie.getWordRankWithPrefix("apple", "a")); // 2번째 단어 ('a'로 시작하는 단어 중)
        System.out.println(trie.getWordRankWithPrefix("app", "a")); // 1번째 단어 ('a'로 시작하는 단어 중)

        // 단어 삭제
        trie.delete("banana");
        System.out.println(trie.getWordRank("banana")); // -1 (존재하지 않음)
    }
}
