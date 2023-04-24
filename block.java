import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

// Transaction Data
class TransactionData {
    double amount;
    String senderKey;
    String receiverKey;
    long timeStamp;

    TransactionData(double amount, String senderKey, String receiverKey, long timeStamp) {
        this.amount = amount;
        this.senderKey = senderKey;
        this.receiverKey = receiverKey;
        this.timeStamp = timeStamp;
    }
}

// Block class
class Block {
    private int index;
    private long blockHash;
    private long previousHash;
    private TransactionData data;

    Block(int index, TransactionData data, long previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.blockHash = generateHash();
    }

    private long generateHash() {
        MessageDigest digest;
        String toHash = data.amount + data.senderKey + data.receiverKey + data.timeStamp + previousHash;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(toHash.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hash) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return Long.parseLong(hexString.toString(), 16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    long getBlockHash() {
        return blockHash;
    }

    long getPreviousHash() {
        return previousHash;
    }

    boolean isHashValid() {
        return generateHash() == blockHash;
    }
}

// Blockchain class
class Blockchain {
    private ArrayList<Block> chain;

    Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        TransactionData genesisData = new TransactionData(0, "None", "None", new Date().getTime());
        return new Block(0, genesisData, 0);
    }

    private Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    void addBlock(TransactionData data) {
        int index = chain.size() - 1;
        Block newBlock = new Block(index, data, getLatestBlock().getBlockHash());
        chain.add(newBlock);
    }

    boolean isChainValid() {
        int chainLength = chain.size();

        for (int i = 0; i < chainLength; i++) {
            Block currentBlock = chain.get(i);
            if (!currentBlock.isHashValid()) {
                return false;
            }

            if (i > 0) {
                Block previousBlock = chain.get(i - 1);
                if (currentBlock.getPreviousHash() != previousBlock.getBlockHash()) {
                    return false;
                }
            }
        }
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
    Blockchain blockchain = new Blockchain();

    // First transaction data
    TransactionData data1 = new TransactionData(1.5, "Akshat Mehra", "Utkarsh Tripathi", new Date().getTime());


    // Add first block
    blockchain.addBlock(data1);

    // Second transaction data
    TransactionData data1 = new TransactionData(1.5, "Akshat Jaimini", "Amit Goyal", new Date().getTime());


    // Add second block
    blockchain.addBlock(data2);

    // Check if chain is valid
    boolean isChainValid = blockchain.isChainValid();
    System.out.println("Is chain valid? " + isChainValid);
}
}
