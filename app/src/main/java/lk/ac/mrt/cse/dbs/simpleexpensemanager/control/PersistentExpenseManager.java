
package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DataBase.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {

        DataBaseHelper dbHelper = new DataBaseHelper(context);

        TransactionDAO persistentTransactionDAB = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAB);

        AccountDAO persistentAccountDAB = new PersistentAccountDAO(dbHelper, context);
        setAccountsDAO(persistentAccountDAB);
    }
}