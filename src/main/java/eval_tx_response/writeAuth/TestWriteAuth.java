package eval_tx_response.writeAuth;

import eval_tx_response.TxTestTemplate;

public class TestWriteAuth {
    public static void main(String[] args) {
        TxTestTemplate writeAuthByACP_record = new GetWriteAuthByACP_record();
        TxTestTemplate writeAuthByACP_block = new GetWriteAuthByACP_block();

        System.out.println("[RECORD] average response time : " + writeAuthByACP_record.getAvgExecutionTime(10) + " ms");
        System.out.println("[BLOCK] average response time : " + writeAuthByACP_block.getAvgExecutionTime(10) + " ms");
    }
}
