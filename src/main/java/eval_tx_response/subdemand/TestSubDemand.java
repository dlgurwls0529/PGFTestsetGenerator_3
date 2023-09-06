package eval_tx_response.subdemand;

import eval_tx_response.TxTestTemplate;

public class TestSubDemand {
    public static void main(String[] args) {
        TxTestTemplate cluster = new GetSubDemandByClustered();
        TxTestTemplate unCluster = new GetSubDemandByUnClustered();


        // todo : 인덱스 바꾸는 과정 잘못된듯.

        /* CLUSTERED
            ALTER TABLE SubscribeDemand DROP PRIMARY KEY;
            ALTER TABLE SubscribeDemand ADD PRIMARY KEY (folderCP, accountCP);
         */

        /* NON_CLUSTERED
            ALTER TABLE SubscribeDemand DROP PRIMARY KEY;
            ALTER TABLE SubscribeDemand ADD PRIMARY KEY (accountCP, folderCP);
         */

        System.out.println("[CLUSTERED] average response time : " + cluster.getAvgExecutionTime(10) + " ms");
        System.out.println("[UnCLUSTERED] average response time : " + unCluster.getAvgExecutionTime(10) + " ms");
    }
}
