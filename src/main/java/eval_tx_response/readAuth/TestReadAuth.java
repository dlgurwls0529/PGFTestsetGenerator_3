package eval_tx_response.readAuth;

import eval_tx_response.TxTestTemplate;

public class TestReadAuth {
    public static void main(String[] args) {
        TxTestTemplate readAuthByACP_record = new GetReadAuthByACP_record();
        TxTestTemplate readAuthByACP_block = new GetReadAuthByACP_block();

        System.out.println("[RECORD] average response time : " + readAuthByACP_record.getAvgExecutionTime(10) + " ms");
        System.out.println("[BLOCK] average response time : " + readAuthByACP_block.getAvgExecutionTime(10) + " ms");
    }
}
