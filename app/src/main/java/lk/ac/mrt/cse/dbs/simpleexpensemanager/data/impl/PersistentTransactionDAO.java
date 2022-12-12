package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DataBase.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DataBaseHelper dataBaseHelper;
    public PersistentTransactionDAO(DataBaseHelper db){this.dataBaseHelper = db;}
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        //Check whether account is exist or not.
        Cursor result = dataBaseHelper.getAccountByNo(accountNo);
        if(!result.moveToFirst()){
            System.out.println("PLEASE ENTER A VALID ACCOUNT NUMBER!");
            return;
        }

        //Check for the current balance is sufficient.
        double CurrentAmount = result.getDouble(3);
        if(ExpenseType.EXPENSE == expenseType && (CurrentAmount-amount)<0){
            System.out.println("CURRENT BALANCE IS NOT ENOUGH!");
            return;
        }

        DateFormat simpleFormat = new SimpleDateFormat("yyyy-mm-dd");
        String simpleDate = simpleFormat.format(date);
        dataBaseHelper.addTransactionLog(accountNo,expenseType.name(),amount,simpleDate);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList = new ArrayList<>();
        Transaction transaction;

        Cursor cursor = dataBaseHelper.getAllTransactions();

        if(!cursor.moveToFirst()){
            return transactionsList;
        }

        do {
            Date date = null;
            String stringDate = cursor.getString(1);
            try {
                date = new SimpleDateFormat("yyyy-mm-dd").parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expense = ExpenseType.valueOf(cursor.getString(3));
            String acc_no = cursor.getString(2);
            double amount = cursor.getDouble(4);
            transaction = new Transaction(date,acc_no,expense,amount);
            transactionsList.add(transaction);
        }
        while(cursor.moveToNext());

        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = getAllTransactionLogs();

        //Check the limit exceed the list size.
        if(limit>=transactionList.size()){
            return transactionList;
        }
        return transactionList.subList((transactionList.size()-limit),transactionList.size());
    }
}
