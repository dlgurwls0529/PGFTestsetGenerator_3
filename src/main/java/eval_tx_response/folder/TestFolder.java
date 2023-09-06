package eval_tx_response.folder;

import eval_tx_response.TxTestTemplate;
import eval_tx_response.readAuth.GetReadAuthByACP_block;
import eval_tx_response.readAuth.GetReadAuthByACP_record;

public class TestFolder {
    public static void main(String[] args) {
        TxTestTemplate master = new SearchFromMaster();
        TxTestTemplate slave = new SearchFromSlave();

        System.out.println("[FROM MASTER] average response time : " + master.getAvgExecutionTime(10) + " ms");
        System.out.println("[FROM SLAVE] average response time : " + slave.getAvgExecutionTime(10) + " ms");
    }
}
