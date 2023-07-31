import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

// Transaction Data
class TransactionData {
    double amount;
    String senderKey;
    String receiverKey;
    long timestamp;

    TransactionData(double amount, String senderKey, String receiverKey) {
        this.amount = amount;
        this.senderKey = senderKey;
        this.receiverKey = receiverKey;
        this.timestamp = new Date().getTime();
    }
}

// Block class
class Block {
    private int index;
    private long blockHash;
    private long previousHash;

    Block(int index, TransactionData data, long prevHash) {
        this.index = index;
        this.data = data;
        this.previousHash = prevHash;
        this.blockHash = generateHash();
    }

    private long generateHash() {
        String toHash = String.valueOf(data.amount) + data.receiverKey + data.senderKey + String.valueOf(data.timestamp);
        return Objects.hash(toHash) + previousHash;
    }

    long getHash() {
        return blockHash;
    }

    long getPreviousHash() {
        return previousHash;
    }

    TransactionData data;

    boolean isHashValid() {
        return generateHash() == blockHash;
    }
}

// Blockchain
class Blockchain {
    private List<Block> chain;

    Blockchain() {
        chain = new ArrayList<>();
        Block genesis = createGenesisBlock();
        chain.add(genesis);
    }

    private Block createGenesisBlock() {
        TransactionData data = new TransactionData(0, "None", "None");
        Block genesis = new Block(0, data, Objects.hash(0));
        return genesis;
    }

    Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    void addBlock(TransactionData data) {
        int index = chain.size() - 1;
        Block newBlock = new Block(index, data, getLatestBlock().getHash());
        chain.add(newBlock);
    }

    boolean isChainValid() {
        int chainLen = chain.size();

        for (int i = 0; i < chainLen; i++) {
            Block currentBlock = chain.get(i);
            if (!currentBlock.isHashValid()) {
                return false;
            }

            if (chainLen > 1 && i > 0) {
                Block previousBlock = chain.get(i - 1);
                if (currentBlock.getPreviousHash() != previousBlock.getHash()) {
                    return false;
                }
            }
        }

        return true;
    }
}

class Main {
    public static void main(String[] args) {
        Blockchain TWSCCoin = new Blockchain();

        TransactionData data1 = new TransactionData(1.5, "Akshat Mehra", "Utkarsh Tripathi");
        TWSCCoin.addBlock(data1);

        System.out.println("Is chain Valid?");
        System.out.println(TWSCCoin.isChainValid());

        TransactionData data2 = new TransactionData(1.5, "Amit Goyal", "Akshat Jaimini");
        TWSCCoin.addBlock(data2);

        System.out.println("Now is the chain valid?");
        System.out.println(TWSCCoin.isChainValid());

        Block hackBlock = TWSCCoin.getLatestBlock();
        hackBlock.data.amount = 10000;
        hackBlock.data.receiverKey = "Akshavya - From Crypto Enthusiasts";

        System.out.println("Now is the chain still valid?");
        System.out.println(TWSCCoin.isChainValid());
    }
}
