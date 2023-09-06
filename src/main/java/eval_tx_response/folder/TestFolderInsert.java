package eval_tx_response.folder;

import eval_tx_response.TxTestTemplate;

public class TestFolderInsert {
    public static void main(String[] args) {
        TxTestTemplate plain = new FolderPlainInsert();
        TxTestTemplate sync = new FolderSyncInsert();

        System.out.println("[PLAIN INSERT] average response time : " + plain.getAvgExecutionTime(10) + " ms");
        System.out.println("[SYNC INSERT] average response time : " + sync.getAvgExecutionTime(10) + " ms");
    }
}
